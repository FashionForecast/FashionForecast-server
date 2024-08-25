package com.example.fashionforecastbackend.member.domain;

import static jakarta.persistence.EnumType.*;

import java.time.LocalDateTime;

import com.example.fashionforecastbackend.global.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

	private String password;

	@Column(nullable = false, unique = true, length = 20)
	private String nickname;

	@Column(nullable = false)
	private LocalDateTime lastLoginDate;

	@Embedded
	private MemberBirthday birthday;

	@Enumerated(value = STRING)
	private MemberGender gender;

	@Enumerated(value = STRING)
	private MemberRole role;

	@Enumerated(value = STRING)
	private MemberState state;

	@Enumerated(value = STRING)
	private MemberJoinType joinType;

	@Builder
	public Member(final Long id, final String email, final String password, final String nickname,
		final MemberBirthday birthday ,final MemberGender gender, final MemberRole role, final MemberJoinType joinType) {

		this.id = id;
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.lastLoginDate = LocalDateTime.now();
		this.birthday = birthday;
		this.gender = gender;
		this.state = MemberState.ACTIVE;
		this.role = role;
		this.joinType = joinType;

	}

	public void updateState(final MemberState state) {
		this.state = state;
	}

	public void updateNickname(final String nickname) {
		this.nickname = nickname;
	}

}
