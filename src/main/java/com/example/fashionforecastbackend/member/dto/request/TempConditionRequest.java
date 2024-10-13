package com.example.fashionforecastbackend.member.dto.request;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

public record TempConditionRequest(
	TempCondition tempCondition
) {
}
