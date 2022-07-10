package com.bobocode.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/evening")
public class EveningServlet extends HttpServlet {

    private static final String NAME_QUERY_PARAM = "name";
    private static final String DEFAULT_NAME = "Buddy";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var writer = resp.getWriter()) {
            var queryParamValue = Optional.ofNullable(req.getParameter(NAME_QUERY_PARAM));

            String name = queryParamValue
                    .or(() -> SessionUtil.getAttribute(req, NAME_QUERY_PARAM))
                    .orElse(DEFAULT_NAME);

            if (queryParamValue.isPresent()) {
                SessionUtil.setAttribute(req, resp, NAME_QUERY_PARAM, name);
            }

            writer.printf("Good evening, %s", name);
        }
    }
}
