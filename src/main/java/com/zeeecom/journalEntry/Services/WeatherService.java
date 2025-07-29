package com.zeeecom.journalEntry.Services;

import com.zeeecom.journalEntry.Cache.AppCache;
import com.zeeecom.journalEntry.WeatherApiResponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private RestTemplate restTemplate;
    private AppCache appCacheObj;

    //Constructor injection
    public WeatherService(RestTemplate restTemplate,AppCache appCacheObj){
        this.restTemplate=restTemplate;
        this.appCacheObj=appCacheObj;
    }

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherResponse getWeather(String city){
        String finalAPI=appCacheObj.appCache.get(AppCache.Keys.WEATHER_API.toString()).replace("<apiKey>",apiKey).replace("<city>",city);

        //This is to consume the API
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }
}
