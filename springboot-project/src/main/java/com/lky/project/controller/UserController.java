package com.lky.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lky.project.VO.ShopManagerRegisterVO;
import com.lky.project.mapper.ShopManagerMapper;
import com.lky.project.mapper.ShopMapper;
import com.lky.project.mapper.UserMapper;
import com.lky.project.pojo.Shop;
import com.lky.project.pojo.ShopManager;
import com.lky.project.pojo.User;
import com.lky.project.response.Result;
import com.lky.project.utils.AddRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopManagerMapper shopManagerMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    String loginCode;

    @RequestMapping("/login")
    public String userLogin(String username,String password){
        User user=userMapper.findUserByUsername(username);
        Result<Object> result=new Result<>();
        if (user.getPassword().equals(password)){
            result.setCode(200);
            result.setMessage("xxxxx");
            Map<String,Object> map=new HashMap<>();
            map.put("user",user);
            result.setData(map);
        }else {
            result.setCode(401);
            result.setData(null);
            result.setMessage("error");
        }

        return JSON.toJSONString(result);
    }

    /*根据用户名查找用户是否存在*/
    @RequestMapping("/findUserByUsername")
    public String findUserByUsername(String username){
        User user =  userMapper.findUserByUsername(username);
        if(user!=null){
            return "exist";
        }else {
            return "none";
        }
    }

    /*根据id获取用户*/
//    @RequestMapping("/findUserById")
//    public String findUserById(String id){
////        User user = userMapper.findUserByUid(Integer.valueOf(id));
////        return JSON.toJSONString(user,SerializerFeature.WriteMapNullValue);
//        User user;
//        String key = "user_" + id;
//        ValueOperations<String,User> operations = redisTemplate.opsForValue();
//        //判断redis中是否有键为key的缓存
//        boolean haskey = redisTemplate.hasKey(key);
//        if (haskey){
//             user = operations.get(key);
//            System.out.println("从缓存中获取的数据："+user.getUsername());
//            System.out.println("----------------------------------");
//
//        }else {
//            user = userMapper.findUserByUid(Integer.valueOf(id));
//            System.out.println("查询数据库中的数据"+ user.getUsername());
//            System.out.println("--------------------写入数据------------------");
//            //写入数据
//            operations.set(key,user,5, TimeUnit.HOURS);
//
//        }
//        return JSON.toJSONString(user,SerializerFeature.WriteMapNullValue);
//    }
    @RequestMapping("/findUserById")
    @AddRedis
    public String findUserById(String id){
        User user = userMapper.findUserByUid(Integer.valueOf(id));
        return JSON.toJSONString(user,SerializerFeature.WriteMapNullValue);

    }



    /*根据用户手机号查找用户是否存在*/
    @RequestMapping("/findUserByPhone")
    public String findUserByPhone(String phone){
        User user = userMapper.findUserByPhone(phone);
        if(user!=null){
            return "exist";
        }else {
            return "none";
        }
    }

    /*修改用户信息*/
    @RequestMapping("updateUserMessage")
    public String updateUserMessage(@RequestBody User user){
        System.out.println(user);
//        boolean flag = userService.updateUserMessage(user);
        Integer row = userMapper.updateUserMessage(user);
        if(row>0){
            return "success";
        }
        return "fail";
    }


    /*发送code*/
    @RequestMapping("/sendCodeUser")
    public String sendCodeUser(String phone){
        //int code = SendSms.sendCode(phone);
        int code = 123;
        if (code == 0) {
            return "fail";
        } else {
            loginCode = phone + "_" + code;
            /*System.out.println("存进session的code："+loginCode);
            session.setAttribute("loginCode", loginCode);*/
            return "success";
        }
    }

    /*用户验证码登录*/
    @RequestMapping("/loginByPhone")
    public String loginByPhone(String phone,String code){
        String newCode = phone+"_"+code;
        System.out.println("newCode:"+newCode);
        /*String loginCode = (String) session.getAttribute("loginCode");
        System.out.println("loginCode:"+loginCode);*/
        if(newCode.equals(loginCode)){
            User user = userMapper.findUserByPhone(phone);
            if ("1".equals(user.getStat())) {
                //登陆成功
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                return JSON.toJSONString(new Result().setCode(200).setMessage("登陆成功").setData(map), SerializerFeature.WriteMapNullValue);
            } else{
                return JSON.toJSONString(new Result().setCode(401).setMessage("您的账号由于特殊原因处于冻结状态，详情请联系管理员"));
            }
        }else {
            return "fail";
        }
    }


    /*用户注册*/
    @RequestMapping("/userRegister")
    public String userRegister(@RequestBody User user){
        System.out.println("user:"+user);
//        boolean flag = userService.userRegister(user);
        Integer row = userMapper.userRegister(user);
        if(row>0){
            return "success";
        }
        return "fail";
    }






    //    /*店家登录*/
    @RequestMapping("/shopManagerLogin")
    public String shopManagerLogin(String username,String password){
        System.out.println(username);
        System.out.println(password);
        ShopManager shopManager =  shopManagerMapper.findShopManagerByUsername(username);
        if(shopManager!=null&&shopManager.getPassword().equals(password)){
            if(shopManager.getStat()==1){
                //登陆成功
                Map<String, Object> map = new HashMap<>();
                map.put("user",shopManager);
                return JSON.toJSONString(new Result().setCode(200).setMessage("登陆成功").setData(map));
            }else {
                return JSON.toJSONString(new Result().setCode(401).setMessage("您的账号由于特殊原因处于冻结状态，详情请联系管理员"));
            }

        }else{//账号错误或密码错误
            return JSON.toJSONString(new Result().setCode(401).setMessage("账号或密码错误！！！"));
        }
    }

    /*管理员登录*/
    @RequestMapping("/managerLogin")
    public String managerLogin(String username,String password){
        User user = userMapper.findManagerByUsername(username);
        if(user!=null&&user.getPassword().equals(password)){
            //登陆成功
            Map<String, Object> map = new HashMap<>();
            map.put("user",user);
            return JSON.toJSONString(new Result().setCode(200).setMessage("登陆成功").setData(map));
        }else{//账号错误或密码错误
            return JSON.toJSONString(new Result().setCode(401).setMessage("账号或密码错误！！！"));
        }
    }


    //商家注册
    /*根据商家用户名查询是否存在*/
    @RequestMapping("/findShopManagerByUsername")
    public String findShopManagerByUsername(String username){
        ShopManager shopManager = shopManagerMapper.findShopManagerByUsername(username);
        if(shopManager!=null){
            return "exist";
        }else {
            return "none";
        }
    }

    /*根据身份证号查询店铺管理员是否存在*/
    @RequestMapping("/findShopManagerByIdentityNumber")
    public String findShopManagerByIdentityNumber(String identityNumber){
        ShopManager shopManager = shopManagerMapper.findShopManagerByIdentityNumber(identityNumber);
        if(shopManager!=null){
            return "exist";
        }else {
            return "none";
        }
    }

    /*根据手机号查询店铺管理员是否存在*/
    @RequestMapping("/findShopManagerByPhone")
    public String findShopManagerByPhone(String phone){
        ShopManager shopManager = shopManagerMapper.findShopManagerByPhone(phone);
        if(shopManager!=null){
            return "exist";
        }else {
            return "none";
        }
    }

    /*店铺管理人员注册和店铺注册*/
    @RequestMapping("/shopManagerRegister")
    public String shopManagerRegister(@RequestBody ShopManagerRegisterVO shopManagerRegisterInfo){
        System.out.println("shopManagerRegisterInfo:"+shopManagerRegisterInfo);
//        boolean flag = shopManagerService.shopManagerRegisterInfo(shopManagerRegisterInfo);
//        System.out.println("shopManagerRegisterVO:"+shopManagerRegisterVO);
        //1、添加新的店铺管理员
        shopManagerMapper.shopManagerRegister(shopManagerRegisterInfo.getUserInfo());
        //2、根据username查找刚添加的店铺管理员
        ShopManager shopManager = shopManagerMapper.findShopManagerByUsername(shopManagerRegisterInfo.getUserInfo().getUsername());
        //3、新增店铺
        Shop shop = shopManagerRegisterInfo.getShopInfo();
        shop.setUid(shopManager.getSmid());
        Integer row = shopMapper.addShop(shop);
        if(row>0){
            return "success";
        }
        return "fail";
    }

}
