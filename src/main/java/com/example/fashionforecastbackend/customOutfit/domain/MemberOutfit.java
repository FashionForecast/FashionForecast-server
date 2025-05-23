package com.example.fashionforecastbackend.customOutfit.domain;

import org.hibernate.annotations.SQLDelete;

import com.example.fashionforecastbackend.customOutfit.domain.constant.BottomAttribute;
import com.example.fashionforecastbackend.customOutfit.domain.constant.TopAttribute;
import com.example.fashionforecastbackend.global.BaseTimeEntity;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE member_outfit SET is_deleted = true WHERE id = ?")
public class MemberOutfit extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private TopAttribute topAttribute;

	@Embedded
	private BottomAttribute bottomAttribute;

	private boolean isDeleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "temp_stage_id", nullable = false)
	private TempStage tempStage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Builder
	public MemberOutfit(final Long id, final TopAttribute topAttribute, final BottomAttribute bottomAttribute, final TempStage tempStage) {
		this.id = id;
		this.topAttribute = topAttribute;
		this.bottomAttribute = bottomAttribute;
		this.tempStage = tempStage;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	public void updateTopAttribute(final TopAttribute topAttribute) {
		this.topAttribute = topAttribute;
	}

	public void updateBottomAttribute(final BottomAttribute bottomAttribute) {
		this.bottomAttribute = bottomAttribute;
	}

}
