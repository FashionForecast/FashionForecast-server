package com.example.fashionforecastbackend.region.domain.repository;

import com.example.fashionforecastbackend.region.domain.Region;
import java.util.Collection;

public interface RegionCustomRepository {
    void saveAll(Collection<Region> regions);
    boolean isExist();
}
