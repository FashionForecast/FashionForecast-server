package com.example.fashionforecastbackend.region.domain;

import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Address address;

	private int nx;

	private int ny;

	@Builder
	public Region(Address address, int nx, int ny) {
		this.address = address;
		this.nx = nx;
		this.ny = ny;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Region region = (Region)o;
		return nx == region.nx && ny == region.ny;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nx, ny);
	}
}