package com.example.fashionforecastbackend.recommend.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

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
import com.example.fashionforecastbackend.weather.dto.WeatherSummaryResponse;
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
		WeatherSummaryResponse weatherSummary = weatherService.getWeatherByTime(recommendRequest);

		TempStage tempStage;
		if (recommendRequest.startTime().getMonth().getValue() >= 5
			&& recommendRequest.startTime().getMonth().getValue() <= 9) {
			if (recommendRequest.tempCondition() == TempCondition.COOL) {
				tempStage = tempStageRepository.findByWeatherAndCoolOption(weatherSummary.min(), weatherSummary.max())
					.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
			} else if (recommendRequest.tempCondition() == TempCondition.NORMAL) {
				tempStage = tempStageRepository.findMinByWeather(weatherSummary.min(), weatherSummary.max())
					.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
			} else {
				throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
			}

		} else {
			if (recommendRequest.tempCondition() == TempCondition.WARM) {
				tempStage = tempStageRepository.findByWeatherAndWarmOption(weatherSummary.min(), weatherSummary.max())
					.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
			} else if (recommendRequest.tempCondition() == TempCondition.NORMAL) {
				tempStage = tempStageRepository.findMaxByWeather(weatherSummary.min(), weatherSummary.max())
					.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
			} else {
				throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
			}
		}

		List<Outfit> outfits = new ArrayList<>(tempStage.getRecommendations().stream()
			.map(Recommendation::getOutfit).toList());

		determineUmbrella(weatherSummary, outfits);
		determineLayered(tempStage, weatherSummary, outfits);

		return outfits.stream().map(RecommendResponse::of).toList();
	}

	private void determineUmbrella(WeatherSummaryResponse weatherSummary, List<Outfit> outfits) {
		if (weatherSummary.isHighPrecipitationProb()) {
			Outfit outfit;
			if (weatherSummary.isHeavyRainfall()) {
				outfit = outfitRepository.findByUmbrellaType(OutfitType.BASIC_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			} else {
				outfit = outfitRepository.findByUmbrellaType(OutfitType.FOLDING_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			}
			outfits.add(outfit);
		} else {
			if (weatherSummary.isHeavyRainfall()) {
				Outfit outfit = outfitRepository.findByUmbrellaType(OutfitType.FOLDING_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
				outfits.add(outfit);
			}
		}

	}

	private void determineLayered(TempStage tempStage, WeatherSummaryResponse weatherSummary, List<Outfit> outfits) {
		if (tempStage.getLevel() == 6 || tempStage.getLevel() == 7 || tempStage.getLevel() == 8) {
			return;
		}
		if (weatherSummary.max() - weatherSummary.min() >= 10) {
			Outfit outfit = outfitRepository.findByUmbrellaType(OutfitType.LAYERED)
				.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			outfits.add(outfit);
		}
	}
}
