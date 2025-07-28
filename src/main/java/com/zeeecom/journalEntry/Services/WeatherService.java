package com.zeeecom.journalEntry.Services;

import com.zeeecom.journalEntry.WeatherApiResponse.WeatherResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private RestTemplate restTemplate;

    //Constructor injection
    public WeatherService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    private final String apiKey="6b384b4f4bc827444186c71e82877e12";
    private static final String API="http://api.weatherstack.com/current?access_key=API_Key&query=CITY";

    public WeatherResponse getWeather(String city){
        String finalAPI=API.replace("API_Key",apiKey).replace("CITY",city);

        //This is to consume the API
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }
}
