package com.example.fashionforecastbackend.global.util;

import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FileReader {

    @Value("${national-forecast-regions}")
    private String fileName;

    @Bean
    @Qualifier("national_forecast_regions")
    public CSVReader csvReader() throws Exception {
        ClassPathResource resource = new ClassPathResource(fileName);
        return new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
    }
}
