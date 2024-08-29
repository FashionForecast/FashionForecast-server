package com.example.fashionforecastbackend.search.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.exception.SearchNotExistException;
import com.example.fashionforecastbackend.search.domain.Search;
import com.example.fashionforecastbackend.search.dto.request.SearchRequest;
import com.example.fashionforecastbackend.search.dto.response.SearchResponse;
import com.example.fashionforecastbackend.search.service.SearchService;
import com.example.fashionforecastbackend.search.util.SearchValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

	private static final String KEY_PREFIX = "Search";

	private final RedisTemplate<String, Search> redisTemplate;
	private final SearchValidator validator;


	@Override
	public SearchResponse registSearch(final String uuid, final SearchRequest request) {
		validator.validateExistUser(uuid);
		String key = KEY_PREFIX + uuid;
		Search search = Search.builder()
			.city(request.city())
			.district(request.district())
			.build();

		redisTemplate.opsForList().remove(key, 1, search);
		Long size = redisTemplate.opsForList().size(key);

		if (size == 3) {
			redisTemplate.opsForList().rightPop(key);
		}
		redisTemplate.opsForList().leftPush(key, search);

		return SearchResponse.from(search);

	}

	@Override
	public List<SearchResponse> getSearch(final String uuid) {
		validator.validateExistUser(uuid);
		String key = KEY_PREFIX + uuid;
		List<Search> searches = redisTemplate.opsForList().range(key, 0, 3);
		List<SearchResponse> responses = new ArrayList<>();
		if (!searches.isEmpty()) {
			for (Search search : searches) {
				responses.add(SearchResponse.from(search));
			}
		}
		return responses;
	}

	@Override
	public void deleteSearch(final String uuid, final SearchRequest request) {
		validator.validateExistUser(uuid);
		String key = KEY_PREFIX + uuid;
		Search search = Search.builder()
			.city(request.city())
			.district(request.district())
			.build();

		Long count = redisTemplate.opsForList().remove(key, 1, search);

		if (count == 0) {
			throw new SearchNotExistException(ErrorCode.SEARCH_NOT_EXIST);
		}

	}

}
