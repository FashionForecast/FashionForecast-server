package com.example.fashionforecastbackend.member.domain;

import com.example.fashionforecastbackend.global.BaseTimeEntity;
import com.example.fashionforecastbackend.member.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.member.domain.constant.TopAttribute;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberOutfit extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private TopAttribute topAttribute;

	@Embedded
	private BottomAttribute bottomAttribute;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "temp_stage_id", nullable = false)
	private TempStage tempStage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Builder
	public MemberOutfit(final TopAttribute topAttribute, final BottomAttribute bottomAttribute, final TempStage tempStage) {
		this.topAttribute = topAttribute;
		this.bottomAttribute = bottomAttribute;
		this.tempStage = tempStage;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

}
