package com.lky.project.controller;

import com.alibaba.fastjson.JSON;
import com.lky.project.mapper.AddressMapper;
import com.lky.project.pojo.Address;
import com.lky.project.VO.AddressVO;
import com.lky.project.utils.AddRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author Administrator
 */
@RestController
public class AddressController {
    @Autowired
    private AddressMapper addressMapper;


    /*根据用户id查询所有address*/
    @RequestMapping("/listAddress")
    @AddRedis
    public String listAddress(String uid) {
        List<Address> addressList = addressMapper.listAllAddress(Integer.valueOf(uid));
        return JSON.toJSONString(addressList);
    }

    /*根据用户id修改address*/
    @RequestMapping("/updateAddressById")
    public String updateAddressById(@RequestBody Address address) {
        int row = addressMapper.updateAddressById(address);
        if (row > 0) {
            return "success";
        } else {
            return "fail";
        }
    }

    /*添加address*/
    @RequestMapping("/addAddress")
    public String addAddress(@RequestBody AddressVO addressInfo) {
        System.out.println(addressInfo);
        int row = addressMapper.addAddress(addressInfo);
        if (row > 0) {
            return "success";
        }
        return "fail";
    }
}


