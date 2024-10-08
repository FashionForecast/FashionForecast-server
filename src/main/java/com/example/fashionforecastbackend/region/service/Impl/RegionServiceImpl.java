package com.example.fashionforecastbackend.region.service.Impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.fashionforecastbackend.region.config.NationalForecastRegionReader;
import com.example.fashionforecastbackend.region.domain.Region;
import com.example.fashionforecastbackend.region.domain.repository.RegionRepository;
import com.example.fashionforecastbackend.region.service.RegionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionServiceImpl implements RegionService {

	private final RegionRepository regionRepository;
	private final NationalForecastRegionReader nationalForecastRegionReader;

	public void writeNationalForecastRegions() {

		if (regionRepository.isExist()) {
			log.info("전국 기상 예보 구역 정보가 이미 존재합니다");
			return;
		}
		registRegions();
		log.info("전국 기상 예보 구역 정보 저장 완료");

	}

	private void registRegions() {
		Set<Region> regions = nationalForecastRegionReader.read();
		regionRepository.saveAll(regions);
	}
}
