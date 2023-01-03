package com.lky.project.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.SerializableSerializer;
import com.lky.project.VO.GoodDataVO;
import com.lky.project.VO.GoodInfoVO;
import com.lky.project.VO.GoodVO;
import com.lky.project.mapper.CategoryMapper;
import com.lky.project.mapper.GoodMapper;
import com.lky.project.mapper.ShopMapper;
import com.lky.project.pojo.Category;
import com.lky.project.pojo.Good;
import com.lky.project.pojo.Shop;
import com.lky.project.utils.AddRedis;
import com.lky.project.utils.PageTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
public class ShopController {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /*查询所有店铺*/
    @RequestMapping("/findAllShop")
    @AddRedis
    public String findAllShop() {
        List<Shop> shops = shopMapper.findAllShop();
        System.out.println(shops);
        return JSON.toJSONString(shops);
    }

    @RequestMapping("/findShopOrderBySales")
    @AddRedis
    public String findShopOrderBySales(){
        List<Shop> shopList=shopMapper.findShopOrderBySales();
        return  JSON.toJSONString(shopList);
    }

    @RequestMapping("/findShopBySid")
    @AddRedis
    public String findShopBySid(Integer sid){
        Shop shop=shopMapper.findShopBySid(sid);
        return  JSON.toJSONString(shop);
    }

    /*根据店铺id获取商品*/
    @GetMapping("/findAllGoodsBySid")
    @AddRedis
    public String findAllGoodsBySid(String sid){
        //System.out.println(sid);
//        List<GoodVO> goods = shopService.findAllGoodsBySid(sid);
        List<GoodVO> goodList = new ArrayList<>();
        //1、根据sid获取所有的category
        List<Category> categories = shopMapper.findAllCategoryBySid(sid);
        //System.out.println("categories:"+categories);
        //2、根据category的id找到其下的所有good
        for (Category category : categories) {
            List<Good> goods = shopMapper.findAllGoodByCid(category.getCid());
            //System.out.println("goods:"+goods);
            //3、将categoryName和其下的good放进goodVO对象里
            GoodVO goodVO = new GoodVO(category.getCategoryName(), goods);
            goodList.add(goodVO);
        }
        //System.out.println(goodList);
        //System.out.println(JSON.toJSONString(goodList));

        System.out.println(goodList);
        return JSON.toJSONString(goodList);
    }



    /*获取各个商品的销量*/
    @RequestMapping("/getGoodData")
    @AddRedis
    public String getGoodData(String sid){
        List<Good> goodList = goodMapper.findAllGoodBySid(sid);
        List<GoodDataVO> list = new ArrayList<>();
        for (Good good : goodList) {
            GoodDataVO goodDataVO = new GoodDataVO();
            goodDataVO.setName(good.getGoodName());
            goodDataVO.setValue(good.getSales());
            list.add(goodDataVO);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("goods",list);
        return JSON.toJSONString(hashMap);
    }

    /*根据店铺license查询店铺是否存在*/
    @RequestMapping("/findShopByLicense")
    public String findShopByLicense(String license){
        Shop shop = shopMapper.findShopByLicense(license);
        if(shop!=null){
            return "exist";
        }else {
            return "none";
        }
    }

    /*根据店长id查询店铺*/
    @RequestMapping("/findShopByUid")
    @AddRedis
    public String findShopByUid(String uid){
        System.out.println(uid);
        Shop shop = shopMapper.findShopByUid(uid);
        return JSON.toJSONString(shop,SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue);
    }


    /*模糊查询店铺或商品*/
    @RequestMapping("/findGoodsAndShopsByValue")
    @AddRedis
    public String findGoodsAndShopsByValue(String value){
        //System.out.println(value);
//        List<Shop> shops = shopService.findGoodsAndShopsByValue(value);
        //1、模糊查询店铺名
        List<Shop> shops = shopMapper.findShopsByShopNameLike(value);
        //2、模糊查询商品，从而找到对应的店铺
        List<Good> goods = goodMapper.findGoodByNameLike(value);
        for (Good good : goods) {
            Shop shop = shopMapper.findShopBySid(good.getSid());
            //店铺是否是经营状态并且在shops里不存在
            if(shop.getStat()==1&&!shops.contains(shop)){
                shops.add(shop);
            }
        }

        System.out.println("shops json:"+JSON.toJSONString(shops));
        return JSON.toJSONString(shops, SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue);
    }

    /*修改店铺信息*/
    @RequestMapping("/updateShopMessage")
    public String updateShopMessage(@RequestBody Shop shop){
        System.out.println("shop:"+shop);
//        boolean flag = shopService.updateShopMessage(shop);
        Integer row = shopMapper.updateShopMessage(shop);
        if(row>0){
            return "success";
        }
        return "fail";
    }

    /*分页根据店长id获取店铺商品*/
    @RequestMapping("/findGoodsAndCategoryByPage")
    @AddRedis
    public String findGoodsAndCategoryByPage(String uid,String currentPage,String pageSize){
//        HashMap<String, Object> hashMap = shopService.findGoodsAndCategoryByPage(uid, currentPage, Integer.parseInt(pageSize));

        Shop shop = shopMapper.findShopByUid(uid);
        //根据店铺找到类别
        //1、根据sid获取所有的goods数量
        Integer count = goodMapper.findGoodCountBySid(shop.getSid());

        //2、根据sid分页获取good
        PageTool pageTool = new PageTool(count, currentPage, Integer.parseInt(pageSize));
        List<GoodInfoVO> goods = goodMapper.findGoodByPage(shop.getSid(), pageTool.getStartIndex(), pageTool.getPageSize());
        //3、根据good的cid找到对应的类别
        for (GoodInfoVO good:goods) {
            //4、找到对应good的类别
            Category category = categoryMapper.findCategoryByCid(good.getCid());
            good.setCategory(category.getCategoryName());
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("goods", goods);
        hashMap.put("totalCount",count);

        if((int)hashMap.get("totalCount")!=0){
            return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
        }
        return null;
    }

    /*修改商品信息*/
    @RequestMapping("/updateGoodMessage")
    public String updateGoodMessage(@RequestBody GoodInfoVO goodInfoVO){
        //System.out.println("goodInfoVO:"+goodInfoVO);
//        boolean flag = shopService.updateGoodMessage(goodInfoVO);
        Integer row = goodMapper.updateGoodMessage(goodInfoVO);
        if(row>0){
            return "success";
        }
        return "fail";
    }

    /*新增商品*/
    @RequestMapping("/addGoodMessage")
    public String addGoodMessage(@RequestBody Good good){
//        boolean flag = shopService.addGoodMessage(good);
        try {
            Integer row = goodMapper.addGoodMessage(good);
            if(row>0){
                return "success";
            }
            return "fail";
        }catch (Exception e){
            return "fail";
        }
    }


}
