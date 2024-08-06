package com.example.fashionforecastbackend.region.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.fashionforecastbackend.region.domain.Region;
import com.opencsv.CSVReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NationalForecastRegionReaderTest {

    @InjectMocks
    private NationalForecastRegionReader nationalForecastRegionReader;

    @Mock
    private CSVReader csvReader;

    @BeforeEach
    public void setUp() throws Exception {
        when(csvReader.readNext()).thenReturn(
                new String[]{"code", "city", "district", "neighborhood", "nx", "ny"},
                new String[]{"001", "서울특별시", "강남구", "삼성동", "60", "127"},
                new String[]{"002", "부산광역시", "해운대구", "정동", "98", "76"},
                null
        );
    }

    @DisplayName("csv 파일을 정상적으로 읽어온다")
    @Test
    public void read_정상_케이스() {
        //given
        List<Region> regions = nationalForecastRegionReader.read();
        //when

        //then
        assertThat(regions).hasSize(2);
        assertThat(regions.get(0).getAddress().getCode()).isEqualTo("001");
        assertThat(regions.get(0).getAddress().getCity()).isEqualTo("서울특별시");
        assertThat(regions.get(0).getAddress().getDistrict()).isEqualTo("강남구");
        assertThat(regions.get(0).getAddress().getNeighborhood()).isEqualTo("삼성동");
        assertThat(regions.get(0).getNx()).isEqualTo("60");
        assertThat(regions.get(0).getNy()).isEqualTo("127");
    }

    @DisplayName("잘못된 형식의 라인은 무시되고 읽어오지 않는다")
    @Test
    void read_잘못된_형식의_라인_무시() throws Exception {
        //given

        //when
        when(csvReader.readNext()).thenReturn(
                new String[]{"code", "city", "district", "neighborhood", "nx", "ny"},
                new String[]{"001", "서울특별시", "강남구", "삼성동", "60", "127"},
                new String[]{"002", "부산광역시"},
                null
        );

        List<Region> regions = nationalForecastRegionReader.read();

        //then
        assertThat(regions).hasSize(1);
        assertThat(regions.get(0).getAddress().getCode()).isEqualTo("001");
        assertThat(regions.get(0).getAddress().getCity()).isEqualTo("서울특별시");
        assertThat(regions.get(0).getAddress().getDistrict()).isEqualTo("강남구");
        assertThat(regions.get(0).getAddress().getNeighborhood()).isEqualTo("삼성동");
        assertThat(regions.get(0).getNx()).isEqualTo("60");
        assertThat(regions.get(0).getNy()).isEqualTo("127");

        assertThatThrownBy(() -> regions.get(1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @DisplayName("csv파일을 읽어오는 중 예외가 발생한다.")
    @Test
    public void read_예외_발생() throws Exception {
        //given

        //when
        when(csvReader.readNext()).thenThrow(new RuntimeException("파일 읽기 실패"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            nationalForecastRegionReader.read();
        });

        //then
        assertThat(exception.getMessage()).isEqualTo("national_forecast_regions.csv을 읽어오는데 실패하였습니다.");
    }
}