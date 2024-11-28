package com.example.fashionforecastbackend.customOutfit.presentation;

import static com.example.fashionforecastbackend.recommend.domain.TempCondition.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import org.springframework.test.web.servlet.ResultActions;

import com.example.fashionforecastbackend.customOutfit.dto.request.DeleteGuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.GuestOutfitResponse;
import com.example.fashionforecastbackend.customOutfit.service.GuestOutfitService;
import com.example.fashionforecastbackend.global.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GuestOutfitController.class)
@AutoConfigureRestDocs
@WithMockUser
@MockBean(JpaMetamodelMappingContext.class)
class GuestOutfitControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private GuestOutfitService guestOutfitService;

	@Test
	@DisplayName("옷차림 추가 성공")
	void addOutfit() throws Exception {
		// given
		GuestOutfitRequest request =
			new GuestOutfitRequest("guest123", "긴팔티", "#111111", "면바지", "#000000", 2);

		willDoNothing().given(guestOutfitService).saveGuestOutfit(any(GuestOutfitRequest.class));

		// when
		ResultActions resultActions = mockMvc.perform(post("/api/v1/guest/outfit")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf()));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid").type(JsonFieldType.STRING).description("Guest UUID"),
					fieldWithPath("topType").type(JsonFieldType.STRING).description("상의 유형"),
					fieldWithPath("topColor").type(JsonFieldType.STRING).description("상의 컬러코드"),
					fieldWithPath("bottomType").type(JsonFieldType.STRING).description("하의 유형"),
					fieldWithPath("bottomColor").type(JsonFieldType.STRING).description("하의 컬러코드"),
					fieldWithPath("tempStageLevel").type(JsonFieldType.NUMBER).description("온도 단계 레벨")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("반환 데이터")
				)
			));
	}

	@Test
	@DisplayName("옷차림 조회 성공")
	void getOutfits() throws Exception {
		// given
		String uuid = "guest123";
		GuestOutfitResponse guestOutfit1 = new GuestOutfitResponse(
			2,
			"긴팔티",
			"#111111",
			"면바지",
			"#000000"
		);
		GuestOutfitResponse guestOutfit2 = new GuestOutfitResponse(
			3,
			"후드티",
			"#111111",
			"청바지",
			"#000000"
		);
		given(guestOutfitService.getGuestOutfitsByUuid(anyString())).willReturn(List.of(guestOutfit1, guestOutfit2));

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/guest/outfit/{uuid}", uuid));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data[].tempStageLevel").type(JsonFieldType.NUMBER).description("온도 단계 레벨"),
					fieldWithPath("data[].topType").type(JsonFieldType.STRING).description("상의 유형"),
					fieldWithPath("data[].topColor").type(JsonFieldType.STRING).description("상의 컬러코드"),
					fieldWithPath("data[].bottomType").type(JsonFieldType.STRING).description("하의 유형"),
					fieldWithPath("data[].bottomColor").type(JsonFieldType.STRING).description("하의 컬러코드")
				)
			));
	}

	@Test
	@DisplayName("온도 단계별 옷차림 조회 성공")
	void getTempStageOutfits() throws Exception {
		// given
		String uuid = "guest123";
		GuestTempStageOutfitRequest request = new GuestTempStageOutfitRequest(20, NORMAL);
		GuestOutfitResponse response = new GuestOutfitResponse(
			3,
			"후드티",
			"#111111",
			"청바지",
			"#000000"
		);

		given(guestOutfitService.getGuestTempStageOutfit(any(GuestTempStageOutfitRequest.class), eq(uuid)))
			.willReturn(response);

		// when
		ResultActions resultActions = mockMvc.perform(get("/api/v1/guest/outfit/temp-stage/{uuid}", uuid) // URL 템플릿 사용
				.param("extremumTmp", String.valueOf(request.extremumTmp()))
				.param("tempCondition", request.tempCondition().name())
				.contentType(MediaType.APPLICATION_JSON)
		);

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data.tempStageLevel").value(response.tempStageLevel()))
			.andExpect(jsonPath("$.data.topType").value(response.topType()))
			.andExpect(jsonPath("$.data.topColor").value(response.topColor()))
			.andExpect(jsonPath("$.data.bottomType").value(response.bottomType()))
			.andExpect(jsonPath("$.data.bottomColor").value(response.bottomColor()))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("uuid").description("게스트 UUID")
				),
				queryParameters(
					parameterWithName("extremumTmp").description("현재 최저 혹은 최고 온도"),
					parameterWithName("tempCondition").description("옷차림 설정 옵션 (COOL/NORMAL/WARM)")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메시지"),
					fieldWithPath("data.tempStageLevel").type(JsonFieldType.NUMBER).description("온도 단계 레벨"),
					fieldWithPath("data.topType").type(JsonFieldType.STRING).description("상의 유형"),
					fieldWithPath("data.topColor").type(JsonFieldType.STRING).description("상의 색상 코드"),
					fieldWithPath("data.bottomType").type(JsonFieldType.STRING).description("하의 유형"),
					fieldWithPath("data.bottomColor").type(JsonFieldType.STRING).description("하의 색상 코드")
				)
			));
	}


	@Test
	@DisplayName("옷차림 수정 성공")
	void updateOutfit() throws Exception {
		// given
		GuestOutfitRequest request =
			new GuestOutfitRequest("guest123", "긴팔티", "#111111", "면바지", "#000000", 2);
		willDoNothing().given(guestOutfitService).updateGuestOutfit(any(GuestOutfitRequest.class));

		// when
		ResultActions resultActions = mockMvc.perform(patch("/api/v1/guest/outfit")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf()));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid").type(JsonFieldType.STRING).description("게스트 UUID"),
					fieldWithPath("topType").type(JsonFieldType.STRING).description("상의 유형"),
					fieldWithPath("topColor").type(JsonFieldType.STRING).description("상의 컬러코드"),
					fieldWithPath("bottomType").type(JsonFieldType.STRING).description("하의 유형"),
					fieldWithPath("bottomColor").type(JsonFieldType.STRING).description("하의 컬러코드"),
					fieldWithPath("tempStageLevel").type(JsonFieldType.NUMBER).description("온도 단계 레벨")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("반환 데이터")
				)
			));
	}

	@Test
	@DisplayName("옷차림 삭제 성공")
	void deleteOutfit() throws Exception {
		// given
		DeleteGuestOutfitRequest request = new DeleteGuestOutfitRequest("guest123", 2);
		willDoNothing().given(guestOutfitService).deleteGuestOutfit(any(DeleteGuestOutfitRequest.class));

		// when
		ResultActions resultActions = mockMvc.perform(delete("/api/v1/guest/outfit")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf()));

		// then
		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid").type(JsonFieldType.STRING).description("게스트 UUID"),
					fieldWithPath("tempStageLevel").type(JsonFieldType.NUMBER).description("온도 단계 레벨")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("반환 데이터")
				)
			));
	}
}
