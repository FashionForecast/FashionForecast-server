package com.example.fashionforecastbackend.customOutfit.service;

import java.util.List;

import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitGroupResponse;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitResponse;

public interface GuestOutfitService {
	void saveGuestOutfit(final GuestOutfitRequest request);

	List<GuestOutfitGroupResponse> getGuestOutfits(final String uuid);

	List<GuestOutfitResponse> getGuestTempStageOutfits(final GuestTempStageOutfitRequest request, final String uuid);

	void deleteGuestOutfit(final String uuid);

	void updateGuestOutfit(final GuestOutfitRequest request);
}
