package com.example.six_entities.mapper;

import com.example.six_entities.model.Coupon;
import com.example.six_entities.model.CouponDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CouponMapper {

    CouponDto toDto(Coupon coupon);

    @Mapping(target = "id", ignore = true)
    Coupon toEntity(CouponDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CouponDto dto, @MappingTarget Coupon coupon);

}
