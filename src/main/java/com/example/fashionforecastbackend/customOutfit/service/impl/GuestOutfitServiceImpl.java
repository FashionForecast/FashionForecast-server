package com.example.fashionforecastbackend.customOutfit.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.customOutfit.domain.GuestOutfit;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitResponse;
import com.example.fashionforecastbackend.customOutfit.service.GuestOutfitService;
import com.example.fashionforecastbackend.global.error.exception.GuestOutfitException;
import com.example.fashionforecastbackend.guest.domain.Guest;
import com.example.fashionforecastbackend.guest.service.GuestService;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.service.TempStageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestOutfitServiceImpl implements GuestOutfitService {

	private static final String KEY_PREFIX = "Outfit";
	private final RedisTemplate<String, GuestOutfit> redisTemplate;
	private final GuestService guestService;
	private final TempStageService tempStageService;

	@Transactional
	@Override
	public void saveGuestOutfit(GuestOutfitRequest guestOutfitRequest) {
		final Guest guest = guestService.getGuestByUuid(guestOutfitRequest.uuid());
		validateCount(guestOutfitRequest.tempStageLevel(), guest.getId());

		final String key = KEY_PREFIX + guest.getId();
		final GuestOutfit guestOutfit = GuestOutfit.builder()
			.tempStageLevel(guestOutfitRequest.tempStageLevel())
			.topType(guestOutfitRequest.topType())
			.topColor(guestOutfitRequest.topColor())
			.bottomType(guestOutfitRequest.bottomType())
			.bottomColor(guestOutfitRequest.bottomColor())
			.build();

		redisTemplate.opsForList().leftPush(key, guestOutfit);
	}

	@Override
	public List<GuestOutfitResponse> getGuestOutfitsByUuid(final String uuid) {
		final Guest guest = guestService.getGuestByUuid(uuid);
		final List<GuestOutfit> guestOutfits = getGuestOutfitsByGuest(guest.getId());
		return guestOutfits.stream()
			.map(GuestOutfitResponse::of)
			.toList();
	}

	@Override
	public GuestOutfitResponse getGuestTempStageOutfits(final GuestTempStageOutfitRequest request, final String uuid) {
		final Guest guest = guestService.getGuestByUuid(uuid);
		final TempStage tempStage = tempStageService.getTempStageByWeather(request.extremumTmp(),
			request.tempCondition());

		return getGuestOutfitsByGuest(guest.getId()).stream()
			.filter(outfit -> outfit.getTempStageLevel() == tempStage.getLevel())
			.findFirst()
			.map(GuestOutfitResponse::of)
			.orElse(null);
	}

	private void validateCount(final int tempStageLevel, final Long guestId) {
		final List<GuestOutfit> guestOutfits = getGuestOutfitsByGuest(guestId);
		if (guestOutfits != null && guestOutfits.stream()
			.anyMatch(guestOutfit -> guestOutfit.getTempStageLevel() == tempStageLevel)) {
			throw new GuestOutfitException(OUTFIT_ALREADY_EXIST);
		}
	}

	private List<GuestOutfit> getGuestOutfitsByGuest(final Long guestId) {
		String key = KEY_PREFIX + guestId;
		return redisTemplate.opsForList().range(key, 0, -1);
	}

	@Transactional
	@Override
	public void deleteGuestOutfit(String uuid) {

	}

	@Transactional
	@Override
	public void updateGuestOutfit(GuestOutfitRequest request) {

	}
}
