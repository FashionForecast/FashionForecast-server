package com.example.fashionforecastbackend.outfit.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.fashionforecastbackend.global.BaseTimeEntity;
import com.example.fashionforecastbackend.recommend.domain.Recommendation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Outfit extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@Enumerated(EnumType.STRING)
	private OutfitType outfitType;

	@OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Recommendation> recommendations = new ArrayList<>();

	private Outfit(String name, OutfitType outfitType) {
		this.name = name;
		this.outfitType = outfitType;
	}

	public void addRecommendations(Recommendation recommendation) {
		recommendations.add(recommendation);
		recommendation.setOutfit(this);
	}
	
	public static Outfit createOutfit(String name, OutfitType outfitType, List<Recommendation> recommendations) {
		Outfit outfit = new Outfit(name, outfitType);
		for (Recommendation recommendation : recommendations) {
			outfit.addRecommendations(recommendation);
		}
		return outfit;
	}

}