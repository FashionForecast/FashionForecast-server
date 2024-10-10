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
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import com.example.fashionforecastbackend.global.oauth2.CustomOauth2User;
import com.example.fashionforecastbackend.member.dto.request.MemberGenderRequest;
import com.example.fashionforecastbackend.member.dto.request.MemberOutfitRequest;
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

	private CustomOauth2User customOauth2User;

	private UsernamePasswordAuthenticationToken authentication;

	@BeforeEach
	void setUp() {
		customOauth2User = new CustomOauth2User(
			List.of(new SimpleGrantedAuthority("ROLE_USER")),
			Map.of("sub", "testUser"),
			"sub",
			123L,
			"testUser@example.com",
			"ROLE_USER",
			false
		);

		authentication = new UsernamePasswordAuthenticationToken(customOauth2User, null,
			customOauth2User.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

	}

	@Test
	void getMemberInfo() throws Exception {

		MemberInfoResponse response = new MemberInfoResponse("testUser", "서울시 동작구",
			"오전 08시", "오후 08시", NORMAL, FEMALE, "http://k.kakaocdn.net/dn/~~.jpg");
		given(memberService.getMemberInfo(any(Long.class))).willReturn(response);

		final ResultActions resultActions = performGetRequest();

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
	@DisplayName("옷차림 추가 성공")
	void addOutfitTest() throws Exception {
		//given
		final MemberOutfitRequest memberOutfitRequest = new MemberOutfitRequest(
			"반팔티",
			"#RRGGBB",
			"슬랙스",
			"#RRGGBB",
			2);
		doNothing().when(memberService).saveMemberOutfit(any(MemberOutfitRequest.class), any(Long.class));

		//when
		final ResultActions resultActions = performPostRequest("/outfit", memberOutfitRequest);

		//then
		resultActions.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("topType").type(JsonFieldType.STRING).description("상의 유형"),
					fieldWithPath("topColor").type(JsonFieldType.STRING).description("상의 컬러코드"),
					fieldWithPath("bottomType").type(JsonFieldType.STRING).description("하의 유형"),
					fieldWithPath("bottomColor").type(JsonFieldType.STRING).description("하의 컬러코드"),
					fieldWithPath("tempStageLevel").type(JsonFieldType.NUMBER).description("온도 단계 레벨")
				)
			));

	}

	private ResultActions performGetRequest() throws Exception {
		return mockMvc.perform(get("/api/v1/member")
			.contentType(MediaType.APPLICATION_JSON));
	}

	private <T> ResultActions performPostRequest(final String path, final T request) throws Exception {
		return mockMvc.perform(post("/api/v1/member" + path)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf().asHeader()));
	}

}