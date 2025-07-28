package com.zeeecom.journalEntry.WeatherApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse {
    public Current current;

    @Getter
    @Setter
    public static class Current {
        public int temperature;

        @JsonProperty("weather_description")
        public List<String> weatherDescriptions;

        public double precip;
        public int humidity;

        @JsonProperty("feelslike")
        public int feelsLike;
    }
}
