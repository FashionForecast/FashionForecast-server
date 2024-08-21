package com.example.fashionforecastbackend.weather.domain.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.fashionforecastbackend.weather.domain.Weather;
import com.example.fashionforecastbackend.weather.fixture.WeatherFixture;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeatherRepositoryTest {

	@Autowired
	private WeatherRepository weatherRepository;

	@DisplayName("4개의 레코드가 하나의 쿼리로 인서트")
	@Test
	void saveAllTest() throws Exception {
		//given
		Weather weather1 = WeatherFixture.createWeather("20240811", "1400", "20240811", "1500", 120, 67);
		Weather weather2 = WeatherFixture.createWeather("20240811", "1400", "20240811", "1600", 120, 67);
		Weather weather3 = WeatherFixture.createWeather("20240812", "1400", "20240812", "1500", 120, 67);
		Weather weather4 = WeatherFixture.createWeather("20240812", "1400", "20240812", "1600", 120, 67);
		//when
		weatherRepository.saveAll(List.of(weather1, weather2, weather3, weather4));
		List<Weather> records1 = weatherRepository.findWeather("20240811", "1400", 120, 67);
		List<Weather> records2 = weatherRepository.findWeather("20240812", "1400", 120, 67);
		//then
		assertThat(records1).isNotEmpty();
		assertThat(records2).isNotEmpty();
	}
	
	@DisplayName("baseDate = 20240812, baseTime = 1500 보다 과거인 날씨 데이터를 삭제한다.")
	@Test
	void deletePastWeathersTest() throws Exception {
	    //given
		Weather weather1 = WeatherFixture.createWeather("20240811", "1400", "20240811", "1500", 120, 67);
		Weather weather2 = WeatherFixture.createWeather("20240811", "1400", "20240811", "1600", 120, 67);
		Weather weather3 = WeatherFixture.createWeather("20240812", "1400", "20240812", "1500", 120, 67);
		Weather weather4 = WeatherFixture.createWeather("20240812", "1400", "20240812", "1600", 120, 67);
		weatherRepository.saveAll(List.of(weather1, weather2, weather3, weather4));
		//when
		weatherRepository.deletePastWeathers("20240812", "1500");

		//then
		List<Weather> pastWeathers1 = weatherRepository.findWeather("20240811", "1400", 120, 67);
		List<Weather> pastWeathers2 = weatherRepository.findWeather("20240812", "1400", 120, 67);
		assertAll(
			() -> assertThat(pastWeathers1).isEmpty(),
			() -> assertThat(pastWeathers2).isEmpty()
		);

	}

}