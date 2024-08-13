package com.example.fashionforecastbackend.region.config;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.fashionforecastbackend.region.domain.Region;
import com.opencsv.CSVReader;

@ExtendWith(MockitoExtension.class)
class NationalForecastRegionReaderTest {

	@InjectMocks
	private NationalForecastRegionReader nationalForecastRegionReader;

	@Mock
	private CSVReader csvReader;

	@DisplayName("csv 파일을 정상적으로 읽어온다")
	@Test
	void read_정상_케이스() throws Exception {
		//given
		given(csvReader.readNext()).willReturn(new String[] {"code", "city", "district", "neighborhood", "nx", "ny"},
			new String[] {"001", "서울특별시", "강남구", "삼성동", "60", "127"},
			new String[] {"002", "부산광역시", "해운대구", "정동", "98", "76"},
			null);
		//when
		Set<Region> regions = nationalForecastRegionReader.read();

		//then
		assertThat(regions).hasSize(2);
	}

	@DisplayName("잘못된 형식의 라인은 무시되고 읽어오지 않는다")
	@Test
	void read_잘못된_형식의_라인_무시() throws Exception {

		//given
		given(csvReader.readNext()).willReturn(
			new String[] {"code", "city", "district", "neighborhood", "nx", "ny"},
			new String[] {"001", "서울특별시", "강남구", "삼성동", "60", "127"},
			new String[] {"002", "부산광역시"},
			null
		);
		//when
		Set<Region> regions = nationalForecastRegionReader.read();

		//then
		assertThat(regions).hasSize(1);
	}

	@DisplayName("csv파일을 읽어오는 중 예외가 발생한다.")
	@Test
	void read_예외_발생() throws Exception {
		//given
		given(csvReader.readNext()).willThrow(new RuntimeException());

		//when //then
		assertThatThrownBy(() -> nationalForecastRegionReader.read())
			.isInstanceOf(RuntimeException.class)
			.hasMessage("national_forecast_regions.csv을 읽어오는데 실패하였습니다.");
	}
}