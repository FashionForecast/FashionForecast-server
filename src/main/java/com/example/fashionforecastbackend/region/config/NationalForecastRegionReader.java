package com.example.fashionforecastbackend.region.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.region.domain.Address;
import com.example.fashionforecastbackend.region.domain.Region;
import com.opencsv.CSVReader;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NationalForecastRegionReader {

	private final CSVReader csvReader;

	public NationalForecastRegionReader(@Qualifier("national_forecast_regions") CSVReader csvReader) {
		this.csvReader = csvReader;
	}

	public Set<Region> read() {

		try {
			Set<Region> regions = new HashSet<>();
			String[] fields;

			// 첫 번째 줄(헤더)을 읽고 무시
			csvReader.readNext();

			while ((fields = csvReader.readNext()) != null) {
				if (fields.length < 6) {
					continue; // 잘못된 형식의 라인은 무시합니다.
				}
				Region region = convertToRegion(fields);
				regions.add(region);
			}
			return regions;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException("national_forecast_regions.csv을 읽어오는데 실패하였습니다.");
		}

	}

	private Region convertToRegion(String[] fields) {
		String code = fields[0];
		String city = fields[1];
		String district = fields[2];
		String neighborhood = fields[3];
		double nx = Double.parseDouble(fields[4]);
		double ny = Double.parseDouble(fields[5]);

		Address address = new Address(code, city, district, neighborhood);
		return Region.builder()
			.address(address)
			.nx(nx)
			.ny(ny)
			.build();
	}
}
