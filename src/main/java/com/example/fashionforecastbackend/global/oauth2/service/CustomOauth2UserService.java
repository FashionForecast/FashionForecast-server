package com.example.fashionforecastbackend.global.oauth2.service;

import java.util.Collections;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.fashionforecastbackend.global.oauth2.CustomOauth2User;
import com.example.fashionforecastbackend.global.oauth2.Oauth2Attributes;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.constant.MemberJoinType;
import com.example.fashionforecastbackend.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private static final String GOOGLE = "google";

	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		final String registrationId = userRequest.getClientRegistration().getRegistrationId();
		log.info("loadUser() 실행 : {}", registrationId);
		final MemberJoinType joinType = getMemberType(registrationId);
		final String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		final Map<String, Object> attributes = oAuth2User.getAttributes();

		final Oauth2Attributes extractAttributes = Oauth2Attributes.of(joinType, userNameAttributeName, attributes);
		final boolean isNewUser = isNewMember(extractAttributes, joinType);
		final Member member = getMember(extractAttributes, joinType);
		final boolean canAddGuestOutfit = canAddGuestOutfit(member);

		return new CustomOauth2User(
			Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())), // 단일 요소를 갖는 권한 목록(컬렉션)을 불변객체로
			attributes,
			extractAttributes.getUserNameAttributeKey(),
			member.getId(),
			member.getEmail(),
			member.getRole().getKey(),
			isNewUser, canAddGuestOutfit);
	}

	private MemberJoinType getMemberType(String registrationId) {
		if (GOOGLE.equals(registrationId)) {
			return MemberJoinType.GOOGLE;
		}
		return MemberJoinType.KAKAO;
	}

	private boolean isNewMember(Oauth2Attributes attributes, MemberJoinType joinType) {
		return !memberRepository.existsByJoinTypeAndSocialId(joinType, attributes.getOauth2UserInfo().getId())
			|| memberRepository.findByJoinTypeAndSocialId(joinType, attributes.getOauth2UserInfo().getId())
			.orElseGet(() -> savedMember(attributes, joinType))
			.getGender() == null;
	}

	private Member getMember(Oauth2Attributes attributes, MemberJoinType joinType) {
		return memberRepository.findByJoinTypeAndSocialId(joinType,
			attributes.getOauth2UserInfo().getId()).orElseGet(() -> savedMember(attributes, joinType));
	}

	private boolean canAddGuestOutfit(final Member member) {
		return member.notExistOutfit();
	}

	private Member savedMember(Oauth2Attributes attributes, MemberJoinType joinType) {
		final Member member = attributes.toEntity(joinType, attributes.getOauth2UserInfo());
		return memberRepository.save(member);
	}
}
