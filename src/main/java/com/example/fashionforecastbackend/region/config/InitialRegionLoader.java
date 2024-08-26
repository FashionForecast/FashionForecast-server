package com.example.fashionforecastbackend.region.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.region.service.RegionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "initialize-region.enabled", havingValue = "true")
public class InitialRegionLoader implements CommandLineRunner {

	private final RegionService regionService;

	@Override
	public void run(String... args) throws Exception {
		regionService.writeNationalForecastRegions();
	}
}
