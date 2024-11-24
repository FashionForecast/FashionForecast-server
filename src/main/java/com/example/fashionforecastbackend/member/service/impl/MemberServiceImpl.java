package com.example.fashionforecastbackend.member.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.InvalidOutingTimeException;
import com.example.fashionforecastbackend.global.error.exception.MemberNotFoundException;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.OutingTimeRequest;
import com.example.fashionforecastbackend.member.dto.request.RegionRequest;
import com.example.fashionforecastbackend.member.dto.request.TempConditionRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public MemberInfoResponse getMemberInfo(final Long memberId) {
		Member member = getById(memberId);
		return MemberInfoResponse.of(member);
	}

	@Transactional
	@Override
	public void saveGender(final MemberGenderRequest memberGenderRequest, final Long memberId) {
		Member member = getById(memberId);
		member.updateGender(memberGenderRequest.gender());
	}

	@Transactional
	@Override
	public void updateRegion(final RegionRequest request, final Long memberId) {
		final Member member = getById(memberId);

		member.getPersonalSetting().updateRegion(request.region());
	}

	@Transactional
	@Override
	public void updateOutingTime(final OutingTimeRequest request, final Long memberId) {
		final Member member = getById(memberId);

		String start = request.startTime();
		String end = request.endTime();
		if (!start.equals("DEFAULT") && !end.equals("DEFAULT")) {
			validateOutingTime(start, end);
		}

		member.getPersonalSetting().updateOutingTime(start, end);
	}

	@Transactional
	@Override
	public void updateTempStage(final TempConditionRequest request, final Long memberId) {
		final Member member = getById(memberId);
		member.getPersonalSetting().updateTempCondition(request.tempCondition());

	}

	private Member getById(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
	}

	private void validateOutingTime(String start, String end) {
		LocalTime startTime = parseToLocalTime(start);
		LocalTime endTime = parseToLocalTime(end);
		if (startTime.isAfter(endTime)) {
			throw new InvalidOutingTimeException(INVALID_OUTING_TIME);
		}
	}

	private LocalTime parseToLocalTime(String timeString) {
		timeString = timeString.replace("오전", "AM")
			.replace("오후", "PM")
			.replace("시", "")
			.trim();

		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.appendPattern("a hh")
			.toFormatter(Locale.ENGLISH);

		return LocalTime.parse(timeString, formatter);
	}
}
