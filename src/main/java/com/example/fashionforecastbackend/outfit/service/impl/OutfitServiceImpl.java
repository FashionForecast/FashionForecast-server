package com.example.fashionforecastbackend.outfit.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;
import static com.example.fashionforecastbackend.outfit.domain.OutfitGender.*;
import static com.example.fashionforecastbackend.outfit.domain.OutfitType.*;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.global.error.exception.TempStageNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.constant.Gender;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.domain.repository.OutfitRepository;
import com.example.fashionforecastbackend.outfit.dto.request.OutfitRequest;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitGroupResponse;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitResponse;
import com.example.fashionforecastbackend.outfit.service.OutfitService;
import com.example.fashionforecastbackend.recommend.domain.Recommendation;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.domain.repository.TempStageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OutfitServiceImpl implements OutfitService {

    private final OutfitRepository outfitRepository;
    private final TempStageRepository tempStageRepository;
    private final MemberRepository memberRepository;

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

    @Override
    public OutfitGroupResponse getOutfitGroup(final Long memberId) {
        final Member member = getById(memberId);
        final Gender gender = member.getGender();
        List<Outfit> outfits = outfitRepository.findByOutfitTypeIn(List.of(TOP, BOTTOM));

        if (gender.equals(Gender.MALE)) {
            outfits = outfits.stream()
                .filter(outfit -> Objects.equals(outfit.getOutfitGender(), UNISEX))
                .toList();
        }

        final List<OutfitResponse> tops = getTops(outfits);
        final List<OutfitResponse> bottoms = getBottoms(outfits);

        return OutfitGroupResponse.of(tops, bottoms);
    }

    private List<OutfitResponse> getTops(final List<Outfit> outfits) {
        return outfits.stream()
            .filter(outfit -> outfit.getOutfitType() == TOP)
            .map(OutfitResponse::of)
            .toList();
    }

    private List<OutfitResponse> getBottoms(final List<Outfit> outfits) {
        return outfits.stream()
            .filter(outfit -> outfit.getOutfitType() == BOTTOM)
            .map(OutfitResponse::of)
            .toList();
    }

    private Member getById(final Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
    }

}
