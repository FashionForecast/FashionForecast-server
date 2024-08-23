package com.example.fashionforecastbackend.guest.presentation;

import static com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

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
import com.example.fashionforecastbackend.guest.dto.GuestLoginRequest;
import com.example.fashionforecastbackend.guest.dto.GuestLoginResponse;
import com.example.fashionforecastbackend.guest.service.GuestService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GuestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WithMockUser
class GuestControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private GuestService guestService;

	@DisplayName("게스트 로그인")
	@Test
	void loginTest() throws Exception {
		//given
		String uuid = UUID.randomUUID().toString();
		final GuestLoginRequest request = new GuestLoginRequest(uuid);
		final GuestLoginResponse response = GuestLoginResponse.of(uuid, false);
		given(guestService.login(any(GuestLoginRequest.class))).willReturn(response);
		//when
		mockMvc.perform(post("/api/v1/guest/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf().asHeader()))
			//then
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("uuid")
						.type(JsonFieldType.STRING)
						.optional()
						.attributes(field("format", "문자열 or null"))
						.description("UUID 값")

				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("요청 성공 여부"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("게스트 데이터")
				)
					.andWithPrefix("data.",
						fieldWithPath("uuid").type(JsonFieldType.STRING).description("uuid 값"),
						fieldWithPath("isNewGuest").type(JsonFieldType.BOOLEAN).description("신규 게스트 여부")
					)

			));

	}
}