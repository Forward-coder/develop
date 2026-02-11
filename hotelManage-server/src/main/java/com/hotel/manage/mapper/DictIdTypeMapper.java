package com.hotel.manage.mapper;

import com.hotel.manage.entity.DictIdType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictIdTypeMapper {

    @Select("SELECT * FROM dict_id_type ORDER BY sort_order")
    List<DictIdType> selectAll();
}
