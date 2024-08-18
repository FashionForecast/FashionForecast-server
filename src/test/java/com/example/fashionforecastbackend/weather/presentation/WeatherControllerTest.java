package com.example.fashionforecastbackend.weather.presentation;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import com.example.fashionforecastbackend.weather.dto.WeatherRequestDto;
import com.example.fashionforecastbackend.weather.dto.WeatherResponseDto;
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

	@Test
	void getWeatherTest() throws Exception {

		final List<WeatherResponseDto> response = WeatherFixture.WEATHER_RESPONSE_DTOS;
		given(weatherService.getWeather(any(WeatherRequestDto.class))).willReturn(response);
		mockMvc.perform(get("/api/v1/weather/forecast")
				.param("now", "2024-08-11T15:00:00")
				.param("nx", "60")
				.param("ny", "127")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("now").description(
						"현재시간 (예: '2024-08-11T15:00:00', ISO 8601 형식의 문자열, ':'는 '%3A'로 인코딩됨)"),
					parameterWithName("nx").description("위도 (정수값)"),
					parameterWithName("ny").description("경도 (정수값)")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("요청 성공 여부"),
					fieldWithPath("data").type(JsonFieldType.ARRAY).description("날씨 데이터")
				)
					.andWithPrefix("data.[].",
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
						fieldWithPath("ny").type(JsonFieldType.NUMBER).description("경도"))
			));
	}

	@DisplayName("잘못된 날씨 요청값으로 에러가 발생한다.")
	@Test
	void getWeatherErrorTest() throws Exception {
		// given
		String invalidNx = "0";
		String invalidNy = "1001";
		String invalidDateTime = null;

		// when
		mockMvc.perform(get("/api/v1/weather/forecast")
				.param("now", invalidDateTime)
				.param("nx", invalidNx)
				.param("ny", invalidNy)
				.contentType(MediaType.APPLICATION_JSON))
			// then
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("E001"))
			.andExpect(jsonPath("$.message").value(containsString("잘못된 요청")))
			.andExpect(jsonPath("$.data.nx").value("위도는 1 이상이어야 합니다."))
			.andExpect(jsonPath("$.data.ny").value("경도는 999 이하여야 합니다."))
			.andExpect(jsonPath("$.data.now").value("시간을 입력해주세요."));
	}
}

