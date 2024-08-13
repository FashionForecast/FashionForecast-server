package com.example.fashionforecastbackend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestClientConfig {

	/*
	기상청에서 제공하는 서비스키의 경우 이미 인코딩이 된 값이기 때문에 다시 인코딩할 필요가 없다.
	*/
	@Bean
	public RestClient restClient() {
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
		return RestClient.builder()
			.uriBuilderFactory(factory)
			.build();
	}
}
