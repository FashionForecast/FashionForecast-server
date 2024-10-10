package com.example.fashionforecastbackend.outfit.presentation;

import static com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration.*;
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
import com.example.fashionforecastbackend.outfit.domain.OutfitType;
import com.example.fashionforecastbackend.outfit.dto.request.OutfitRequest;
import com.example.fashionforecastbackend.outfit.dto.response.OutfitGroupResponse;
import com.example.fashionforecastbackend.outfit.fixture.OutfitFixture;
import com.example.fashionforecastbackend.outfit.service.OutfitService;
import com.fasterxml.jackson.databind.ObjectMapper;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = OutfitController.class)
@AutoConfigureRestDocs
class OutfitControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OutfitService outfitService;

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
	void addDefaultOutfit() throws Exception {

		OutfitRequest request = new OutfitRequest("청바지", OutfitType.BOTTOM, List.of(4, 5));

		final ResultActions resultActions = performPostRequest("/default", request);

		resultActions
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("name").description("옷 이름")
						.attributes(field("format", "널이 아닌 값")),
					fieldWithPath("outfitType").description("옷 카테고리")
						.attributes(field("format", "ENUM 값 참고")),
					fieldWithPath("tempLevels").description("온도 단계(그룹)")
						.attributes(field("format", "1 이상 정수값 - 복수개 가능 예) 1,2"))
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("성공")
				))
			);
	}

	@Test
	@DisplayName("옷유형 조회")
	void getOutfitGroupTest() throws Exception {
		//given
		final OutfitGroupResponse outfitGroupResponse = OutfitFixture.OUTFIT_GROUP_RESPONSE;
		given(outfitService.getOutfitGroup(any(Long.class))).willReturn(outfitGroupResponse);

		//when
		final ResultActions resultActions = performGetRequest();

		//then
		resultActions.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data.tops[0].name").value("반팔티"))
			.andExpect(jsonPath("$.data.tops[0].outfitType").value("TOP"))
			.andExpect(jsonPath("$.data.tops[1].name").value("긴팔티"))
			.andExpect(jsonPath("$.data.tops[1].outfitType").value("TOP"))
			.andExpect(jsonPath("$.data.tops[2].name").value("셔츠"))
			.andExpect(jsonPath("$.data.tops[2].outfitType").value("TOP"))
			.andExpect(jsonPath("$.data.bottoms[0].name").value("반바지"))
			.andExpect(jsonPath("$.data.bottoms[0].outfitType").value("BOTTOM"))
			.andExpect(jsonPath("$.data.bottoms[1].name").value("긴바지"))
			.andExpect(jsonPath("$.data.bottoms[1].outfitType").value("BOTTOM"))
			.andDo(restDocs.document(
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("옷유형 데이터")
				)
					.andWithPrefix("data.",
						fieldWithPath("tops").type(JsonFieldType.ARRAY).description("상의 유형 목록"),
						fieldWithPath("bottoms").type(JsonFieldType.ARRAY).description("하의 유형 목록")
					)
					.andWithPrefix("data.tops[].",
						fieldWithPath("name").type(JsonFieldType.STRING).description("상의 유형"),
						fieldWithPath("outfitType").type(JsonFieldType.STRING).description("옷유형"))
					.andWithPrefix("data.bottoms[].",
						fieldWithPath("name").type(JsonFieldType.STRING).description("하의 유형"),
						fieldWithPath("outfitType").type(JsonFieldType.STRING).description("옷유형"))
			));
	}

	private ResultActions performGetRequest() throws Exception {
		return mockMvc.perform(get("/api/v1/outfit")
			.contentType(MediaType.APPLICATION_JSON));
	}

	private <T> ResultActions performPostRequest(final String path, final T request) throws Exception {
		return mockMvc.perform(post("/api/v1/outfit/" + path)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf().asHeader()));
	}

}
