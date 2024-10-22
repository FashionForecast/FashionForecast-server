package com.example.fashionforecastbackend.tempStage.service.impl;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fashionforecastbackend.global.error.exception.InvalidWeatherRequestException;
import com.example.fashionforecastbackend.global.error.exception.TempStageNotFoundException;
import com.example.fashionforecastbackend.recommend.domain.TempCondition;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;
import com.example.fashionforecastbackend.tempStage.domain.repository.TempStageRepository;
import com.example.fashionforecastbackend.tempStage.service.TempStageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TempStageServiceImpl implements TempStageService {

	private final TempStageRepository tempStageRepository;

	@Override
	public TempStage getTempStageByTemp(final int extremumTmp, final TempCondition tempCondition) {
		validateTempCondition(extremumTmp, tempCondition);

		if (tempCondition == TempCondition.WARM) {
			return tempStageRepository.findByWeatherAndWarmOption(extremumTmp)
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		}
		if (tempCondition == TempCondition.NORMAL) {
			return tempStageRepository.findByWeather(extremumTmp)
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		}
		if (tempCondition == TempCondition.COOL) {
			return tempStageRepository.findByWeatherAndCoolOption(extremumTmp)
				.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
		}
		throw new InvalidWeatherRequestException(INVALID_TEMP_CONDITION);
	}

	@Override
	public TempStage getTempStageByLevel(final int level) {
		return tempStageRepository.findByLevel(level)
			.orElseThrow(() -> new TempStageNotFoundException(TEMP_LEVEL_NOT_FOUND));
	}

	@Override
	public List<TempStage> findAllTempStage() {
		return tempStageRepository.findAll();
	}

	private void validateTempCondition(final int extremumTmp, final TempCondition tempCondition) {
		if (extremumTmp < 5 && tempCondition == TempCondition.WARM) {
			throw new InvalidWeatherRequestException(INVALID_GROUP8_WARM_OPTION);
		}

		if (extremumTmp >= 28 && tempCondition == TempCondition.COOL) {
			throw new InvalidWeatherRequestException(INVALID_GROUP1_COOL_OPTION);
		}
	}
}
