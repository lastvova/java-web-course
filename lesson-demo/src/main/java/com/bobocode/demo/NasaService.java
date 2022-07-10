package com.bobocode.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NasaService {

    @SneakyThrows
    public String getLargestUrl(Integer sol) {
        var uri = String.format("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=%d&api_key=DEMO_KEY", sol);
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode node = new ObjectMapper().readTree(response.body());

        Optional<Map.Entry<String, Long>> img_src = node.findValues("img_src").parallelStream()
                .map(JsonNode::asText)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Function.identity(), url -> getLength(client, url)),
                        map -> map.entrySet().parallelStream()
                                .max(Map.Entry.comparingByValue())));
        return img_src.get().getKey();

    }

    @SneakyThrows
    private long getLength(HttpClient client, String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
        return client.send(request, HttpResponse.BodyHandlers.discarding())
                .headers()
                .firstValueAsLong("Content-Length")
                .orElseThrow();
    }
}
