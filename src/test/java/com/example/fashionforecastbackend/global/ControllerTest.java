package com.example.fashionforecastbackend.global;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.example.fashionforecastbackend.global.jwt.service.ErrorResponseMaker;
import com.example.fashionforecastbackend.global.jwt.service.JwtService;
import com.example.fashionforecastbackend.global.restdocs.RestDocsConfiguration;

@Import({RestDocsConfiguration.class, ErrorResponseMaker.class, JwtService.class})
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {

	@Autowired
	protected RestDocumentationResultHandler restDocs;

	@Autowired
	protected MockMvc mockMvc;

	@BeforeEach
	void setUp(
		final WebApplicationContext context,
		final RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(restDocs)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.build();
	}
}
