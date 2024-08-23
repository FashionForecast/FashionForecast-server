package com.example.fashionforecastbackend.recommend.presetation;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
import com.example.fashionforecastbackend.recommend.dto.RecommendRequest;
import com.example.fashionforecastbackend.recommend.dto.RecommendResponse;
import com.example.fashionforecastbackend.recommend.service.RecommendService;

@WebMvcTest(RecommendationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WithMockUser
class RecommendationControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RecommendService recommendService;

	@Test
	void getDefaultRecommend() throws Exception {

		// 가짜 RecommendResponse 객체 생성
		List<RecommendResponse> response = Arrays.asList(
			RecommendResponse.builder().name("민소매").outfitType("TOP").build(),
			RecommendResponse.builder().name("반바지").outfitType("BOTTOM").build(),
			RecommendResponse.builder().name("장우산").outfitType("BASIC_UMBRELLA").build()
		);

		// RecommendService의 동작을 Mocking
		given(recommendService.getRecommendedOutfit(any(RecommendRequest.class))).willReturn(response);

		// 테스트용 RecommendRequest 생성
		RecommendRequest request = new RecommendRequest(60, 128,
			LocalDateTime.now(),
			LocalDateTime.now().plusHours(8));

		// API 호출 및 검증
		mockMvc.perform(get("/api/v1/recommend/default")
				.param("nx", String.valueOf(request.nx()))
				.param("ny", String.valueOf(request.ny()))
				.param("startTime", request.startTime().toString())
				.param("endTime", request.endTime().toString())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].name").value("민소매"))
			.andExpect(jsonPath("$.data[0].outfitType").value("TOP"))
			.andExpect(jsonPath("$.data[1].name").value("반바지"))
			.andExpect(jsonPath("$.data[1].outfitType").value("BOTTOM"))
			.andExpect(jsonPath("$.data[2].name").value("장우산"))
			.andExpect(jsonPath("$.data[2].outfitType").value("BASIC_UMBRELLA"))
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("nx").description("위도 (정수값)"),
					parameterWithName("ny").description("경도 (정수값)"),
					parameterWithName("startTime").description(
						"시작 시간 (예: '2024-08-11T15:00:00', ISO 8601 형식의 문자열, ':'는 '%3A'로 인코딩됨)"),
					parameterWithName("endTime").description(
						"종료 시간 (예: '2024-08-11T20:00:00', ISO 8601 형식의 문자열, ':'는 '%3A'로 인코딩됨)")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("요청 성공 여부"),
					fieldWithPath("data").type(JsonFieldType.ARRAY).description("날씨 데이터")
				)
					.andWithPrefix("data.[].",
						fieldWithPath("name").type(JsonFieldType.STRING).description("옷 이름"),
						fieldWithPath("outfitType").type(JsonFieldType.STRING).description("옷 카테고리")
					))
			);
	}

}