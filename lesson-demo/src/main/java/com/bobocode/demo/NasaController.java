package com.bobocode.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/pictures")
public class NasaController {

    @Autowired
    private NasaService nasaService;

    @GetMapping("/{sol}/largest")
    public ResponseEntity getPictures(@PathVariable("sol") Integer sol) {
        String largestUrl = nasaService.getLargestUrl(sol);
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(largestUrl))
                .build();
    }
}
