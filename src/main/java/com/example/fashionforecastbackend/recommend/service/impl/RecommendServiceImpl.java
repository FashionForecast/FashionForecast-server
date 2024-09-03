package com.example.fashionforecastbackend.recommend.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.example.fashionforecastbackend.recommend.domain.repository.RecommendationRepository;
import com.example.fashionforecastbackend.recommend.dto.RecommendRequest;
import com.example.fashionforecastbackend.recommend.dto.RecommendResponse;
import com.example.fashionforecastbackend.recommend.service.RecommendService;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.domain.repository.TempStageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecommendServiceImpl implements RecommendService {

	private final OutfitRepository outfitRepository;
	private final TempStageRepository tempStageRepository;
	private final RecommendationRepository recommendationRepository;

	@Override
	public List<RecommendResponse> getRecommendedOutfit(RecommendRequest recommendRequest) {
		TempStage tempStage;

		if (recommendRequest.extremumTmp() < 5 && (recommendRequest.tempCondition() == TempCondition.COOL
			|| recommendRequest.tempCondition() == TempCondition.WARM)) {
			throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
		}

		if (recommendRequest.extremumTmp() >= 28 && (recommendRequest.tempCondition() == TempCondition.WARM
			|| recommendRequest.tempCondition() == TempCondition.COOL)) {
			throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
		}

		if (recommendRequest.tempCondition() == TempCondition.WARM) {
			tempStage = tempStageRepository.findByWeatherAndWarmOption(recommendRequest.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		} else if (recommendRequest.tempCondition() == TempCondition.NORMAL) {
			tempStage = tempStageRepository.findByWeather(recommendRequest.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		} else if (recommendRequest.tempCondition() == TempCondition.COOL) {
			tempStage = tempStageRepository.findByWeatherAndCoolOption(recommendRequest.extremumTmp())
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		} else {
			throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
		}

		List<Outfit> outfits = new ArrayList<>(recommendationRepository.findByTempStage(tempStage.getId()).stream()
			.map(Recommendation::getOutfit).toList());

		determineUmbrella(recommendRequest, outfits);
		determineLayered(recommendRequest, outfits);

		Map<OutfitType, List<String>> groupByType = outfits.stream()
			.sorted(Comparator.comparing(outfit -> outfit.getOutfitType().ordinal()))
			.collect(Collectors.groupingBy(
				Outfit::getOutfitType,
				LinkedHashMap::new,
				Collectors.mapping(Outfit::getName, Collectors.toList())
			));

		return groupByType.entrySet().stream()
			.map(entry -> RecommendResponse.of(entry.getValue(), entry.getKey()))
			.toList();
	}

	private void determineUmbrella(RecommendRequest recommendRequest, List<Outfit> outfits) {
		if (recommendRequest.maximumPop() >= 30) {
			Outfit outfit;
			if (recommendRequest.maximumPcp() >= 3) {
				outfit = outfitRepository.findByUmbrellaType(OutfitType.BASIC_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			} else {
				outfit = outfitRepository.findByUmbrellaType(OutfitType.FOLDING_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			}
			outfits.add(outfit);
		} else {
			if (recommendRequest.maximumPcp() >= 3) {
				Outfit outfit = outfitRepository.findByUmbrellaType(OutfitType.FOLDING_UMBRELLA)
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
				outfits.add(outfit);
			}
		}

	}

	private void determineLayered(RecommendRequest recommendRequest, List<Outfit> outfits) {
		if (recommendRequest.maxMinTmpDiff() >= 10) {
			Outfit outfit = outfitRepository.findByUmbrellaType(OutfitType.LAYERED)
				.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			outfits.add(outfit);
		}
	}
}
