package com.lky.project.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String testControler(){
        System.out.println("测试成功");
        return "测试成功";
    }
}

