package com.example.fashionforecastbackend.outfit.fixture;

import static com.example.fashionforecastbackend.outfit.domain.OutfitGender.*;
import static com.example.fashionforecastbackend.outfit.domain.OutfitType.*;

import java.util.List;

import com.example.fashionforecastbackend.outfit.domain.Outfit;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitGroupResponse;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitResponse;

public class OutfitFixture {

	public static final List<Outfit> OUTFITS = List.of(
		new Outfit("반팔티", TOP, UNISEX),
		new Outfit("긴팔티", TOP, UNISEX),
		new Outfit("셔츠", TOP, UNISEX),
		new Outfit("반바지", BOTTOM, UNISEX),
		new Outfit("긴바지", BOTTOM, UNISEX),
		new Outfit("레깅스", BOTTOM, FEMALE),
		new Outfit("스타킹", BOTTOM, FEMALE)
	);

	public static final List<OutfitResponse> MALE_TOPS = List.of(
		OutfitResponse.of(new Outfit("반팔티", TOP, UNISEX)),
		OutfitResponse.of(new Outfit("긴팔티", TOP, UNISEX)),
		OutfitResponse.of(new Outfit("셔츠", TOP, UNISEX))
	);

	public static final List<OutfitResponse> MALE_BOTTOMS = List.of(
		OutfitResponse.of(new Outfit("반바지", BOTTOM, UNISEX)),
		OutfitResponse.of(new Outfit("긴바지", BOTTOM, UNISEX))
	);

	public static final List<OutfitResponse> FEMALE_TOPS = List.of(
		OutfitResponse.of(new Outfit("반팔티", TOP, UNISEX)),
		OutfitResponse.of(new Outfit("긴팔티", TOP, UNISEX)),
		OutfitResponse.of(new Outfit("셔츠", TOP, UNISEX))
	);

	public static final List<OutfitResponse> FEMALE_BOTTOMS = List.of(
		OutfitResponse.of(new Outfit("반바지", BOTTOM, UNISEX)),
		OutfitResponse.of(new Outfit("긴바지", BOTTOM, UNISEX)),
		OutfitResponse.of(new Outfit("레깅스", BOTTOM, FEMALE)),
		OutfitResponse.of(new Outfit("스타킹", BOTTOM, FEMALE))
	);

	public static final OutfitGroupResponse OUTFIT_GROUP_RESPONSE = OutfitGroupResponse.of(MALE_TOPS, MALE_BOTTOMS);
}
