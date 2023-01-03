package com.lky.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lky.project.mapper.CategoryMapper;
import com.lky.project.mapper.DeliveryMapper;
import com.lky.project.mapper.ShopMapper;
import com.lky.project.pojo.Category;

import com.lky.project.pojo.Delivery;
import com.lky.project.pojo.Shop;
import com.lky.project.utils.AddRedis;
import com.lky.project.utils.PageTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /*获取所有类别*/
    @RequestMapping("/getAllCategory")
    @AddRedis
    public String getAllCategory(){
        List<Category> allCategory = categoryMapper.findAllCategory();
        if(allCategory!=null){
            return JSON.toJSONString(allCategory);
        }
        return "none";
    }

    //
//    /*分页获取类别*/
    @RequestMapping("/findCategoriesByPage")
    @AddRedis
    public String findCategoriesByPage(String uid,String currentPage,String pageSize){
        Shop shop = shopMapper.findShopByUid(uid);
        Integer count = categoryMapper.getCategoryCountBySid(shop.getSid());
        PageTool pageTool = new PageTool(count, currentPage, Integer.parseInt(pageSize));
        List<Category> categories = categoryMapper.findCategoriesByPage(shop.getSid(), pageTool.getStartIndex(), pageTool.getPageSize());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("categories",categories);
        hashMap.put("count",count);
        return JSON.toJSONString(hashMap);
    }

    /*根据sid获取对应的商品类别*/
    @RequestMapping("/getAllCategoryBySid")
    @AddRedis
    public String getAllCategoryBySid(String sid){
        List<Category> categories = categoryMapper.getAllCategoryBySid(sid);
        return JSON.toJSONString(categories);
    }

    /*查询类别名是否存在*/
    @RequestMapping("/findCategoryNameBySid")
    @AddRedis
    public String findCategoryNameBySid(String categoryName,String sid){
        Category category = categoryMapper.findCategoryNameBySid(categoryName,sid);
        if(category!=null){
            return "exist";
        }
        return "none";
    }

    //    /*保存修改后的类别*/
    @RequestMapping("/updateCategoryMessage")
    public String updateCategoryMessage(@RequestBody Category category){
        Integer row = categoryMapper.updateCategoryMessage(category);
        if(row>0){
            return "success";
        }
        return "fail";
    }

    /*新增类别*/
    @RequestMapping("/addCategoryMessage")
    public String addCategoryMessage(@RequestBody Category category){
        Integer row = categoryMapper.addCategoryMessage(category);
        if(row>0){
            return "success";
        }
        return "fail";
    }
}




