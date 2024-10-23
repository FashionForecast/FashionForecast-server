package com.example.fashionforecastbackend.tempStage.service;

import java.util.List;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;
import com.example.fashionforecastbackend.tempStage.domain.TempStage;

public interface TempStageService {

	TempStage getTempStageByWeather(int extremumTmp, final TempCondition tempCondition);

	TempStage getTempStageByLevel(int level);

	List<TempStage> findAllTempStage();
}
