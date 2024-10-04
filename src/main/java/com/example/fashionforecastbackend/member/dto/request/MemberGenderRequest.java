package com.example.fashionforecastbackend.member.dto.request;

import com.example.fashionforecastbackend.member.domain.Gender;

public record MemberGenderRequest(
	Gender gender
) {
}
