package com.example.fashionforecastbackend.customOutfit.presentation;

import static com.example.fashionforecastbackend.customOutfit.fixture.MemberOutfitFixture.*;
import static com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration.*;
import static com.example.fashionforecastbackend.recommend.domain.TempCondition.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedList;
import java.util.List;

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

import com.example.fashionforecastbackend.customOutfit.dto.request.GuestOutfitsRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.MemberOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.request.MemberTempStageOutfitRequest;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitGroupResponse;
import com.example.fashionforecastbackend.customOutfit.dto.response.MemberOutfitResponse;
import com.example.fashionforecastbackend.customOutfit.service.MemberOutfitService;
import com.example.fashionforecastbackend.global.ControllerTest;
import com.example.fashionforecastbackend.global.oauth2.UserDetail;
import com.example.fashionforecastbackend.recommend.domain.TempCondition;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(MemberOutfitController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberOutfitControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberOutfitService memberOutfitService;

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
	@DisplayName("옷차림 추가 성공")
	void addOutfitTest() throws Exception {
		//given
		final MemberOutfitRequest memberOutfitRequest = new MemberOutfitRequest(
			"반팔티",
			"#RRGGBB",
			"슬랙스",
			"#RRGGBB",
			2);
		willDoNothing().given(memberOutfitService).saveMemberOutfit(any(MemberOutfitRequest.class), any(Long.class));

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
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("반환 데이터")
				)
			));
	}

	@Test
	@DisplayName("회원별 옷차림 조회 성공")
	void getOutfitsTest() throws Exception {
		//given
		final LinkedList<MemberOutfitGroupResponse> response = new LinkedList<>(
			MEMBER_OUTFITS_GROUPS);
		given(memberOutfitService.getMemberOutfits(any(Long.class))).willReturn(response);
		//when
		final ResultActions resultActions = performGetRequest("/outfits");

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())

			// 레벨 1
			.andExpect(jsonPath("$.data[0].tempStageLevel").value(1))
			.andExpect(jsonPath("$.data[0].memberOutfits[0].topType").value("반팔티"))
			.andExpect(jsonPath("$.data[0].memberOutfits[0].topColor").value("#111111"))
			.andExpect(jsonPath("$.data[0].memberOutfits[0].bottomType").value("반바지"))
			.andExpect(jsonPath("$.data[0].memberOutfits[0].bottomColor").value("#222222"))

			// 레벨 2
			.andExpect(jsonPath("$.data[1].tempStageLevel").value(2))
			.andExpect(jsonPath("$.data[1].memberOutfits[0].topType").value("긴팔티"))
			.andExpect(jsonPath("$.data[1].memberOutfits[0].topColor").value("#333333"))
			.andExpect(jsonPath("$.data[1].memberOutfits[0].bottomType").value("청바지"))
			.andExpect(jsonPath("$.data[1].memberOutfits[0].bottomColor").value("#444444"))

			// 레벨 3
			.andExpect(jsonPath("$.data[2].tempStageLevel").value(3))
			.andExpect(jsonPath("$.data[2].memberOutfits[0].topType").value("셔츠"))
			.andExpect(jsonPath("$.data[2].memberOutfits[0].topColor").value("#555555"))
			.andExpect(jsonPath("$.data[2].memberOutfits[0].bottomType").value("면바지"))
			.andExpect(jsonPath("$.data[2].memberOutfits[0].bottomColor").value("#666666"))

			// 레벨 4
			.andExpect(jsonPath("$.data[3].tempStageLevel").value(4))
			.andExpect(jsonPath("$.data[3].memberOutfits[0].topType").value("니트"))
			.andExpect(jsonPath("$.data[3].memberOutfits[0].topColor").value("#777777"))
			.andExpect(jsonPath("$.data[3].memberOutfits[0].bottomType").value("청바지"))
			.andExpect(jsonPath("$.data[3].memberOutfits[0].bottomColor").value("#888888"))

			// 레벨 5
			.andExpect(jsonPath("$.data[4].tempStageLevel").value(5))
			.andExpect(jsonPath("$.data[4].memberOutfits[0].topType").value("후드티"))
			.andExpect(jsonPath("$.data[4].memberOutfits[0].topColor").value("#999999"))
			.andExpect(jsonPath("$.data[4].memberOutfits[0].bottomType").value("면바지"))
			.andExpect(jsonPath("$.data[4].memberOutfits[0].bottomColor").value("#AAAAAA"))

			// 레벨 6
			.andExpect(jsonPath("$.data[5].tempStageLevel").value(6))
			.andExpect(jsonPath("$.data[5].memberOutfits[0].topType").value("가디건"))
			.andExpect(jsonPath("$.data[5].memberOutfits[0].topColor").value("#BBBBBB"))
			.andExpect(jsonPath("$.data[5].memberOutfits[0].bottomType").value("청바지"))
			.andExpect(jsonPath("$.data[5].memberOutfits[0].bottomColor").value("#CCCCCC"))

			// 레벨 7
			.andExpect(jsonPath("$.data[6].tempStageLevel").value(7))
			.andExpect(jsonPath("$.data[6].memberOutfits[0].topType").value("패딩"))
			.andExpect(jsonPath("$.data[6].memberOutfits[0].topColor").value("#DDDDDD"))
			.andExpect(jsonPath("$.data[6].memberOutfits[0].bottomType").value("청바지"))
			.andExpect(jsonPath("$.data[6].memberOutfits[0].bottomColor").value("#EEEEEE"))

			// 레벨 8
			.andExpect(jsonPath("$.data[7].tempStageLevel").value(8))
			.andExpect(jsonPath("$.data[7].memberOutfits[0].topType").value("패딩"))
			.andExpect(jsonPath("$.data[7].memberOutfits[0].topColor").value("#FFFFFF"))
			.andExpect(jsonPath("$.data[7].memberOutfits[0].bottomType").value("기모바지"))
			.andExpect(jsonPath("$.data[7].memberOutfits[0].bottomColor").value("#000000"))

			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY).description("온도 단계별 회원 옷차림 전체 목록"),
					fieldWithPath("data[].tempStageLevel").type(JsonFieldType.NUMBER).description("온도 단계 레벨")
				)
					.andWithPrefix("data[].memberOutfits[].",
						fieldWithPath("memberOutfitId").type(JsonFieldType.NUMBER).description("회원 옷차림 ID"),
						fieldWithPath("topType").type(JsonFieldType.STRING).description("상의 유형"),
						fieldWithPath("topColor").type(JsonFieldType.STRING).description("상의 색상 코드"),
						fieldWithPath("bottomType").type(JsonFieldType.STRING).description("하의 유형"),
						fieldWithPath("bottomColor").type(JsonFieldType.STRING).description("하의 색상 코드"))
			));

	}

	@Test
	@DisplayName("현재 최저 혹은 최고 온도 기준 옷차림 조회")
	void getTempStageOutfit() throws Exception {
		//given
		final Integer extremumTemp = 20;
		final TempCondition tempCondition = NORMAL;
		final List<MemberOutfitResponse> response = MEMBER_TEMP_STAGE_OUTFITS_RESPONSE;
		final MemberTempStageOutfitRequest request = new MemberTempStageOutfitRequest(extremumTemp, tempCondition);
		final MemberOutfitResponse memberOutfit1 = response.get(0);
		final MemberOutfitResponse memberOutfit2 = response.get(1);
		given(memberOutfitService.getMemberTempStageOutfits(request, userDetail.memberId())).willReturn(response);

		//when
		final ResultActions resultActions = mockMvc.perform(get("/api/v1/member/outfits/temp-stage")
			.param("extremumTmp", String.valueOf(request.extremumTmp()))
			.param("tempCondition", request.tempCondition().name())
			.contentType(MediaType.APPLICATION_JSON));

		//then
		resultActions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data").isArray())

			.andExpect(jsonPath("$.data[0].memberOutfitId").value(String.valueOf(memberOutfit1.memberOutfitId())))
			.andExpect(jsonPath("$.data[0].topType").value(memberOutfit1.topType()))
			.andExpect(jsonPath("$.data[0].topColor").value(memberOutfit1.topColor()))
			.andExpect(jsonPath("$.data[0].bottomType").value(memberOutfit1.bottomType()))
			.andExpect(jsonPath("$.data[0].bottomColor").value(memberOutfit1.bottomColor()))

			.andExpect(jsonPath("$.data[1].memberOutfitId").value(String.valueOf(memberOutfit2.memberOutfitId())))
			.andExpect(jsonPath("$.data[1].topType").value(memberOutfit2.topType()))
			.andExpect(jsonPath("$.data[1].topColor").value(memberOutfit2.topColor()))
			.andExpect(jsonPath("$.data[1].bottomType").value(memberOutfit2.bottomType()))
			.andExpect(jsonPath("$.data[1].bottomColor").value(memberOutfit2.bottomColor()))

			.andDo(restDocs.document(

				queryParameters(
					parameterWithName("extremumTmp")
						.attributes(field("format", "정수값"))
						.description("현재 최저 혹은 최고 온도"),
					parameterWithName("tempCondition")
						.attributes(field("format", "COOL/NORMAL/WARM"))
						.description("시원하게/보통/따뜻하게 옵션 - 그룹 1은 WARM 비활성화, 그룹 8은 COOL 비활성화")),

				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY).description("현재 최저 혹은 최고 온도 기준 회원 옷차림 목록"))

					.andWithPrefix("data[].",
						fieldWithPath("memberOutfitId").type(JsonFieldType.NUMBER).description("회원 옷차림 ID"),
						fieldWithPath("topType").type(JsonFieldType.STRING).description("상의 유형"),
						fieldWithPath("topColor").type(JsonFieldType.STRING).description("상의 색상 코드"),
						fieldWithPath("bottomType").type(JsonFieldType.STRING).description("하의 유형"),
						fieldWithPath("bottomColor").type(JsonFieldType.STRING).description("하의 색상 코드"))
			));

	}

	@Test
	@DisplayName("멤버 옷차림 삭제")
	void deleteOutfitsTest() throws Exception {
		//given
		final long memberOutfitId = 1L;
		willDoNothing().given(memberOutfitService).deleteMemberOutfit(any(Long.class));

		//when
		final ResultActions resultActions = mockMvc.perform(delete("/api/v1/member/outfits/{memberOutfitId}", memberOutfitId)
			.contentType(MediaType.APPLICATION_JSON)
			.with(csrf().asHeader()));

		//then
		resultActions.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("memberOutfitId").description("회원 옷차림 ID")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("반환 데이터")
				)
			));

	}

	@Test
	@DisplayName("멤버 옷차림 수정")
	void updateOutfitTest() throws Exception {
	    //given
		final Long memberOutfitId = 1L;
		final MemberOutfitRequest memberOutfitRequest = MEMBER_OUTFIT_REQUEST;

		willDoNothing().given(memberOutfitService).updateMemberOutfit(any(Long.class), any(MemberOutfitRequest.class));
	    //when
		final ResultActions resultActions = mockMvc.perform(patch("/api/v1/member/outfits/{memberOutfitId}", memberOutfitId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(memberOutfitRequest))
			.with(csrf().asHeader()));

		//then
		resultActions.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("memberOutfitId").description("회원 옷차림 ID")
				),
				requestFields(
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
	@DisplayName("회원 옷차림에 기존 게스트 옷차림 추가 성공")
	void addOutfitFromGuestOutfitTest() throws Exception {
		// given
		GuestOutfitsRequest request = new GuestOutfitsRequest("guest123");
		willDoNothing().given(memberOutfitService)
			.saveMemberOutfitFromGuestOutfit(any(GuestOutfitsRequest.class), any(Long.class));

		// when
		ResultActions resultActions = performPostRequest("/guestOutfits", request);

		// then
		resultActions.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid").type(JsonFieldType.STRING).description("사용자 UUID")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("반환 데이터")
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


}
