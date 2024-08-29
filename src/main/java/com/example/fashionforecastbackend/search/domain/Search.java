package com.example.fashionforecastbackend.search.domain;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search {

	private String city;
	private String district;

	@Builder
	public Search(String city, String district) {
		this.city = city;
		this.district = district;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Search search = (Search)o;
		return Objects.equals(city, search.city) && Objects.equals(district, search.district);
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, district);
	}
}
