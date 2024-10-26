package com.example.fashionforecastbackend.recommend.presetation;

import static com.example.fashionforecastbackend.outfit.domain.OutfitType.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.example.fashionforecastbackend.recommend.domain.TempCondition;
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
			RecommendResponse.builder().names(List.of("민소매", "반팔티")).outfitType(TOP).build(),
			RecommendResponse.builder().names(List.of("반바지")).outfitType(BOTTOM).build(),
			RecommendResponse.builder().names(List.of("장우산")).outfitType(ETC).build()
		);

		// RecommendService의 동작을 Mocking
		given(recommendService.getRecommendedOutfit(any(RecommendRequest.class))).willReturn(response);

		// 테스트용 RecommendRequest 생성
		RecommendRequest request = new RecommendRequest(29, 7, 30, 3, TempCondition.NORMAL);

		// API 호출 및 검증
		mockMvc.perform(get("/api/v1/recommend/default")
				.param("extremumTmp", String.valueOf(request.extremumTmp()))
				.param("maxMinTmpDiff", String.valueOf(request.maxMinTmpDiff()))
				.param("maximumPop", String.valueOf(request.maximumPop()))
				.param("maximumPcp", String.valueOf(request.maximumPcp()))
				.param("tempCondition", request.tempCondition().toString())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].names[0]").value("민소매"))
			.andExpect(jsonPath("$.data[0].names[1]").value("반팔티"))
			.andExpect(jsonPath("$.data[0].outfitType").value("TOP"))
			.andExpect(jsonPath("$.data[1].names[0]").value("반바지"))
			.andExpect(jsonPath("$.data[1].outfitType").value("BOTTOM"))
			.andExpect(jsonPath("$.data[2].names[0]").value("장우산"))
			.andExpect(jsonPath("$.data[2].outfitType").value("ETC"))
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("extremumTmp").description("최고 또는 최저 기온"),
					parameterWithName("maxMinTmpDiff").description("최고 최저 기온차"),
					parameterWithName("maximumPop").description("최대 강수확률"),
					parameterWithName("maximumPcp").description("최대 강수량"),
					parameterWithName("tempCondition")
						.description("시원하게/보통/따뜻하게 옵션 - 그룹 1은 WARM 비활성화, 그룹 8은 COOL 비활성화")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY).description("옷차림 데이터")
				)
					.andWithPrefix("data.[].",
						fieldWithPath("names").type(JsonFieldType.ARRAY).description("옷 이름"),
						fieldWithPath("outfitType").type(JsonFieldType.STRING).description("옷 카테고리")
					))
			);
	}

}