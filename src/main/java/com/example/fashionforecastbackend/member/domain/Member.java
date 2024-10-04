package com.example.fashionforecastbackend.member.domain;

import static jakarta.persistence.EnumType.*;

import java.time.LocalDateTime;

import com.example.fashionforecastbackend.global.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor
@Getter
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String imageUrl;

	private String nickname;

	private String socialId;

	@Column(nullable = false)
	private LocalDateTime lastLoginDate;

	@Enumerated(value = STRING)
	private MemberRole role;

	@Enumerated(value = STRING)
	private MemberState state;

	@Enumerated(value = STRING)
	private MemberJoinType joinType;

	@Enumerated(value = STRING)
	private Gender gender;

	@Builder
	public Member(final Long id, final String email, final String imageUrl, final String nickname,
		final String socialId, final MemberRole role, final MemberJoinType joinType, final Gender gender) {

		this.id = id;
		this.email = email;
		this.imageUrl = imageUrl;
		this.nickname = nickname;
		this.socialId = socialId;
		this.lastLoginDate = LocalDateTime.now();
		this.state = MemberState.ACTIVE;
		this.role = role;
		this.joinType = joinType;
		this.gender = gender;

	}

	public void updateState(final MemberState state) {
		this.state = state;
	}

	public void updateGender(Gender gender) {
		this.gender = gender;
	}
}
