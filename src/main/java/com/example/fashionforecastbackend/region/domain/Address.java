package com.example.fashionforecastbackend.region.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String code;
    private String city;
    private String district;
    private String neighborhood;

}