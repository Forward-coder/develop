package com.hotel.manage.mapper;

import com.hotel.manage.entity.DictBedType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictBedTypeMapper {

    @Select("SELECT * FROM dict_bed_type WHERE is_enabled = 1 ORDER BY sort_order")
    List<DictBedType> selectAll();
}
