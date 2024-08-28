package com.example.fashionforecastbackend.weather.presentation;

import static com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.fashionforecastbackend.global.ControllerTest;
import com.example.fashionforecastbackend.weather.dto.request.WeatherRequest;
import com.example.fashionforecastbackend.weather.dto.response.WeatherResponse;
import com.example.fashionforecastbackend.weather.fixture.WeatherFixture;
import com.example.fashionforecastbackend.weather.service.WeatherService;

@WebMvcTest(WeatherController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WithMockUser
class WeatherControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WeatherService weatherService;

	@DisplayName("날씨 조회 api 호출이 성공 한다.")
	@Test
	void getWeatherTest() throws Exception {

		final WeatherResponse response = WeatherFixture.WEATHER_RESPONSE;
		given(weatherService.getWeather(any(WeatherRequest.class))).willReturn(response);
		mockMvc.perform(get("/api/v1/weather/forecast")
				.param("nowDateTime", "2024-08-11T14:21:11")
				.param("startDateTime", "2024-08-11T16:00:00")
				.param("endDateTime", "2024-08-11T21:00:00")
				.param("nx", "60")
				.param("ny", "127")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("nowDateTime")
						.attributes(field("format", "yyyy-mm-ddTHH:mm:ss"))
						.description("현재 시간"),
					parameterWithName("startDateTime")
						.attributes(field("format", "yyyy-mm-ddTHH:mm:ss"))
						.description("외출 시작 시간"),
					parameterWithName("endDateTime")
						.attributes(field("format", "yyyy-mm-ddTHH:mm:ss"))
						.description("외출 끝 시간"),
					parameterWithName("nx").attributes(field("format", "1~999 사이 정수값")).description("위도"),
					parameterWithName("ny").attributes(field("format", "1~999 사이 정수값")).description("경도")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("날씨 데이터")
				)
					.andWithPrefix("data.",
						fieldWithPath("season").type(JsonFieldType.STRING).description("계절"),
						fieldWithPath("extremumTmp").type(JsonFieldType.NUMBER).description("최고 또는 최저 기온"),
						fieldWithPath("maxMinTmpDiff").type(JsonFieldType.NUMBER).description("최고 최저 기온차"),
						fieldWithPath("maximumPop").type(JsonFieldType.NUMBER).description("최대 강수확률"),
						fieldWithPath("maximumPcp").type(JsonFieldType.NUMBER).description("최대 강수량"),
						fieldWithPath("forecasts").type(JsonFieldType.ARRAY).description("날씨 예보 목록")
					)
					.andWithPrefix("data.forecasts[].",
						fieldWithPath("fcstDate").type(JsonFieldType.STRING).description("예보 날짜"),
						fieldWithPath("fcstTime").type(JsonFieldType.STRING).description("예보 시간"),
						fieldWithPath("tmp").type(JsonFieldType.STRING).description("온도"),
						fieldWithPath("reh").type(JsonFieldType.STRING).description("습도"),
						fieldWithPath("wsd").type(JsonFieldType.STRING).description("풍속"),
						fieldWithPath("pop").type(JsonFieldType.STRING).description("강수확률"),
						fieldWithPath("pcp").type(JsonFieldType.STRING).description("강수량"),
						fieldWithPath("rainType").type(JsonFieldType.STRING).description("강수 유형"),
						fieldWithPath("skyStatus").type(JsonFieldType.STRING).description("하늘 상태"),
						fieldWithPath("nx").type(JsonFieldType.NUMBER).description("위도"),
						fieldWithPath("ny").type(JsonFieldType.NUMBER).description("경도")
					)
			));
	}

	@DisplayName("잘못된 날씨 요청값으로 에러가 발생한다.")
	@Test
	void getWeatherErrorTest() throws Exception {
		// given
		String invalidNx = "0";
		String invalidNy = "1001";
		String invalidNowDateTime = null;
		String invalidStartDateTime = null;
		String invalidEndDateTime = null;

		// when
		mockMvc.perform(get("/api/v1/weather/forecast")
				.param("nowDateTime", invalidNowDateTime)
				.param("startDateTime", invalidStartDateTime)
				.param("endDateTime", invalidEndDateTime)
				.param("nx", invalidNx)
				.param("ny", invalidNy)
				.contentType(MediaType.APPLICATION_JSON))
			// then
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("E001"))
			.andExpect(jsonPath("$.message").value(containsString("잘못된 요청")))
			.andExpect(jsonPath("$.data.nx").value("위도는 1 이상이어야 합니다."))
			.andExpect(jsonPath("$.data.ny").value("경도는 999 이하여야 합니다."))
			.andExpect(jsonPath("$.data.nowDateTime").value("현재 시간을 입력해주세요."))
			.andExpect(jsonPath("$.data.startDateTime").value("외출 시작 시간을 입력해주세요."))
			.andExpect(jsonPath("$.data.endDateTime").value("외출 끝 시간을 입력해주세요."));
	}
}

