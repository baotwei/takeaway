package com.lky.project.mapper;

import com.lky.project.pojo.OrderGood;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderGoodMapper {

    /*根据oid获取订单商品等信息*/
    List<OrderGood> findGidByOid(Integer oid);
}
