package com.example.fashionforecastbackend.member.fixture;

import java.util.List;

import com.example.fashionforecastbackend.member.domain.MemberOutfit;
import com.example.fashionforecastbackend.member.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.member.domain.constant.TopAttribute;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.member.dto.response.MemberOutfitResponse;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;

public class MemberOutfitFixture {

	public static final MemberOutfitRequest MEMBER_OUTFIT_REQUEST = new MemberOutfitRequest(
		"긴팔티",
		"#333333",
		"슬랙스",
		"#444444",
		2
	);

	public static final List<MemberOutfit> MEMBER_OUTFITS = List.of(
		MemberOutfit.builder()
			.tempStage(TempStage.create(3, 20, 22))
			.topAttribute(TopAttribute.of("반팔티", "#111111"))
			.bottomAttribute(BottomAttribute.of("반바지", "#222222"))
			.build(),
		MemberOutfit.builder()
			.tempStage(TempStage.create(4, 17, 19))
			.topAttribute(TopAttribute.of("긴팔티", "#111111"))
			.bottomAttribute(BottomAttribute.of("긴바지", "#222222"))
			.build()
	);

	public static final List<MemberOutfit> MEMBER_TEMP_STAGE_OUTFITS = List.of(
		MemberOutfit.builder()
			.tempStage(TempStage.create(3, 20, 22))
			.topAttribute(TopAttribute.of("반팔티", "#111111"))
			.bottomAttribute(BottomAttribute.of("반바지", "#222222"))
			.build(),
		MemberOutfit.builder()
			.tempStage(TempStage.create(3, 20, 22))
			.topAttribute(TopAttribute.of("긴팔티", "#111111"))
			.bottomAttribute(BottomAttribute.of("긴바지", "#222222"))
			.build()
	);

	public static final List<MemberOutfitResponse> MEMBER_TEMP_STAGE_OUTFITS_RESPONSE = List.of(
		MemberOutfitResponse.of(
			MemberOutfit.builder()
				.id(6L)
				.tempStage(TempStage.create(3, 20, 22))
				.topAttribute(TopAttribute.of("반팔티", "#111111"))
				.bottomAttribute(BottomAttribute.of("반바지", "#222222"))
				.build()),
		MemberOutfitResponse.of(
			MemberOutfit.builder()
				.id(2L)
				.tempStage(TempStage.create(3, 20, 22))
				.topAttribute(TopAttribute.of("긴팔티", "#111111"))
				.bottomAttribute(BottomAttribute.of("긴바지", "#222222"))
				.build()
		));

	public static final List<MemberOutfitGroupResponse> MEMBER_OUTFITS_GROUPS = List.of(
		MemberOutfitGroupResponse.of(1, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(3L)
					.tempStage(TempStage.create(1, 28, 50))
					.topAttribute(TopAttribute.of("반팔티", "#111111"))
					.bottomAttribute(BottomAttribute.of("반바지", "#222222"))
					.build()
			)
		)),
		MemberOutfitGroupResponse.of(2, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(4L)
					.tempStage(TempStage.create(2, 23, 27))
					.topAttribute(TopAttribute.of("긴팔티", "#333333"))
					.bottomAttribute(BottomAttribute.of("청바지", "#444444"))
					.build()
			)
		)),
		MemberOutfitGroupResponse.of(3, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(2L)
					.tempStage(TempStage.create(3, 20, 22))
					.topAttribute(TopAttribute.of("셔츠", "#555555"))
					.bottomAttribute(BottomAttribute.of("면바지", "#666666"))
					.build()
			)
		)),
		MemberOutfitGroupResponse.of(4, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(1L)
					.tempStage(TempStage.create(4, 17, 19))
					.topAttribute(TopAttribute.of("니트", "#777777"))
					.bottomAttribute(BottomAttribute.of("청바지", "#888888"))
					.build()
			)
		)),
		MemberOutfitGroupResponse.of(5, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(9L)
					.tempStage(TempStage.create(5, 12, 16))
					.topAttribute(TopAttribute.of("후드티", "#999999"))
					.bottomAttribute(BottomAttribute.of("면바지", "#AAAAAA"))
					.build()
			)
		)),
		MemberOutfitGroupResponse.of(6, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(8L)
					.tempStage(TempStage.create(6, 9, 11))
					.topAttribute(TopAttribute.of("가디건", "#BBBBBB"))
					.bottomAttribute(BottomAttribute.of("청바지", "#CCCCCC"))
					.build()
			)
		)),
		MemberOutfitGroupResponse.of(7, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(6L)
					.tempStage(TempStage.create(7, 5, 8))
					.topAttribute(TopAttribute.of("패딩", "#DDDDDD"))
					.bottomAttribute(BottomAttribute.of("청바지", "#EEEEEE"))
					.build()
			)
		)),
		MemberOutfitGroupResponse.of(8, List.of(
			MemberOutfitResponse.of(
				MemberOutfit.builder()
					.id(10L)
					.tempStage(TempStage.create(8, -50, 4))
					.topAttribute(TopAttribute.of("패딩", "#FFFFFF"))
					.bottomAttribute(BottomAttribute.of("기모바지", "#000000"))
					.build()
			)
		))
	);

}
