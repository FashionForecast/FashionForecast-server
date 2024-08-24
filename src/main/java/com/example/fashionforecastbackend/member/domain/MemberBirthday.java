package com.example.fashionforecastbackend.member.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MemberBirthday {

	private String years;
	private String months;
	private String days;
}
