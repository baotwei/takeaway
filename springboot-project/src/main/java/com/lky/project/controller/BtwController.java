package com.lky.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lky.project.VO.*;
import com.lky.project.mapper.*;
import com.lky.project.pojo.*;
import com.lky.project.response.Result;
import com.lky.project.utils.DateUtil;
import com.lky.project.utils.PageTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BtwController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private  CategoryMapper categoryMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderGoodMapper orderGoodMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Autowired
    private ShopManagerMapper shopManagerMapper;

    private DateUtil dateUtil=new DateUtil();
    String loginCode;



    //UserController----------------------------------------------------------------------------------------------------------------
//    @RequestMapping("/login")
//    public String userLogin(String username,String password){
//        User user=userMapper.findUserByUsername(username);
//        Result<Object> result=new Result<>();
//        if (user.getPassword().equals(password)){
//            result.setCode(200);
//            result.setMessage("xxxxx");
//            Map<String,Object> map=new HashMap<>();
//            map.put("user",user);
//            result.setData(map);
//        }else {
//            result.setCode(401);
//            result.setData(null);
//            result.setMessage("error");
//        }
//
//        return JSON.toJSONString(result);
//    }
//
//    /*根据用户名查找用户是否存在*/
//    @RequestMapping("/findUserByUsername")
//    public String findUserByUsername(String username){
//        User user =  userMapper.findUserByUsername(username);
//        if(user!=null){
//            return "exist";
//        }else {
//            return "none";
//        }
//    }
//
//    /*根据id获取用户*/
//    @RequestMapping("/findUserById")
//    public String findUserById(String id){
//        User user = userMapper.findUserByUid(Integer.valueOf(id));
//        return JSON.toJSONString(user,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*根据用户手机号查找用户是否存在*/
//    @RequestMapping("/findUserByPhone")
//    public String findUserByPhone(String phone){
//        User user = userMapper.findUserByPhone(phone);
//        if(user!=null){
//            return "exist";
//        }else {
//            return "none";
//        }
//    }
//
//    /*修改用户信息*/
//    @RequestMapping("updateUserMessage")
//    public String updateUserMessage(@RequestBody User user){
//        System.out.println(user);
////        boolean flag = userService.updateUserMessage(user);
//        Integer row = userMapper.updateUserMessage(user);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//
//    /*发送code*/
//    @RequestMapping("/sendCodeUser")
//    public String sendCodeUser(String phone){
//        //int code = SendSms.sendCode(phone);
//        int code = 123;
//        if (code == 0) {
//            return "fail";
//        } else {
//            loginCode = phone + "_" + code;
//            /*System.out.println("存进session的code："+loginCode);
//            session.setAttribute("loginCode", loginCode);*/
//            return "success";
//        }
//    }
//
//    /*用户验证码登录*/
//    @RequestMapping("/loginByPhone")
//    public String loginByPhone(String phone,String code){
//        String newCode = phone+"_"+code;
//        System.out.println("newCode:"+newCode);
//        /*String loginCode = (String) session.getAttribute("loginCode");
//        System.out.println("loginCode:"+loginCode);*/
//        if(newCode.equals(loginCode)){
//            User user = userMapper.findUserByPhone(phone);
//            if ("1".equals(user.getStat())) {
//                //登陆成功
//                Map<String, Object> map = new HashMap<>();
//                map.put("user", user);
//                return JSON.toJSONString(new Result().setCode(200).setMessage("登陆成功").setData(map), SerializerFeature.WriteMapNullValue);
//            } else{
//                return JSON.toJSONString(new Result().setCode(401).setMessage("您的账号由于特殊原因处于冻结状态，详情请联系管理员"));
//            }
//        }else {
//            return "fail";
//        }
//    }
//
//
//    /*用户注册*/
//    @RequestMapping("/userRegister")
//    public String userRegister(@RequestBody User user){
//        System.out.println("user:"+user);
////        boolean flag = userService.userRegister(user);
//        Integer row = userMapper.userRegister(user);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//
//
//
//
//
////    /*店家登录*/
//    @RequestMapping("/shopManagerLogin")
//    public String shopManagerLogin(String username,String password){
//        System.out.println(username);
//        System.out.println(password);
//        ShopManager shopManager =  shopManagerMapper.findShopManagerByUsername(username);
//        if(shopManager!=null&&shopManager.getPassword().equals(password)){
//            if(shopManager.getStat()==1){
//                //登陆成功
//                Map<String, Object> map = new HashMap<>();
//                map.put("user",shopManager);
//                return JSON.toJSONString(new Result().setCode(200).setMessage("登陆成功").setData(map));
//            }else {
//                return JSON.toJSONString(new Result().setCode(401).setMessage("您的账号由于特殊原因处于冻结状态，详情请联系管理员"));
//            }
//
//        }else{//账号错误或密码错误
//            return JSON.toJSONString(new Result().setCode(401).setMessage("账号或密码错误！！！"));
//        }
//    }
//
//    /*管理员登录*/
//    @RequestMapping("/managerLogin")
//    public String managerLogin(String username,String password){
//        User user = userMapper.findManagerByUsername(username);
//        if(user!=null&&user.getPassword().equals(password)){
//            //登陆成功
//            Map<String, Object> map = new HashMap<>();
//            map.put("user",user);
//            return JSON.toJSONString(new Result().setCode(200).setMessage("登陆成功").setData(map));
//        }else{//账号错误或密码错误
//            return JSON.toJSONString(new Result().setCode(401).setMessage("账号或密码错误！！！"));
//        }
//    }
//
//
//    //商家注册
//    /*根据商家用户名查询是否存在*/
//    @RequestMapping("/findShopManagerByUsername")
//    public String findShopManagerByUsername(String username){
//        ShopManager shopManager = shopManagerMapper.findShopManagerByUsername(username);
//        if(shopManager!=null){
//            return "exist";
//        }else {
//            return "none";
//        }
//    }
//
//    /*根据身份证号查询店铺管理员是否存在*/
//    @RequestMapping("/findShopManagerByIdentityNumber")
//    public String findShopManagerByIdentityNumber(String identityNumber){
//        ShopManager shopManager = shopManagerMapper.findShopManagerByIdentityNumber(identityNumber);
//        if(shopManager!=null){
//            return "exist";
//        }else {
//            return "none";
//        }
//    }
//
//    /*根据手机号查询店铺管理员是否存在*/
//    @RequestMapping("/findShopManagerByPhone")
//    public String findShopManagerByPhone(String phone){
//        ShopManager shopManager = shopManagerMapper.findShopManagerByPhone(phone);
//        if(shopManager!=null){
//            return "exist";
//        }else {
//            return "none";
//        }
//    }
//
//    /*店铺管理人员注册和店铺注册*/
//    @RequestMapping("/shopManagerRegister")
//    public String shopManagerRegister(@RequestBody ShopManagerRegisterVO shopManagerRegisterInfo){
//        System.out.println("shopManagerRegisterInfo:"+shopManagerRegisterInfo);
////        boolean flag = shopManagerService.shopManagerRegisterInfo(shopManagerRegisterInfo);
////        System.out.println("shopManagerRegisterVO:"+shopManagerRegisterVO);
//        //1、添加新的店铺管理员
//        shopManagerMapper.shopManagerRegister(shopManagerRegisterInfo.getUserInfo());
//        //2、根据username查找刚添加的店铺管理员
//        ShopManager shopManager = shopManagerMapper.findShopManagerByUsername(shopManagerRegisterInfo.getUserInfo().getUsername());
//        //3、新增店铺
//        Shop shop = shopManagerRegisterInfo.getShopInfo();
//        shop.setUid(shopManager.getSmid());
//        Integer row = shopMapper.addShop(shop);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }

    //AddressController---------------------------------------------------------------------------------------
//    /*根据用户id查询所有address*/
//    @RequestMapping("/listAddress")
//    public String listAddress(String uid){
//        List<Address> addressList=addressMapper.listAllAddress(Integer.valueOf(uid));
//        return JSON.toJSONString(addressList);
//    }
//
//    /*根据用户id修改address*/
//    @RequestMapping ("/updateAddressById")
//    public String updateAddressById(@RequestBody Address address){
//        int row = addressMapper.updateAddressById(address);
//        if(row>0){
//            return "success";
//        }else {
//            return "fail";
//        }
//    }
//
//    /*添加address*/
//    @RequestMapping("/addAddress")
//    public String addAddress(@RequestBody AddressVO addressInfo){
//        System.out.println(addressInfo);
//        int row = addressMapper.addAddress(addressInfo);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//    //CategoryController------------------------------------------------------------------------------
//    /*获取所有类别*/
//    @RequestMapping("/getAllCategory")
//    public String getAllCategory(){
//        List<Category> allCategory = categoryMapper.findAllCategory();
//        if(allCategory!=null){
//            return JSON.toJSONString(allCategory);
//        }
//        return "none";
//    }
//
////
////    /*分页获取类别*/
//    @RequestMapping("/findCategoriesByPage")
//    public String findCategoriesByPage(String uid,String currentPage,String pageSize){
//        Shop shop = shopMapper.findShopByUid(uid);
//        Integer count = categoryMapper.getCategoryCountBySid(shop.getSid());
//        PageTool pageTool = new PageTool(count, currentPage, Integer.parseInt(pageSize));
//        List<Category> categories = categoryMapper.findCategoriesByPage(shop.getSid(), pageTool.getStartIndex(), pageTool.getPageSize());
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("categories",categories);
//        hashMap.put("count",count);
//        return JSON.toJSONString(hashMap);
//    }
//
//    /*根据sid获取对应的商品类别*/
//    @RequestMapping("/getAllCategoryBySid")
//    public String getAllCategoryBySid(String sid){
//        List<Category> categories = categoryMapper.getAllCategoryBySid(sid);
//        return JSON.toJSONString(categories);
//    }
//
//    /*查询类别名是否存在*/
//    @RequestMapping("/findCategoryNameBySid")
//    public String findCategoryNameBySid(String categoryName,String sid){
//        Category category = categoryMapper.findCategoryNameBySid(categoryName,sid);
//        if(category!=null){
//            return "exist";
//        }
//        return "none";
//    }
//
////    /*保存修改后的类别*/
//    @RequestMapping("/updateCategoryMessage")
//    public String updateCategoryMessage(@RequestBody Category category){
//        Integer row = categoryMapper.updateCategoryMessage(category);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//    /*新增类别*/
//    @RequestMapping("/addCategoryMessage")
//    public String addCategoryMessage(@RequestBody Category category){
//        Integer row = categoryMapper.addCategoryMessage(category);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }

//    DeliveryController----------------------------------------------------------------------------
//    /*根据oid获取taker*/
//    @RequestMapping("/findTakerByOid")
//    public String findTakerByOid(String oid){
//        System.out.println(oid);
//        Delivery deliveryMan = deliveryMapper.findTakerByOid(Integer.valueOf(oid));
//        return JSON.toJSONString(deliveryMan);
//    }
//
//    /*检查phone是否已注册*/
//    @RequestMapping("/checkPhone")
//    public String checkPhone(String phone){
//        Delivery delivery = deliveryMapper.findDeliveryByPhone(phone);
//        if(delivery!=null){
//            return "exist";
//        }else {
//            return "none";
//        }
//    }
//
//    /*发送code*/
//    @RequestMapping("/sendCode")
//    public String sendCode(String phone){
//        //int code = SendSms.sendCode(phone);
//        int code = 123;
//        if (code == 0) {
//            return "fail";
//        } else {
//            loginCode = phone + "_" + code;
//            /*System.out.println("存进session的code："+loginCode);
//            session.setAttribute("loginCode", loginCode);*/
//            return "success";
//        }
//    }
//
//    /*骑手验证码登录*/
//    @RequestMapping("/riderCodeLogin")
//    public String riderCodeLogin(String phone,String code){
//        String newCode = phone+"_"+code;
//        System.out.println("newCode:"+newCode);
//        /*String loginCode = (String) session.getAttribute("loginCode");
//        System.out.println("loginCode:"+loginCode);*/
//        if(newCode.equals(loginCode)){
//            Delivery delivery = deliveryMapper.findDeliveryByPhone(phone);
//            //System.out.println(delivery);
//            return JSON.toJSONString(delivery);
//        }else {
//            return "fail";
//        }
//    }
//
//    /*根据did获取delivery*/
//    @RequestMapping("/findDeliveryByDid")
//    public String findDeliveryByDid(String did){
//        Delivery delivery = deliveryMapper.findTakerByOid(Integer.valueOf(did));
//        return JSON.toJSONString(delivery, SerializerFeature.WriteMapNullValue);
//    }

    //ManagerController--------------------------------------------------------------------------
//    /*查询所有店铺以及对应的店长*/
//    @RequestMapping("/findAllShopInfo")
//    public String findAllShopAndShopManager(String currentPage,String pageSize){
//        System.out.println("currentPage:"+currentPage);
//        System.out.println("pageSize:"+pageSize);
//        //首先查出所有的店铺（在开和冻结），然后根据uid获取对应的店长
////        ShopsVO shops = shopService.findAllShopAndShopManager(currentPage, Integer.valueOf(pageSize));
//        List<ShopManagerRegisterVO> shopManagerVOS = new ArrayList<>();
//        Integer count = shopMapper.getShopsCount();
//        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
//        System.out.println(pageTool.getStartIndex());
//        System.out.println(pageTool.getPageSize());
//        List<Shop> shopList = shopMapper.findAllShopByPage(pageTool.getStartIndex(),pageTool.getPageSize());
//        for (Shop shop : shopList) {
//            //根据uid获取店长信息
//            ShopManager shopManager = shopManagerMapper.findShopManagerBysmid(shop.getUid());
//            shopManagerVOS.add(new ShopManagerRegisterVO(shopManager,shop));
//        }
//        ShopsVO shops = new ShopsVO(shopManagerVOS,count);
//
//        System.out.println("shops:"+shops);
//        return JSON.toJSONString(shops,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*查询所有的待办理的店铺申请*/
//    @RequestMapping("/findAllShopApplyFor")
//    public String findAllShopApplyFor(String currentPage,String pageSize){
////        List<ShopManagerRegisterVO> allShopApplyFor = shopService.findAllShopApplyFor(currentPage, Integer.valueOf(pageSize));
//        List<ShopManagerRegisterVO> allShopApplyFor;
//        List<ShopManagerRegisterVO> shopManagerVOS = new ArrayList<>();
//        //获取待办理的店铺申请总数
//        Integer count = shopMapper.getShopApplyForCount();
//        if(count!=0){
//            PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
//            //获取待审核的店铺
//            List<Shop> shopList = shopMapper.findAllShopApplyForByPage(pageTool.getStartIndex(), pageTool.getPageSize());
//            for (Shop shop : shopList) {
//                //获取店铺的店长信息
//                ShopManager shopManager = shopManagerMapper.findShopManagerBysmid(shop.getUid());
//                shopManagerVOS.add(new ShopManagerRegisterVO(shopManager,shop));
//            }
//             allShopApplyFor =  shopManagerVOS;
//        }else {
//             allShopApplyFor =  null;
//        }
//
//        System.out.println("findAllShopApplyFor:"+allShopApplyFor);
//        if(allShopApplyFor!=null){
//            return JSON.toJSONString(allShopApplyFor);
//        }else {
//            return "none";
//        }
//
//    }
//
//    /*获取所有用户*/
//    @RequestMapping("/findAllUserByPage")
//    public String findAllUserByPage(String currentPage,String pageSize){
//        //UsersVO userVO = userService.findAllUserByPage(currentPage, Integer.valueOf(pageSize));
////        HashMap<String, Object> hashMap = userService.findAllUserByPage(currentPage, Integer.valueOf(pageSize));
//        //1、获取用户总数count
//        Integer count = userMapper.getUserCount();
//        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
//        //分页获取用户
//        List<User> users = userMapper.findAllUserByPage(pageTool.getStartIndex(), pageTool.getPageSize());
//        //System.out.println("users:"+users);
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("users",users);
//        hashMap.put("count",count);
//
//        //System.out.println("users:"+users);
//        //对象转换成json数据时，会自动忽略为null的值，如果有为null的值需要显示，需要加参数
//        //System.out.println("users:"+JSON.toJSONString(hashMap.get("users"), SerializerFeature.WriteMapNullValue));
//        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*分页查询所有骑手*/
//    @RequestMapping("/findAllRidersByPage")
//    public String findAllRidersByPage(String currentPage,String pageSize){
////        HashMap<String, Object> hashMap = deliveryService.findAllRidersByPage(currentPage, Integer.valueOf(pageSize));
//        Integer count = deliveryMapper.getRidersCount();
//        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
//        List<Delivery> deliveries = deliveryMapper.findAllRidersByPage(pageTool.getStartIndex(), pageTool.getPageSize());
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("deliveries",deliveries);
//        hashMap.put("count",count);
//
//        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*分页查询所有店铺管理人员*/
//    @RequestMapping("/findAllShopManagerByPage")
//    public String findAllShopManagerByPage(String currentPage,String pageSize){
////        HashMap<String, Object> hashMap = shopManagerService.findAllShopManagerByPage(currentPage, Integer.valueOf(pageSize));
//        //获取店铺管理人员总数
//        Integer count = shopManagerMapper.getShopManagerCount();
//        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
//        List<ShopManager> shopManagers = shopManagerMapper.findAllShopManagerByPage(pageTool.getStartIndex(), pageTool.getPageSize());
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("shopManagers",shopManagers);
//        hashMap.put("count",count);
//
//        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*保存修改的店铺信息*/
//    @RequestMapping("/saveShopInfo")
//    public String saveShopInfo(@RequestBody Shop shop){
//        //System.out.println(shop);
////        boolean flag = shopService.saveShopInfo(shop);
//        Integer row = shopMapper.saveShopInfo(shop);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//
//    }
//
//    /*修改店铺状态*/
//    @RequestMapping("/changeShopStat")
//    public String changeShopStat(String stat,String sid){
////        boolean flag = shopService.changeShopStat(stat, sid);
//        Integer row = shopMapper.changeShopStat(stat, sid);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//    /*同意店铺申请*/
//    @RequestMapping("/agreeShopApplyFor")
//    public String agreeShopApplyFor(String sid){
////        boolean flag = shopService.agreeShopApplyFor(sid);
//        Integer row = shopMapper.agreeShopApplyFor(sid);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }

//    OrderController----------------------------------------------------------------------------------------------------
//    /*创建订单*/
//    @PostMapping("/createOrder")
//    public String createOrder(@RequestBody OrderVO orderVO){
//        System.out.println("orderVO:"+orderVO);
////        Order order = orderService.createOrder(orderVO);
//        //1、构建order实体类，存储订单表
//        //1.1 存下该订单的订单号：DM + 时间戳 + 000 + 用户id
//        String orderNumber = "DM" +dateUtil.getTimeStamp()+"000" + orderVO.getUid();
//
//        Order order = new Order(null, orderVO.getUid(), orderVO.getSid(), orderVO.getAddressMessage().getAId(),null,
//                orderNumber, orderVO.getGoodTotalPrice(),orderVO.getDeliveryPrice(),
//                orderVO.getTotalPrice(), dateUtil.getNowTime(), null);
//        System.out.println("order:"+order);
//        //1.2 持久化订单信息
//        Integer flag = orderMapper.addOrder(order);
//        if(flag>0){
//            //2、存储订单商品表
//            //2.1 根据存储的订单号找到刚存储的order
//            Order order1 = orderMapper.findOrderByOrderNumber(orderNumber);
//            //2.2 遍历存储OrderGood表
//            int count = 0;
//            for (Good good : orderVO.getGoods()) {
//                OrderGood orderGood = new OrderGood(null, order1.getOid(), good.getGid(), good.getCount());
//                int row = orderMapper.addOrderGood(orderGood);
//                count+=row;
//            }
//            System.out.println("成功存储"+count+"条订单商品信息");
//
//            System.out.println(JSON.toJSONString(order1));
//            return JSON.toJSONString(order1);
//        }
//        return "fail";
//
//    }
//
//    /*根据uid查找所有订单以及所需的数据*/
//    @GetMapping("/findAllOrderByUid")
//    public String findAllOrderByUid(String uid){
//        //System.out.println(uid);
//        //System.out.println(JSON.toJSONString(orderService.findAllOrderByUid(Integer.valueOf(uid))));
//        Integer id = Integer.valueOf(uid);
//
//        /*前端所需数据：  oid:'',
//                        sid:'',
//                        logoSrc:'/pic/kfcLogo.png',
//                        shopName:'华莱士·全鸡汉堡',
//                        orderStat:1,
//                        goodTotalPrice,
//                        totalPrice:'50',
//                        createTime:'2021.8.9',
//                        addressMessage:'',
//                        goods:'',
//                        orderNumber:'',
//                        deliveryPrice:''
//                        dmId:''
//                        */
//        //1、根据uid获取所有的order
//        /*可以获取的数据：  oid:'',
//                          sid:'',
//                            aid:'',
//                        orderStat:1,
//                        totalPrice:'50',
//                        createTime:'2021.8.9',
//                        goodTotalPrice:'',
//                        orderNumber:'',
//                        deliveryPrice:''*/
//        List<OrderInfoVO> orderInfoVOS = new ArrayList<>();
//        List<Order> orders = orderMapper.findAllOrdersByUid(id);
//        System.out.println("orders:"+orders);
//        for (Order order : orders) {
//            //2、根据aid获取到具体的地址信息
//            Address address = addressMapper.findAddressByAid(order.getAid());
//
//            //3、根据sid获取到具体的店铺信息
//            Shop shop = shopMapper.findShopBySid(order.getSid());
//
//            //4、根据oid获取该订单所有的good
//            List<OrderGood> orderGoods = orderMapper.findAllGoodsByOid(order.getOid());
//            List<Good> goods = new ArrayList<>();
//            for (OrderGood orderGood : orderGoods) {
//                //给good获取对应的count
//                Good good = goodMapper.findGoodByGid(orderGood.getGid());
//                good.setCount(orderGood.getCount());
//                goods.add(good);
//            }
//            //5、OrderInfoVO对象数据组合
//            OrderInfoVO orderInfoVO = new OrderInfoVO(order.getOid(), shop.getSid(), shop.getLogoSrc(), shop.getShopName(),
//                    order.getOrderStat(), order.getGoodTotalPrice(), order.getDeliveryPrice(),
//                    order.getTotalPrice(), order.getCreateTime(), goods, address, order.getOrderNumber());
//            orderInfoVOS.add(orderInfoVO);
//        }
//        System.out.println(orderInfoVOS);
//
//        return JSON.toJSONString(orderInfoVOS);
//    }
//
//    /*获取所有待接单的订单*/
//    @RequestMapping("/getFreeOrders")
//    public String getFreeOrders(){
////        List<OrderFreeVO> freeOrders = orderService.getFreeOrders();
//        List<OrderFreeVO> list = new ArrayList<>();
//
//        //1、获取所有状态为2的订单（1：已支付，待商家接单；2：商家已接单，待骑手接单；3：骑手已接单，待送达；4、已送达）
//        List<Order> orders = orderMapper.findOrderByOrderStat(2);
//
//        for (Order order : orders) {
//            //2、根据每个订单里的sid获取店铺信息
//            Shop shop = shopMapper.findShopBySid(order.getSid());
//            //3、根据aid获取地址信息
//            Address address = addressMapper.findAddressByAid(order.getAid());
//            //4、根据uid获取userName和phone
//            User user = userMapper.findUserByUid(order.getUid());
//            OrderFreeVO orderFreeVO = new OrderFreeVO(user.getUsername(), user.getPhone(), order, shop, address);
//            list.add(orderFreeVO);
//        }
//        List<OrderFreeVO> freeOrders = list;
//
//        System.out.println(freeOrders);
//        return JSON.toJSONString(freeOrders);
//    }
//
//    /*获取所有待商家接单的订单*/
//    @RequestMapping("/getAllNewOrderBySid")
//    public String getAllNewOrderBySid(String sid,String currentPage,String pageSize){
////        HashMap<String, Object> hashMap = orderService.getAllOrderBySidAndStat(sid, currentPage, Integer.valueOf(pageSize),1);
//        List<NewOrderInfoVO> list = new ArrayList<>();
//        //获取sid的所有待接订单的数量
//        Integer count = orderMapper.getOrderCountBySidAndStat(sid,1);
//        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
//        //根据sid获取到所有的订单,分页查询
//        List<Order> orders = orderMapper.findAllOrderBySidAndStat(sid, pageTool.getStartIndex(), pageTool.getPageSize(),1);
//        for (Order order : orders) {
//            //根据uid获取用户信息
//            User userInfo = userMapper.findUserByUid(order.getUid());
//            //根据aid获取收货地址
//            Address addressInfo = addressMapper.findAddressByAid(order.getAid());
//            //根据oid获取订单商品等信息
//            //获取对应的goodId
//            List<OrderGood> orderGoods = orderGoodMapper.findGidByOid(order.getOid());
//            List<Good> goodInfo = new ArrayList<>();
//            for (OrderGood orderGood : orderGoods) {
//                Good good = goodMapper.findGoodByGid(orderGood.getGid());
//                good.setCount(orderGood.getCount());
//                goodInfo.add(good);
//            }
//            //获取骑士信息
//            Delivery delivery = null;
//            if(order.getDid()!=null){
//                delivery = deliveryMapper.findDeliveryByDid(order.getDid());
//            }
//            //封装数据
//            NewOrderInfoVO newOrderInfoVO = new NewOrderInfoVO(userInfo, order,goodInfo, addressInfo,delivery);
//            list.add(newOrderInfoVO);
//        }
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("orders",list);
//        hashMap.put("totalCount",count);
//
//        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*获取所有待商家接单的订单*/
//    @RequestMapping("/getAllOldOrderBySid")
//    public String getAllOldOrderBySid(String sid,String currentPage,String pageSize){
////        HashMap<String, Object> hashMap = orderService.getAllOldOrderBySid(sid, currentPage, Integer.valueOf(pageSize));
//        List<NewOrderInfoVO> list = new ArrayList<>();
//        //获取sid的所有待接订单的数量
//        Integer count = orderMapper.getOldOrderCountBySid(sid);
//        PageTool pageTool = new PageTool(count, currentPage,Integer.valueOf(pageSize));
//        //根据sid获取到所有的订单,分页查询
//        List<Order> orders = orderMapper.findAllOldOrderBySid(sid, pageTool.getStartIndex(), pageTool.getPageSize());
//        for (Order order : orders) {
//            //根据uid获取用户信息
//            User userInfo = userMapper.findUserByUid(order.getUid());
//            //根据aid获取收货地址
//            Address addressInfo = addressMapper.findAddressByAid(order.getAid());
//            //根据oid获取订单商品等信息
//            //获取对应的goodId
//            List<OrderGood> orderGoods = orderGoodMapper.findGidByOid(order.getOid());
//            List<Good> goodInfo = new ArrayList<>();
//            for (OrderGood orderGood : orderGoods) {
//                Good good = goodMapper.findGoodByGid(orderGood.getGid());
//                good.setCount(orderGood.getCount());
//                goodInfo.add(good);
//            }
//            //获取骑士信息
//            Delivery delivery = null;
//            if(order.getDid()!=null){
//                delivery = deliveryMapper.findDeliveryByDid(order.getDid());
//            }
//            //封装数据
//            NewOrderInfoVO newOrderInfoVO = new NewOrderInfoVO(userInfo, order,goodInfo, addressInfo,delivery);
//            list.add(newOrderInfoVO);
//        }
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("orders",list);
//        hashMap.put("totalCount",count);
//
//        return JSON.toJSONString(hashMap, SerializerFeature.WriteMapNullValue);
//    }
//
//    /*商家接收订单*/
//    @RequestMapping("/shopPickOrder")
//    public String shopPickOrder(String oid){
////        boolean flag = orderService.shopPickOrder(oid);
//        Integer row = orderMapper.shopPickOrder(oid,dateUtil.getNowTime());
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//    /*骑手接受订单*/
//    @RequestMapping("/pickOrderByDid")
//    public String pickOrderByDid(String oid,String did){
////        boolean flag = orderService.pickOrderByDid(oid, did);
//        Integer row = orderMapper.pickOrderByDid(oid, did, dateUtil.getNowTime());
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//    /*获取骑手所接的单*/
//    @RequestMapping("/getAllOrderByDid")
//    public String getAllOrderByDid(String did){
////        List<OrderFreeVO> orders = orderService.getAllOrderByDid(did);
//        List<OrderFreeVO> list = new ArrayList<>();
//        //根据did获取所接的订单
//        List<Order> orders = orderMapper.findAllOrdersByDid(did);
//        //每个订单
//        for (Order order : orders) {
//            //获取用户信息
//            User user = userMapper.findUserByUid(order.getUid());
//
//            //获取地址信息
//            Address address = addressMapper.findAddressByAid(order.getAid());
//
//            //获取店铺信息
//            Shop shop = shopMapper.findShopBySid(order.getSid());
//            OrderFreeVO orderFreeVO = new OrderFreeVO(user.getUsername(), user.getPhone(), order, shop, address);
//            list.add(orderFreeVO);
//        }
//        List<OrderFreeVO> orderFreeVOList = list;
//
//        return JSON.toJSONString(orderFreeVOList,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*根据oid获取订单信息*/
//    @RequestMapping("/getOrderByOid")
//    public String getOrderByOid(String oid){
////        Order order = orderService.getOrderByOid(oid);
//        Order order = orderMapper.findOrderByOid(oid);
//        return JSON.toJSONString(order,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*骑手确认订单送达*/
//    @RequestMapping("/deliverySuccess")
//    public String deliverySuccess(String oid) {
//        System.out.println("oid:"+oid);
////        boolean flag = orderService.deliverySuccess(oid);
//
//        //改变订单状态
//        Integer row = orderMapper.deliverySuccess(oid, dateUtil.getNowTime());
//        //获取到订单
//        List<OrderGood> orderGoods = orderGoodMapper.findGidByOid(Integer.valueOf(oid));
//        //将订单里的商品的数量存入对应商品的销量里
//        for (OrderGood orderGood : orderGoods) {
//            //商品增加销量
//            goodMapper.addGoodSales(orderGood.getGid(),orderGood.getCount());
//        }
//        //店铺增加销量
//        shopMapper.addTotalSales(oid);
//        //骑手增加接单量
//        deliveryMapper.addTotalNum(oid);
//        //用户增加下单量
//        userMapper.addScore(oid);
//        System.out.println("row:"+row);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }

////    ShopController------------------------------------------------------------------------------------------------------------------
//    /*查询所有店铺*/
//    @RequestMapping("/findAllShop")
//    public String findAllShop() {
//        List<Shop> shops = shopMapper.findAllShop();
//        System.out.println(shops);
//        return JSON.toJSONString(shops);
//    }
//
//    @RequestMapping("/findShopOrderBySales")
//    public String findShopOrderBySales(){
//        List<Shop> shopList=shopMapper.findShopOrderBySales();
//        return  JSON.toJSONString(shopList);
//    }
//
//    @RequestMapping("/findShopBySid")
//    public String findShopBySid(Integer sid){
//        Shop shop=shopMapper.findShopBySid(sid);
//        return  JSON.toJSONString(shop);
//    }
//
//        /*根据店铺id获取商品*/
//    @GetMapping("/findAllGoodsBySid")
//    public String findAllGoodsBySid(String sid){
//        //System.out.println(sid);
////        List<GoodVO> goods = shopService.findAllGoodsBySid(sid);
//        List<GoodVO> goodList = new ArrayList<>();
//        //1、根据sid获取所有的category
//        List<Category> categories = shopMapper.findAllCategoryBySid(sid);
//        //System.out.println("categories:"+categories);
//        //2、根据category的id找到其下的所有good
//        for (Category category : categories) {
//            List<Good> goods = shopMapper.findAllGoodByCid(category.getCid());
//            //System.out.println("goods:"+goods);
//            //3、将categoryName和其下的good放进goodVO对象里
//            GoodVO goodVO = new GoodVO(category.getCategoryName(), goods);
//            goodList.add(goodVO);
//        }
//        //System.out.println(goodList);
//        //System.out.println(JSON.toJSONString(goodList));
//
//        System.out.println(goodList);
//        return JSON.toJSONString(goodList);
//    }
//
//
//
//    /*获取各个商品的销量*/
//    @RequestMapping("/getGoodData")
//    public String getGoodData(String sid){
//        List<Good> goodList = goodMapper.findAllGoodBySid(sid);
//        List<GoodDataVO> list = new ArrayList<>();
//        for (Good good : goodList) {
//            GoodDataVO goodDataVO = new GoodDataVO();
//            goodDataVO.setName(good.getGoodName());
//            goodDataVO.setValue(good.getSales());
//            list.add(goodDataVO);
//        }
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("goods",list);
//        return JSON.toJSONString(hashMap);
//    }
//
//    /*根据店铺license查询店铺是否存在*/
//    @RequestMapping("/findShopByLicense")
//    public String findShopByLicense(String license){
//        Shop shop = shopMapper.findShopByLicense(license);
//        if(shop!=null){
//            return "exist";
//        }else {
//            return "none";
//        }
//    }
//
//    /*根据店长id查询店铺*/
//    @RequestMapping("/findShopByUid")
//    public String findShopByUid(String uid){
//        System.out.println(uid);
//        Shop shop = shopMapper.findShopByUid(uid);
//        return JSON.toJSONString(shop,SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue);
//    }
//
//
//    /*模糊查询店铺或商品*/
//    @RequestMapping("/findGoodsAndShopsByValue")
//    public String findGoodsAndShopsByValue(String value){
//        //System.out.println(value);
////        List<Shop> shops = shopService.findGoodsAndShopsByValue(value);
//        //1、模糊查询店铺名
//        List<Shop> shops = shopMapper.findShopsByShopNameLike(value);
//        //2、模糊查询商品，从而找到对应的店铺
//        List<Good> goods = goodMapper.findGoodByNameLike(value);
//        for (Good good : goods) {
//            Shop shop = shopMapper.findShopBySid(good.getSid());
//            //店铺是否是经营状态并且在shops里不存在
//            if(shop.getStat()==1&&!shops.contains(shop)){
//                shops.add(shop);
//            }
//        }
//
//        System.out.println("shops json:"+JSON.toJSONString(shops));
//        return JSON.toJSONString(shops, SerializerFeature.PrettyFormat,SerializerFeature.WriteMapNullValue);
//    }
//
//    /*修改店铺信息*/
//    @RequestMapping("/updateShopMessage")
//    public String updateShopMessage(@RequestBody Shop shop){
//        System.out.println("shop:"+shop);
////        boolean flag = shopService.updateShopMessage(shop);
//        Integer row = shopMapper.updateShopMessage(shop);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//    /*分页根据店长id获取店铺商品*/
//    @RequestMapping("/findGoodsAndCategoryByPage")
//    public String findGoodsAndCategoryByPage(String uid,String currentPage,String pageSize){
////        HashMap<String, Object> hashMap = shopService.findGoodsAndCategoryByPage(uid, currentPage, Integer.parseInt(pageSize));
//
//        Shop shop = shopMapper.findShopByUid(uid);
//        //根据店铺找到类别
//        //1、根据sid获取所有的goods数量
//        Integer count = goodMapper.findGoodCountBySid(shop.getSid());
//
//        //2、根据sid分页获取good
//        PageTool pageTool = new PageTool(count, currentPage, Integer.parseInt(pageSize));
//        List<GoodInfoVO> goods = goodMapper.findGoodByPage(shop.getSid(), pageTool.getStartIndex(), pageTool.getPageSize());
//        //3、根据good的cid找到对应的类别
//        for (GoodInfoVO good:goods) {
//            //4、找到对应good的类别
//            Category category = categoryMapper.findCategoryByCid(good.getCid());
//            good.setCategory(category.getCategoryName());
//        }
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("goods", goods);
//        hashMap.put("totalCount",count);
//
//        if((int)hashMap.get("totalCount")!=0){
//            return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
//        }
//        return null;
//    }
//
//    /*修改商品信息*/
//    @RequestMapping("/updateGoodMessage")
//    public String updateGoodMessage(@RequestBody GoodInfoVO goodInfoVO){
//        //System.out.println("goodInfoVO:"+goodInfoVO);
////        boolean flag = shopService.updateGoodMessage(goodInfoVO);
//        Integer row = goodMapper.updateGoodMessage(goodInfoVO);
//        if(row>0){
//            return "success";
//        }
//        return "fail";
//    }
//
//    /*新增商品*/
//    @RequestMapping("/addGoodMessage")
//    public String addGoodMessage(@RequestBody Good good){
////        boolean flag = shopService.addGoodMessage(good);
//        try {
//            Integer row = goodMapper.addGoodMessage(good);
//            if(row>0){
//                return "success";
//            }
//            return "fail";
//        }catch (Exception e){
//            return "fail";
//        }
//    }


}
