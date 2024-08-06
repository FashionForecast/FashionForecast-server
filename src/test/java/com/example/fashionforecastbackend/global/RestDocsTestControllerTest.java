package com.example.fashionforecastbackend.global;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
class RestDocsTestControllerTest extends RestDocsTest{

    @DisplayName("200 상태, Hello를 응답으로 반환 후 restDocs를 생성한다")
    @Test
    void restDocs_스니펫_생성_확인() throws Exception {
        //given
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello"))
                .andDo(restDocs.document());

        // when
        // 스니펫 생성 확인
        String snippetsDir = "build/generated-snippets/rest-docs-test-controller-test/example-test/";

        File requestSnippet = new File(snippetsDir + "http-request.adoc");
        File responseSnippet = new File(snippetsDir + "http-response.adoc");

        //then
        assertThat(requestSnippet).exists();
        assertThat(responseSnippet).exists();
    }


}
