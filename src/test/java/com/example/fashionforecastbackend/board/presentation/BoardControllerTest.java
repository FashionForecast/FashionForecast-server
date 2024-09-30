package com.example.fashionforecastbackend.board.presentation;

import static com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.fashionforecastbackend.board.dto.request.BoardRequest;
import com.example.fashionforecastbackend.board.dto.response.BoardDetailResponse;
import com.example.fashionforecastbackend.board.dto.response.BoardListResponse;
import com.example.fashionforecastbackend.board.service.BoardService;
import com.example.fashionforecastbackend.global.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BoardController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@WithMockUser
class BoardControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BoardService boardService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void postBoard() throws Exception {
		BoardRequest boardRequest = new BoardRequest("OOTC 짱");

		doNothing().when(boardService).createInquiry(boardRequest);

		mockMvc.perform(post("/api/v1/board")
				.with(csrf())
				.content(objectMapper.writeValueAsString(boardRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(204))
			.andExpect(jsonPath("$.message").value("NO_CONTENT"))
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("text").description("게시글 내용")
						.attributes(field("format", "1자 이상 글자"))
				)
			));
	}

	@Test
	void getBoardDetail() throws Exception {
		BoardDetailResponse response = new BoardDetailResponse("OOTC 짱");
		given(boardService.getDetail(any(Long.class))).willReturn(response);

		mockMvc.perform(get("/api/v1/board/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.text").value("OOTC 짱"))
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("id").description("게시글 ID")
				),
				responseFields(
					fieldWithPath("status").type(JsonFieldType.NUMBER).description("HttpStatus"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("상태 메세지")
				)
					.andWithPrefix("data.",
						fieldWithPath("text").type(JsonFieldType.STRING).description("게시글 상세 내용")
					))
			);
	}

	@Test
	void getBoardList() throws Exception {
		BoardListResponse board1 = new BoardListResponse(1L, "OOTC 짱1");
		BoardListResponse board2 = new BoardListResponse(1L, "OOTC 짱2");

		PageImpl<BoardListResponse> boardPage = new PageImpl<>(List.of(board1, board2), PageRequest.of(0, 10), 2);

		given(boardService.getAll(any(Pageable.class))).willReturn(boardPage);

		mockMvc.perform(get("/api/v1/board")
				.param("page", "0")
				.param("size", "10")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].text").value("OOTC 짱1"))
			.andExpect(jsonPath("$.data[1].text").value("OOTC 짱2"))
			.andExpect(jsonPath("$.total_elements").value(2))
			.andExpect(jsonPath("$.page_number").value(0))
			.andExpect(jsonPath("$.page_size").value(10))
			.andExpect(jsonPath("$.first").value(true))
			.andExpect(jsonPath("$.last").value(true))
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("OK"))
			.andDo(restDocs.document(
				queryParameters(
					parameterWithName("page").description("조회할 페이지 번호").optional(),
					parameterWithName("size").description("한 페이지당 데이터 개수").optional()
				),
				responseFields(
					fieldWithPath("data[].id").description("게시글 ID"),
					fieldWithPath("data[].text").description("게시글 내용"),
					fieldWithPath("first").description("첫 번째 페이지 여부"),
					fieldWithPath("last").description("마지막 페이지 여부"),
					fieldWithPath("total_elements").description("전체 요소 수"),
					fieldWithPath("page_number").description("현재 페이지 번호"),
					fieldWithPath("page_size").description("페이지 당 요소 수"),
					fieldWithPath("offset").description("데이터 오프셋"),
					fieldWithPath("sort.empty").description("정렬이 비어있는지 여부"),
					fieldWithPath("sort.unsorted").description("정렬되지 않은 여부"),
					fieldWithPath("sort.sorted").description("정렬된 여부"),
					fieldWithPath("code").description("응답 상태 코드"),
					fieldWithPath("message").description("상태 메시지")
				)
			));
	}
}