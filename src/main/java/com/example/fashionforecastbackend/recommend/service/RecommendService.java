package com.example.fashionforecastbackend.recommend.service;

import java.util.List;

import com.example.fashionforecastbackend.recommend.dto.RecommendRequest;
import com.example.fashionforecastbackend.recommend.dto.RecommendResponse;

public interface RecommendService {

	List<RecommendResponse> getRecommendedOutfit(RecommendRequest recommendRequest);
}
