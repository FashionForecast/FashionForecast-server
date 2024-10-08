package com.example.fashionforecastbackend.global.login.presentation;

import static com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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

import com.example.fashionforecastbackend.global.ControllerTest;
import com.example.fashionforecastbackend.global.login.dto.request.AccessTokenRequest;
import com.example.fashionforecastbackend.global.login.dto.response.AccessTokenResponse;
import com.example.fashionforecastbackend.global.login.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(LoginController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WithMockUser
class LoginControllerTest extends ControllerTest {

	private final static String ACCESS_TOKEN = "accessToken";
	private final static String REFRESH_TOKEN = "refreshToken";
	private final static String NEW_ACCESS_TOKEN = "newAccessToken";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private LoginService loginService;

	@Test
	@DisplayName("액세스 토큰 발급")
	void issueAccessTokenTest() throws Exception {
		//given

		final Cookie cookie = new Cookie("refresh_token", REFRESH_TOKEN);
		final AccessTokenResponse response = AccessTokenResponse.of(ACCESS_TOKEN);
		given(loginService.issueAccessToken(any(String.class))).willReturn(response);

		//when
		final ResultActions resultActions = mockMvc.perform(post("/api/v1/login/token")
			.contentType(MediaType.APPLICATION_JSON)
			.cookie(cookie)
			.with(csrf().asHeader()));

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(201))
			.andExpect(jsonPath("$.message").value("CREATED"))
			.andExpect(jsonPath("$.data.accessToken").value(ACCESS_TOKEN))
			.andDo(restDocs.document(
				requestCookies(
					cookieWithName("refresh_token").description("리프레시 토큰")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지")
				).andWithPrefix("data.",
					fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"))
			));

	}

	@Test
	@DisplayName("토큰 재발급")
	void reissueTokensTest() throws Exception {
		AccessTokenRequest request = new AccessTokenRequest(ACCESS_TOKEN);
		final Cookie cookie = new Cookie("refresh_token", REFRESH_TOKEN);
		AccessTokenResponse newToken = AccessTokenResponse.of(NEW_ACCESS_TOKEN);

		given(
			loginService.renewTokens(any(AccessTokenRequest.class), any(String.class),
				any(HttpServletResponse.class))).willReturn(
			newToken);

		mockMvc.perform(post("/api/v1/login/reissue")
				.with(csrf())
				.cookie(cookie)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(201))
			.andExpect(jsonPath("$.message").value("CREATED"))
			.andExpect(jsonPath("$.data.accessToken").value(NEW_ACCESS_TOKEN))
			.andDo(restDocs.document(
				requestCookies(
					cookieWithName("refresh_token").description("리프레시 토큰")
				),
				requestFields(
					fieldWithPath("accessToken").description("액세스 토큰")
						.attributes(field("format", "jwt"))
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지")
				).andWithPrefix("data.",
					fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"))
			));
	}

}