package com.lky.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lky.project.VO.ShopManagerRegisterVO;
import com.lky.project.VO.ShopsVO;
import com.lky.project.VO.UsersVO;
import com.lky.project.mapper.DeliveryMapper;
import com.lky.project.mapper.ShopManagerMapper;
import com.lky.project.mapper.ShopMapper;
import com.lky.project.mapper.UserMapper;
import com.lky.project.pojo.Delivery;
import com.lky.project.pojo.Shop;
import com.lky.project.pojo.ShopManager;
import com.lky.project.pojo.User;

import com.lky.project.utils.AddRedis;
import com.lky.project.utils.PageTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
public class ManagerController {

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ShopManagerMapper shopManagerMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeliveryMapper deliveryMapper;


    /*查询所有店铺以及对应的店长*/
    @RequestMapping("/findAllShopInfo")
    @AddRedis
    public String findAllShopAndShopManager(String currentPage,String pageSize){
        System.out.println("currentPage:"+currentPage);
        System.out.println("pageSize:"+pageSize);
        //首先查出所有的店铺（在开和冻结），然后根据uid获取对应的店长
//        ShopsVO shops = shopService.findAllShopAndShopManager(currentPage, Integer.valueOf(pageSize));
        List<ShopManagerRegisterVO> shopManagerVOS = new ArrayList<>();
        Integer count = shopMapper.getShopsCount();
        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
        System.out.println(pageTool.getStartIndex());
        System.out.println(pageTool.getPageSize());
        List<Shop> shopList = shopMapper.findAllShopByPage(pageTool.getStartIndex(),pageTool.getPageSize());
        for (Shop shop : shopList) {
            //根据uid获取店长信息
            ShopManager shopManager = shopManagerMapper.findShopManagerBysmid(shop.getUid());
            shopManagerVOS.add(new ShopManagerRegisterVO(shopManager,shop));
        }
        ShopsVO shops = new ShopsVO(shopManagerVOS,count);

        System.out.println("shops:"+shops);
        return JSON.toJSONString(shops,SerializerFeature.WriteMapNullValue);
    }

    /*查询所有的待办理的店铺申请*/
    @RequestMapping("/findAllShopApplyFor")
    @AddRedis
    public String findAllShopApplyFor(String currentPage,String pageSize){
//        List<ShopManagerRegisterVO> allShopApplyFor = shopService.findAllShopApplyFor(currentPage, Integer.valueOf(pageSize));
        List<ShopManagerRegisterVO> allShopApplyFor;
        List<ShopManagerRegisterVO> shopManagerVOS = new ArrayList<>();
        //获取待办理的店铺申请总数
        Integer count = shopMapper.getShopApplyForCount();
        if(count!=0){
            PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
            //获取待审核的店铺
            List<Shop> shopList = shopMapper.findAllShopApplyForByPage(pageTool.getStartIndex(), pageTool.getPageSize());
            for (Shop shop : shopList) {
                //获取店铺的店长信息
                ShopManager shopManager = shopManagerMapper.findShopManagerBysmid(shop.getUid());
                shopManagerVOS.add(new ShopManagerRegisterVO(shopManager,shop));
            }
            allShopApplyFor =  shopManagerVOS;
        }else {
            allShopApplyFor =  null;
        }

        System.out.println("findAllShopApplyFor:"+allShopApplyFor);
        if(allShopApplyFor!=null){
            return JSON.toJSONString(allShopApplyFor);
        }else {
            return "none";
        }

    }

    /*获取所有用户*/
    @RequestMapping("/findAllUserByPage")
    @AddRedis
    public String findAllUserByPage(String currentPage,String pageSize){
        //UsersVO userVO = userService.findAllUserByPage(currentPage, Integer.valueOf(pageSize));
//        HashMap<String, Object> hashMap = userService.findAllUserByPage(currentPage, Integer.valueOf(pageSize));
        //1、获取用户总数count
        Integer count = userMapper.getUserCount();
        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
        //分页获取用户
        List<User> users = userMapper.findAllUserByPage(pageTool.getStartIndex(), pageTool.getPageSize());
        //System.out.println("users:"+users);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("users",users);
        hashMap.put("count",count);

        //System.out.println("users:"+users);
        //对象转换成json数据时，会自动忽略为null的值，如果有为null的值需要显示，需要加参数
        //System.out.println("users:"+JSON.toJSONString(hashMap.get("users"), SerializerFeature.WriteMapNullValue));
        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
    }

    /*分页查询所有骑手*/
    @RequestMapping("/findAllRidersByPage")
    @AddRedis
    public String findAllRidersByPage(String currentPage,String pageSize){
//        HashMap<String, Object> hashMap = deliveryService.findAllRidersByPage(currentPage, Integer.valueOf(pageSize));
        Integer count = deliveryMapper.getRidersCount();
        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
        List<Delivery> deliveries = deliveryMapper.findAllRidersByPage(pageTool.getStartIndex(), pageTool.getPageSize());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("deliveries",deliveries);
        hashMap.put("count",count);

        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
    }

    /*分页查询所有店铺管理人员*/
    @RequestMapping("/findAllShopManagerByPage")
    @AddRedis
    public String findAllShopManagerByPage(String currentPage,String pageSize){
//        HashMap<String, Object> hashMap = shopManagerService.findAllShopManagerByPage(currentPage, Integer.valueOf(pageSize));
        //获取店铺管理人员总数
        Integer count = shopManagerMapper.getShopManagerCount();
        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
        List<ShopManager> shopManagers = shopManagerMapper.findAllShopManagerByPage(pageTool.getStartIndex(), pageTool.getPageSize());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("shopManagers",shopManagers);
        hashMap.put("count",count);

        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
    }

    /*保存修改的店铺信息*/
    @RequestMapping("/saveShopInfo")
    public String saveShopInfo(@RequestBody Shop shop){
        //System.out.println(shop);
//        boolean flag = shopService.saveShopInfo(shop);
        Integer row = shopMapper.saveShopInfo(shop);
        if(row>0){
            return "success";
        }
        return "fail";

    }

    /*修改店铺状态*/
    @RequestMapping("/changeShopStat")
    public String changeShopStat(String stat,String sid){
//        boolean flag = shopService.changeShopStat(stat, sid);
        Integer row = shopMapper.changeShopStat(stat, sid);
        if(row>0){
            return "success";
        }
        return "fail";
    }

    /*同意店铺申请*/
    @RequestMapping("/agreeShopApplyFor")
    public String agreeShopApplyFor(String sid){
//        boolean flag = shopService.agreeShopApplyFor(sid);
        Integer row = shopMapper.agreeShopApplyFor(sid);
        if(row>0){
            return "success";
        }
        return "fail";
    }
}



