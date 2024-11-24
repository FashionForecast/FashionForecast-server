package com.example.fashionforecastbackend.outfit.service;

import com.example.fashionforecastbackend.outfit.dto.request.OutfitRequest;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitGroupResponse;

public interface OutfitService {
	void saveOutfit(OutfitRequest requestDto);
	OutfitGroupResponse getOutfitGroup(final Long memberId);
}
