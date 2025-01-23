package com.example.fashionforecastbackend.customOutfit.fixture;

import java.util.List;

import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitResponse;

public class GuestOutfitFixture {

	public static final GuestOutfitRequest GUEST_OUTFIT_REQUEST = new GuestOutfitRequest(
		"guest123",
		"긴팔티",
		"#111111",
		"면바지",
		"#000000",
		2);

	public static final List<GuestOutfitResponse> GUEST_OUTFITS_RESPONSE = List.of(
		new GuestOutfitResponse(
			2,
			"긴팔티",
			"#111111",
			"면바지",
			"#000000"
		),
		new GuestOutfitResponse(
			3,
			"후드티",
			"#111111",
			"청바지",
			"#000000"
		)
	);

	public static final GuestOutfitResponse GUEST_OUTFIT_RESPONSE = new GuestOutfitResponse(
		2,
		"긴팔티",
		"#111111",
		"면바지",
		"#000000");
}
