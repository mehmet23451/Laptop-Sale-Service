package com.example.demo.dto.models;

import com.example.demo.dto.DtoBase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DtoAddress extends DtoBase {
    private String city;
    private String district;
    private String neighborhood;
    private String street;
}
