package com.example.fashionforecastbackend.customOutfit.presentation;

import static com.example.fashionforecastbackend.customOutfit.fixture.GuestOutfitFixture.*;
import static com.example.fashionforecastbackend.recommend.domain.TempCondition.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import org.springframework.test.web.servlet.ResultActions;

import com.example.fashionforecastbackend.customOutfit.dto.request.DeleteGuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.GuestTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.service.GuestOutfitService;
import com.example.fashionforecastbackend.global.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GuestOutfitController.class)
@AutoConfigureRestDocs
@WithMockUser
@MockBean(JpaMetamodelMappingContext.class)
class GuestOutfitControllerTest extends ControllerTest {

	private static final String GUEST_UUID = "guest123";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private GuestOutfitService guestOutfitService;

	@Test
	@DisplayName("옷차림 추가 성공")
	void addOutfit() throws Exception {
		willDoNothing().given(guestOutfitService).saveGuestOutfit(any(GuestOutfitRequest.class));

		ResultActions resultActions = mockMvc.perform(post("/api/v1/guest/outfit")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(GUEST_OUTFIT_REQUEST))
			.with(csrf()));

		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid").type(JsonFieldType.STRING).description("사용자 UUID"),
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
		given(guestOutfitService.getGuestOutfitsByUuid(anyString())).willReturn(GUEST_OUTFITS_RESPONSE);

		ResultActions resultActions = mockMvc.perform(get("/api/v1/guest/outfit/{uuid}", GUEST_UUID)
			.param("uuid", "사용자 uuid"));

		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("uuid").description("사용자 uuid")
				),
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
		GuestTempStageOutfitRequest request = new GuestTempStageOutfitRequest(GUEST_UUID, 20, NORMAL);

		given(guestOutfitService.getGuestTempStageOutfit(any(GuestTempStageOutfitRequest.class)))
			.willReturn(GUEST_OUTFIT_RESPONSE);

		ResultActions resultActions = mockMvc.perform(get("/api/v1/guest/outfit/temp-stage")
				.param("uuid", request.uuid())
				.param("extremumTmp", String.valueOf(request.extremumTmp()))
				.param("tempCondition", request.tempCondition().name())
				.contentType(MediaType.APPLICATION_JSON)
		);

		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data.tempStageLevel").value(GUEST_OUTFIT_RESPONSE.tempStageLevel()))
			.andExpect(jsonPath("$.data.topType").value(GUEST_OUTFIT_RESPONSE.topType()))
			.andExpect(jsonPath("$.data.topColor").value(GUEST_OUTFIT_RESPONSE.topColor()))
			.andExpect(jsonPath("$.data.bottomType").value(GUEST_OUTFIT_RESPONSE.bottomType()))
			.andExpect(jsonPath("$.data.bottomColor").value(GUEST_OUTFIT_RESPONSE.bottomColor()))
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("uuid").description("사용자 UUID"),
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
		willDoNothing().given(guestOutfitService).updateGuestOutfit(any(GuestOutfitRequest.class));

		ResultActions resultActions = mockMvc.perform(patch("/api/v1/guest/outfit")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(GUEST_OUTFIT_REQUEST))
			.with(csrf()));

		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid").type(JsonFieldType.STRING).description("사용자 UUID"),
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
		DeleteGuestOutfitRequest request = new DeleteGuestOutfitRequest(GUEST_UUID, 2);
		willDoNothing().given(guestOutfitService).deleteGuestOutfit(any(DeleteGuestOutfitRequest.class));

		ResultActions resultActions = mockMvc.perform(delete("/api/v1/guest/outfit")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf()));

		resultActions.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid").type(JsonFieldType.STRING).description("사용자 UUID"),
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
