package com.axenon.weatherfetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@Component
public class FetchWeather {

    RestTemplate restTemplate;
    GeoCoder geoCoder;


    @Autowired
    public FetchWeather(RestTemplate restTemplate, GeoCoder geoCoder) {
        this.restTemplate = restTemplate;
        this.geoCoder = geoCoder;
    }

    public String fetchWeather(String query) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();

        String response = geoCoder.geocodeSync(query);
        JsonNode responseJsonNode = mapper.readTree(response);

        JsonNode items = responseJsonNode.get("items");

        String lat = "";
        String lng = "";

        for (JsonNode item : items) {
            JsonNode position = item.get("position");
            lat = position.get("lat").asText();
            lng = position.get("lng").asText();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.USER_AGENT, "Mozilla/17.0");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        return restTemplate.exchange("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=" + lat + "&lon="
                + lng + "&altitude=59", HttpMethod.GET, entity, String.class).getBody();
    }
}
