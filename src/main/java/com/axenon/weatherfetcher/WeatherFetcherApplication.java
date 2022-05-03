package com.axenon.weatherfetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class WeatherFetcherApplication {



    public static void main(String[] args) throws IOException, InterruptedException {

        FetchWeather fetchWeather = new FetchWeather(new RestTemplate(), new GeoCoder());
        SpringApplication.run(WeatherFetcherApplication.class, args);
        System.out.println(fetchWeather.fetchWeather("Stockholm"));
    }
}
