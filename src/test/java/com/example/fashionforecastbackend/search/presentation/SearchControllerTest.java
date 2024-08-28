package com.example.fashionforecastbackend.search.presentation;

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

import com.example.fashionforecastbackend.global.ControllerTest;
import com.example.fashionforecastbackend.search.dto.request.SearchRequest;
import com.example.fashionforecastbackend.search.dto.response.SearchResponse;
import com.example.fashionforecastbackend.search.fixture.SearchFixture;
import com.example.fashionforecastbackend.search.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SearchController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WithMockUser
class SearchControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private SearchService searchService;

	@DisplayName("최근 검색어 조회 api 호출이 성공 한다.")
	@Test
	void getSearchTest() throws Exception {
		//given
		final String uuid = SearchFixture.UUID;
		final List<SearchResponse> responses = SearchFixture.SEARCH_RESPONSES;
		given(searchService.getSearch(any(String.class))).willReturn(responses);
		//when
		ResultActions resultActions = performGetRequest(uuid);

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andExpect(jsonPath("$.data[0].city").value("서울특별시"))
			.andExpect(jsonPath("$.data[0].district").value("관악구"))
			.andExpect(jsonPath("$.data[1].city").value("경기도"))
			.andExpect(jsonPath("$.data[1].district").value("남양주시"))
			.andExpect(jsonPath("$.data[2].city").value("서울특별시"))
			.andExpect(jsonPath("$.data[2].district").value("서초구"))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("uuid")
						.description("사용자 uuid")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY).description("최근 검색어 목록")
				)
					.andWithPrefix("data[].",
						fieldWithPath("city").type(JsonFieldType.STRING).description("도시"),
						fieldWithPath("district").type(JsonFieldType.STRING).description("구역")
					))
			);
	}

	@DisplayName("최근 검색어 등록이 성공 한다.")
	@Test
	void registSearchTest() throws Exception {
		//given
		final String uuid = SearchFixture.UUID;
		final SearchRequest request = SearchFixture.SEARCH_REQUEST;
		final SearchResponse response = SearchFixture.SEARCH_RESPONSE;
		given(searchService.registSearch(any(String.class), any(SearchRequest.class))).willReturn(response);

		//when
		ResultActions resultActions = performPostRequest(uuid, request);

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(201))
			.andExpect(jsonPath("$.message").value("CREATED"))
			.andExpect(jsonPath("$.data.city").value("서울특별시"))
			.andExpect(jsonPath("$.data.district").value("관악구"))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("uuid").description("사용자 uuid")
				),
				requestFields(
					fieldWithPath("city").type(JsonFieldType.STRING).description("도시"),
					fieldWithPath("district").type(JsonFieldType.STRING).description("구역")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지")
				).andWithPrefix("data.",
					fieldWithPath("city").type(JsonFieldType.STRING).description("도시"),
					fieldWithPath("district").type(JsonFieldType.STRING).description("구역"))
			));
	}

	@DisplayName("최근 검색어를 삭제 한다.")
	@Test
	void deleteSearchTest() throws Exception {
	    //given
		final String uuid = SearchFixture.UUID;
		final SearchRequest request = SearchFixture.SEARCH_REQUEST;
		willDoNothing().given(searchService).deleteSearch(any(String.class), any(SearchRequest.class));

	    //when
		ResultActions resultActions = performDeleteRequest(uuid, request);

		//then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("uuid").description("사용자 uuid 값")
				),
				requestFields(
					fieldWithPath("city").type(JsonFieldType.STRING).description("도시"),
					fieldWithPath("district").type(JsonFieldType.STRING).description("구역")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("반환 데이터")
				)
			));
	}

	private ResultActions performGetRequest(final String uuid) throws Exception {
		return mockMvc.perform(get("/api/v1/search/{uuid}", uuid)
			.contentType(MediaType.APPLICATION_JSON));
	}

	private ResultActions performPostRequest(final String uuid, final SearchRequest request) throws Exception {
		return mockMvc.perform(post("/api/v1/search/{uuid}", uuid)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf().asHeader()));
	}

	private ResultActions performDeleteRequest(final String uuid, final SearchRequest request) throws Exception {
		return mockMvc.perform(delete("/api/v1/search/{uuid}", uuid)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.with(csrf().asHeader()));
	}

}