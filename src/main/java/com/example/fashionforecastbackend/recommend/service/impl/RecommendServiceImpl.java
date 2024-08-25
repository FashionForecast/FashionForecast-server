package com.example.fashionforecastbackend.recommend.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;
import com.example.fashionforecastbackend.global.error.exception.OutfitTypeNotFoundException;
import com.example.fashionforecastbackend.global.error.exception.TempStageNotFoundException;
import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.domain.OutfitType;
import com.example.fashionforecastbackend.outfit.domain.repository.OutfitRepository;
import com.example.fashionforecastbackend.recommend.domain.Recommendation;
import com.example.fashionforecastbackend.recommend.domain.TempCondition;
import com.example.fashionforecastbackend.recommend.dto.RecommendRequest;
import com.example.fashionforecastbackend.recommend.dto.RecommendResponse;
import com.example.fashionforecastbackend.recommend.service.RecommendService;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.domain.repository.TempStageRepository;
import com.example.fashionforecastbackend.weather.dto.request.WeatherRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;
import com.example.fashionforecastbackend.weather.service.WeatherService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecommendServiceImpl implements RecommendService {

	private final OutfitRepository outfitRepository;
	private final TempStageRepository tempStageRepository;
	private final WeatherService weatherService;

	@Transactional
	@Override
	public List<RecommendResponse> getRecommendedOutfit(RecommendRequest recommendRequest) {
		WeatherRequest weatherRequest = new WeatherRequest(LocalDateTime.now(),
			recommendRequest.startTime(), recommendRequest.endTime(), recommendRequest.nx(), recommendRequest.ny());
		WeatherResponse weather = weatherService.getWeather(weatherRequest);

		TempStage tempStage;
		if (weather.extremumTmp() < 5) {
			if (recommendRequest.tempCondition() == TempCondition.COOL) {
				throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
			}
		}
		if (weather.extremumTmp() >= 28) {
			if (recommendRequest.tempCondition() == TempCondition.WARM) {
				throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
			}
		}
		if (recommendRequest.tempCondition() == TempCondition.COOL) {
			tempStage = tempStageRepository.findByWeatherAndCoolOption(weather.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		} else if (recommendRequest.tempCondition() == TempCondition.NORMAL) {
			tempStage = tempStageRepository.findByWeather(weather.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		} else if (recommendRequest.tempCondition() == TempCondition.WARM) {
			tempStage = tempStageRepository.findByWeatherAndWarmOption(weather.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		} else {
			throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
		}

		List<Outfit> outfits = new ArrayList<>(tempStage.getRecommendations().stream()
			.map(Recommendation::getOutfit).toList());

		determineUmbrella(weather, outfits);
		determineLayered(weather, outfits);

		return outfits.stream().map(RecommendResponse::of).toList();
	}

	private void determineUmbrella(WeatherResponse weather, List<Outfit> outfits) {
		if (weather.maximumPop() >= 30) {
			Outfit outfit;
			if (weather.maximumPcp() >= 3) {
				outfit = outfitRepository.findByUmbrellaType(OutfitType.BASIC_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			} else {
				outfit = outfitRepository.findByUmbrellaType(OutfitType.FOLDING_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			}
			outfits.add(outfit);
		} else {
			if (weather.maximumPcp() >= 3) {
				Outfit outfit = outfitRepository.findByUmbrellaType(OutfitType.FOLDING_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
				outfits.add(outfit);
			}
		}

	}

	private void determineLayered(WeatherResponse weather, List<Outfit> outfits) {
		if (weather.maxMinTmpDiff() >= 10) {
			Outfit outfit = outfitRepository.findByUmbrellaType(OutfitType.LAYERED)
				.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			outfits.add(outfit);
		}
	}
}
