package com.example.fashionforecastbackend.outfit.dto;

import java.util.List;

import com.example.fashionforecastbackend.outfit.domain.OutfitType;

import jakarta.validation.constraints.NotNull;

public record OutfitRequest(
	@NotNull(message = "옷 이름을 입력해주세요.")
	String name,
	@NotNull(message = "옷 카테고리를 입력해주세요.")
	OutfitType outfitType,
	@NotNull(message = "온도 단계를 입력해주세요. 복수 입력 가능 예) [1,2]")
	List<Integer> tempLevels
) {
}
