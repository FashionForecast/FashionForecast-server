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

import com.example.fashionforecastbackend.global.error.exception.OutfitTypeNotFoundException;
import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.domain.OutfitType;
import com.example.fashionforecastbackend.outfit.domain.repository.OutfitRepository;
import com.example.fashionforecastbackend.recommend.domain.Recommendation;
import com.example.fashionforecastbackend.recommend.domain.repository.RecommendationRepository;
import com.example.fashionforecastbackend.recommend.dto.RecommendRequest;
import com.example.fashionforecastbackend.recommend.dto.RecommendResponse;
import com.example.fashionforecastbackend.recommend.service.RecommendService;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.service.TempStageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecommendServiceImpl implements RecommendService {

	private final OutfitRepository outfitRepository;
	private final TempStageService tempStageService;
	private final RecommendationRepository recommendationRepository;

	@Override
	public List<RecommendResponse> getRecommendedOutfit(RecommendRequest recommendRequest) {
		TempStage tempStage = tempStageService.getTempStageByWeather(recommendRequest.extremumTmp(),
			recommendRequest.tempCondition());

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
				outfit = outfitRepository.findByName("장우산")
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			} else {
				outfit = outfitRepository.findByName("접이식 우산")
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			}
			outfits.add(outfit);
		} else {
			if (recommendRequest.maximumPcp() >= 3) {
				Outfit outfit = outfitRepository.findByName("접이식 우산")
					.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
				outfits.add(outfit);
			}
		}

	}

	private void determineLayered(RecommendRequest recommendRequest, List<Outfit> outfits) {
		if (recommendRequest.maxMinTmpDiff() >= 10) {
			Outfit outfit = outfitRepository.findByName("겉옷")
				.orElseThrow(() -> new OutfitTypeNotFoundException(UMBRELLA_NOT_FOUND));
			outfits.add(outfit);
		}
	}
}
