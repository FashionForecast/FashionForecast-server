package com.example.fashionforecastbackend.customOutfit.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.customOutfit.domain.GuestOutfit;
import com.example.fashionforecastbackend.customOutfit.dto.request.DeleteGuestOutfitRequest;
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
		validateCountZero(guestOutfitRequest.tempStageLevel(), guest.getId());

		final String key = KEY_PREFIX + guest.getId();
		final GuestOutfit guestOutfit = createGuestOutfit(
			guestOutfitRequest);

		redisTemplate.opsForList().leftPush(key, guestOutfit);
	}

	@Override
	public List<GuestOutfitResponse> getGuestOutfitsByUuid(final String uuid) {
		final Guest guest = guestService.getGuestByUuid(uuid);
		final List<GuestOutfit> guestOutfits = getGuestOutfitsByGuest(guest.getId());

		Map<Integer, GuestOutfit> uniqueOutfits = new HashMap<>();
		for (GuestOutfit outfit : guestOutfits) {
			uniqueOutfits.putIfAbsent(outfit.getTempStageLevel(), outfit);
		}

		return uniqueOutfits.values().stream()
			.map(GuestOutfitResponse::of)
			.toList();
	}

	@Override
	public GuestOutfitResponse getGuestTempStageOutfit(final GuestTempStageOutfitRequest request) {
		final Guest guest = guestService.getGuestByUuid(request.uuid());
		final TempStage tempStage = tempStageService.getTempStageByWeather(request.extremumTmp(),
			request.tempCondition());

		return getGuestOutfitsByGuest(guest.getId()).stream()
			.filter(outfit -> outfit.getTempStageLevel() == tempStage.getLevel())
			.findFirst()
			.map(GuestOutfitResponse::of)
			.orElse(null);
	}

	@Transactional
	@Override
	public void deleteGuestOutfit(final DeleteGuestOutfitRequest request) {
		final Guest guest = guestService.getGuestByUuid(request.uuid());
		final String key = KEY_PREFIX + guest.getId();

		validateGuestOutfitByTempStageLevel(request.tempStageLevel(), guest.getId());
		final GuestOutfit guestOutfit = getGuestOutfit(request.tempStageLevel(), guest.getId());
		redisTemplate.opsForList().remove(key, 1, guestOutfit);
	}

	@Transactional
	@Override
	public void updateGuestOutfit(final GuestOutfitRequest request) {
		final Guest guest = guestService.getGuestByUuid(request.uuid());
		final String key = KEY_PREFIX + guest.getId();

		validateGuestOutfitByTempStageLevel(request.tempStageLevel(), guest.getId());
		final List<GuestOutfit> guestOutfits = getGuestOutfitsByGuest(guest.getId());

		final GuestOutfit updatedOutfit = createGuestOutfit(request);

		for (int i = 0; i < guestOutfits.size(); i++) {
			GuestOutfit outfit = guestOutfits.get(i);
			if (outfit.getTempStageLevel() == request.tempStageLevel()) {
				redisTemplate.opsForList().set(key, i, updatedOutfit);
				return;
			}
		}
	}

	@Transactional
	@Override
	public void deleteAllGuestOutfit(final String uuid) {
		final Guest guest = guestService.getGuestByUuid(uuid);
		final String key = KEY_PREFIX + guest.getId();
		redisTemplate.delete(key);
	}

	private void validateCountZero(final int tempStageLevel, final Long guestId) {
		final List<GuestOutfit> guestOutfits = getGuestOutfitsByGuest(guestId);
		if (guestOutfits != null && guestOutfits.stream()
			.anyMatch(guestOutfit -> guestOutfit.getTempStageLevel() == tempStageLevel)) {
			throw new GuestOutfitException(ALREADY_EXIST_GUEST_OUTFIT);
		}
	}

	private GuestOutfit createGuestOutfit(final GuestOutfitRequest request) {
		return GuestOutfit.builder()
			.tempStageLevel(request.tempStageLevel())
			.topType(request.topType())
			.topColor(request.topColor())
			.bottomType(request.bottomType())
			.bottomColor(request.bottomColor())
			.build();
	}

	private void validateGuestOutfitByTempStageLevel(final int tempStageLevel, final Long guestId) {
		final GuestOutfit guestOutfit = getGuestOutfit(tempStageLevel, guestId);

		if (guestOutfit == null) {
			throw new GuestOutfitException(NOT_FOUND_GUEST_OUTFIT);
		}
	}

	private GuestOutfit getGuestOutfit(final int tempStageLevel, final Long guestId) {
		final List<GuestOutfit> guestOutfits = getGuestOutfitsByGuest(guestId);
		return guestOutfits.stream()
			.filter(outfit -> outfit.getTempStageLevel() == tempStageLevel)
			.findFirst()
			.orElse(null);
	}

	private List<GuestOutfit> getGuestOutfitsByGuest(final Long guestId) {
		String key = KEY_PREFIX + guestId;
		return redisTemplate.opsForList().range(key, 0, -1);
	}

}
