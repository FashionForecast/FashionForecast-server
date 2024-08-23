package com.example.fashionforecastbackend.guest.domain;

import static jakarta.persistence.EnumType.*;

import com.example.fashionforecastbackend.global.BaseTimeEntity;

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
public class Guest extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String uuid;

	@Enumerated(value = STRING)
	private GuestState state;


	@Builder
	public Guest(final Long id, final String uuid) {
		this.id = id;
		this.uuid = uuid;
		this.state = GuestState.ACTIVE;
	}


}
