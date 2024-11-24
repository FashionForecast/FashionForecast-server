package com.example.fashionforecastbackend.tempStage.fixture;

import java.util.List;

import com.example.fashionforecastbackend.tempStage.domain.TempStage;

public class TempStageFixture {

	public static List<TempStage> TEMP_STAGE_ALL = List.of(
		TempStage.create(1, 28, 50),
		TempStage.create(2, 23, 27),
		TempStage.create(3, 20, 22),
		TempStage.create(4, 17, 19),
		TempStage.create(5, 12, 16),
		TempStage.create(6, 9, 11),
		TempStage.create(7, 5, 8),
		TempStage.create(8, -50, 4)
	);
}
