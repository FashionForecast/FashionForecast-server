package com.example.fashionforecastbackend.customOutfit.service;

import java.util.List;

import com.example.fashionforecastbackend.customOutfit.dto.request.DeleteGuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitResponse;

public interface GuestOutfitService {
	void saveGuestOutfit(final GuestOutfitRequest request);

	List<GuestOutfitResponse> getGuestOutfitsByUuid(final String uuid);

	GuestOutfitResponse getGuestTempStageOutfits(final GuestTempStageOutfitRequest request, final String uuid);

	void deleteGuestOutfit(final DeleteGuestOutfitRequest request);

	void updateGuestOutfit(final GuestOutfitRequest request);
}
