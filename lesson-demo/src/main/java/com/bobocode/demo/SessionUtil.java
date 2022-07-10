package com.bobocode.demo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class SessionUtil {

    private final String SESSION_ID = "SESSION_ID";
    private final Map<String, Map<String, String>> sessionMap = new HashMap<>();

    public Optional<String> getAttribute(HttpServletRequest request, String attributeName) {
        return getSessionId(request)
                .map(sessionMap::get)
                .map(a -> a.get(attributeName));
    }

    private Optional<String> getSessionId(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equalsIgnoreCase(SESSION_ID))
                .findAny()
                .map(Cookie::getValue);
    }

    public void setAttribute(HttpServletRequest request, HttpServletResponse response,
                             String attributeName, String attributeValue) {
        String sessionId = getSessionId(request)
                .orElseGet(() -> UUID.randomUUID().toString());
        sessionMap.computeIfAbsent(sessionId, id -> new HashMap<>())
                .put(attributeName, attributeValue);
        response.addCookie(new Cookie(SESSION_ID, sessionId));
    }
}
