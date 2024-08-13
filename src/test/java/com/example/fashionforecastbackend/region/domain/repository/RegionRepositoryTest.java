package com.example.fashionforecastbackend.region.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.fashionforecastbackend.region.domain.Address;
import com.example.fashionforecastbackend.region.domain.Region;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegionRepositoryTest {

	@Autowired
	private RegionRepository regionRepository;

	// Region 객체를 생성하는 픽스처 메서드
	private Region createRegion(String code, String city, String district, String neighborhood, int nx, int ny) {
		Address address = new Address();
		address.setCode(code);
		address.setCity(city);
		address.setDistrict(district);
		address.setNeighborhood(neighborhood);

		Region region = Region.builder()
			.address(address)
			.nx(nx)
			.ny(ny)
			.build();

		return region;
	}

	@DisplayName("saveAll_2개_레코드가_하나의_쿼리로_인서트")
	@Test
	void saveAll_2개_레코드가_하나의_쿼리로_인서트() {
		//given
		Region region1 = createRegion("001", "서울특별시", "강남구", "삼성동", 60, 127);
		Region region2 = createRegion("002", "부산광역시", "해운대구", "정동", 98, 76);
		//when
		regionRepository.saveAll(List.of(region1, region2));
		Region record1 = regionRepository.findByAddress("서울특별시", "강남구", "삼성동").orElse(null);
		Region record2 = regionRepository.findByAddress("부산광역시", "해운대구", "정동").orElse(null);

		//then
		assertThat(record1.getAddress().getCode()).isEqualTo("001");
		assertThat(record2.getAddress().getCode()).isEqualTo("002");

	}

	@DisplayName("isExist_1개_이상의_레코드가_존재하면_true를_반환한다")
	@Test
	void isExist_1개_이상의_레코드가_존재하면_true를_반환한다() {

		// when
		Region region = createRegion("001", "서울특별시", "강남구", "삼성동", 60, 127);
		regionRepository.saveAll(List.of(region));

		// then
		assertThat(regionRepository.isExist()).isTrue();
	}
}
