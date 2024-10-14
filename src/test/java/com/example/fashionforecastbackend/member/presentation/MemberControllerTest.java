package com.example.fashionforecastbackend.member.presentation;

import static com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration.*;
import static com.example.fashionforecastbackend.member.domain.constant.Gender.*;
import static com.example.fashionforecastbackend.recommend.domain.TempCondition.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.fashionforecastbackend.global.ControllerTest;
import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.OutingTimeRequest;
import com.example.fashionforecastbackend.member.dto.request.RegionRequest;
import com.example.fashionforecastbackend.member.dto.request.TempConditionRequest;
import com.example.fashionforecastbackend.member.dto.response.MemberInfoResponse;
import com.example.fashionforecastbackend.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class MemberControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@Autowired
	private ObjectMapper objectMapper;

	private UserDetail userDetail;

	private UsernamePasswordAuthenticationToken authentication;

	@BeforeEach
	void setUp() {
		userDetail = new UserDetail(
			123L,
			"ROLE_USER"
		);

		authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
			List.of(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test
	void getMemberInfo() throws Exception {

		MemberInfoResponse response = new MemberInfoResponse("testUser", "서울시 동작구",
			"오전 08시", "오후 08시", NORMAL, FEMALE, "http://k.kakaocdn.net/dn/~~.jpg");
		given(memberService.getMemberInfo(any(Long.class))).willReturn(response);

		final ResultActions resultActions = performGetRequest("");

		resultActions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data.nickname").value("testUser"))
			.andExpect(jsonPath("$.data.region").value("서울시 동작구"))
			.andExpect(jsonPath("$.data.outingStartTime").value("오전 08시"))
			.andExpect(jsonPath("$.data.outingEndTime").value("오후 08시"))
			.andExpect(jsonPath("$.data.tempCondition").value(NORMAL.toString()))
			.andExpect(jsonPath("$.data.gender").value(FEMALE.toString()))
			.andExpect(jsonPath("$.data.imageUrl").value("http://k.kakaocdn.net/dn/~~.jpg"))
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("사용자 정보")
				)
					.andWithPrefix("data.",
						fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
						fieldWithPath("region").type(JsonFieldType.STRING).description("기본 지역"),
						fieldWithPath("outingStartTime").type(JsonFieldType.STRING).description("기본 외출 시작 시간"),
						fieldWithPath("outingEndTime").type(JsonFieldType.STRING).description("기본 외출 끝난 시간"),
						fieldWithPath("tempCondition").type(JsonFieldType.STRING).description("기본 옷차림 두께"),
						fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
						fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("프로필 이미지 Url")
					)
			));
	}

	@Test
	void addGender() throws Exception {

		MemberGenderRequest request = new MemberGenderRequest(FEMALE);
		doNothing().when(memberService).saveGender(eq(request), any(Long.class));

		final ResultActions resultActions = performPostRequest("/gender", request);

		resultActions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("gender").type(JsonFieldType.STRING).description("성별")
						.attributes(field("format", "MALE/FEMALE"))
				)
			));
	}

	@Test
	void updateRegion() throws Exception {

		RegionRequest request = new RegionRequest("서울시 동작구");
		doNothing().when(memberService).updateRegion(eq(request), any(Long.class));

		final ResultActions resultActions = performPatchRequest("/region", request);

		resultActions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("region").type(JsonFieldType.STRING).description("지역 이름")
				)
			));
	}

	@Test
	void updateOutingTime() throws Exception {

		OutingTimeRequest request = new OutingTimeRequest("오전 08시", "오후 08시");
		doNothing().when(memberService).updateOutingTime(eq(request), any(Long.class));

		final ResultActions resultActions = performPatchRequest("/outingTime", request);

		resultActions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("startTime").type(JsonFieldType.STRING).description("시작 시간"),
					fieldWithPath("endTime").type(JsonFieldType.STRING).description("끝 시간")
				)
			));
	}

	@Test
	void updateTempCondition() throws Exception {

		TempConditionRequest request = new TempConditionRequest(WARM);
		doNothing().when(memberService).updateTempStage(eq(request), any(Long.class));

		final ResultActions resultActions = performPatchRequest("/temp-condition", request);

		resultActions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("tempCondition").type(JsonFieldType.STRING).description("옷차림 두께")
				)
			));
	}

	private ResultActions performGetRequest(final String path) throws Exception {
		return mockMvc.perform(get("/api/v1/member" + path)
			.contentType(MediaType.APPLICATION_JSON));
	}

	private <T> ResultActions performPostRequest(final String path, final T request) throws Exception {
		return mockMvc.perform(post("/api/v1/member" + path)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf().asHeader()));
	}

	private <T> ResultActions performPatchRequest(final String path, final T request) throws Exception {
		return mockMvc.perform(patch("/api/v1/member" + path)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf().asHeader()));
	}
}