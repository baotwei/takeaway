package com.lky.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    //注入RedisTemplate
    @Autowired
    private RedisTemplate redisTemplate;

    //添加数据
    @PostMapping("/addData/key={key}/value={value}")//动态地址
    public Object addData(@PathVariable("key") String key,
                          @PathVariable("value") String value){
        redisTemplate.opsForValue().set(key,value);
        System.out.println("数据添加成功"+redisTemplate);
        return "addData Success";
    }

    //根据key获取value
    @GetMapping("/getData/key={key}")
    public Object getData(@PathVariable("key") String key){
        Object value =  redisTemplate.opsForValue().get(key);
        System.out.println("数据为："+value);
        return redisTemplate.opsForValue().get(key);
    }

}
