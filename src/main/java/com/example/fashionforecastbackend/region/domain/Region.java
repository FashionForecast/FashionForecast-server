package com.example.fashionforecastbackend.region.domain;

import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	indexes = {
		@Index(name = "idx_nx_ny", columnList = "nx, ny")
	}
)
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Address address;

	private double nx;

	private double ny;

	@Builder
	public Region(Address address, double nx, double ny) {
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
