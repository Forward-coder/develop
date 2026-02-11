package com.hotel.manage.mapper;

import com.hotel.manage.entity.DictPaymentMethod;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DictPaymentMethodMapper {

    @Select("SELECT * FROM dict_payment_method ORDER BY sort_order")
    List<DictPaymentMethod> selectAll();
}
