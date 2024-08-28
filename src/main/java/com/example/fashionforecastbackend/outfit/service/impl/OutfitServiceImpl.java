package com.example.fashionforecastbackend.outfit.service.impl;

import com.example.fashionforecastbackend.global.error.exception.TempStageNotFoundException;
import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.domain.repository.OutfitRepository;
import com.example.fashionforecastbackend.outfit.dto.OutfitRequest;
import com.example.fashionforecastbackend.outfit.service.OutfitService;
import com.example.fashionforecastbackend.recommend.domain.Recommendation;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.domain.repository.TempStageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.fashionforecastbackend.global.error.ErrorCode.TEMP_LEVEL_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutfitServiceImpl implements OutfitService {

    private final OutfitRepository outfitRepository;
    private final TempStageRepository tempStageRepository;

    @Transactional
    @Override
    public void saveOutfit(OutfitRequest requestDto) {
        List<TempStage> tempStages = requestDto.tempLevels().stream()
                .map(level -> tempStageRepository.findByLevel(level)
                        .orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND)))
                .toList();

        List<Recommendation> list = tempStages.stream()
                .map(Recommendation::createRecommendation)
                .toList();

        Outfit outfit = Outfit.createOutfit(requestDto.name(), requestDto.outfitType(), list);

        outfitRepository.save(outfit);
    }
}
