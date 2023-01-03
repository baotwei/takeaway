package com.lky.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lky.project.VO.NewOrderInfoVO;
import com.lky.project.VO.OrderFreeVO;
import com.lky.project.VO.OrderInfoVO;
import com.lky.project.VO.OrderVO;
import com.lky.project.config.MessageListenerConfig;
import com.lky.project.config.MyAckReceiver;
import com.lky.project.mapper.*;
import com.lky.project.pojo.*;

import com.lky.project.utils.AddRedis;
import com.lky.project.utils.DateUtil;
import com.lky.project.utils.PageTool;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Administrator
 */
@RestController
public class OrderController {

    private DateUtil dateUtil=new DateUtil();

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderGoodMapper orderGoodMapper;
    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;  //使用RabbitTemplate,这提供了接收/发送等等方法

    //MQConfig
    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckReceiver myAckReceiver;//消息接收处理类

    /*创建订单*/
    @PostMapping("/createOrder")
    public String createOrder(@RequestBody OrderVO orderVO){
        System.out.println("orderVO:"+orderVO);
//        Order order = orderService.createOrder(orderVO);
        //1、构建order实体类，存储订单表
        //1.1 存下该订单的订单号：DM + 时间戳 + 000 + 用户id
        String orderNumber = "DM" +dateUtil.getTimeStamp()+"000" + orderVO.getUid();

        Order order = new Order(null, orderVO.getUid(), orderVO.getSid(), orderVO.getAddressMessage().getAId(),null,
                orderNumber, orderVO.getGoodTotalPrice(),orderVO.getDeliveryPrice(),
                orderVO.getTotalPrice(), dateUtil.getNowTime(), null);
        System.out.println("order:"+order);
        //1.2 持久化订单信息
        Integer flag = orderMapper.addOrder(order);
        if(flag>0){
            //2、存储订单商品表
            //2.1 根据存储的订单号找到刚存储的order
            Order order1 = orderMapper.findOrderByOrderNumber(orderNumber);
            //2.2 遍历存储OrderGood表
            int count = 0;
            for (Good good : orderVO.getGoods()) {
                OrderGood orderGood = new OrderGood(null, order1.getOid(), good.getGid(), good.getCount());
                int row = orderMapper.addOrderGood(orderGood);
                count+=row;
            }
            System.out.println("成功存储"+count+"条订单商品信息");
            System.out.println(JSON.toJSONString(order1));

            String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Map<String, Object> map = new HashMap<>();
            map.put("messageId", orderNumber);
            map.put("messageData", order1);
            map.put("createTime", createTime);
            //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
            rabbitTemplate.convertAndSend("OrderInfoDirectExchange", "OrderInfoDirectRouting", map);
            System.out.println("订单商品信息推送成功------------");

            return JSON.toJSONString(order1);
        }
        return "fail";

    }

    /*根据uid查找所有订单以及所需的数据*/
    @GetMapping("/findAllOrderByUid")
    @AddRedis
    public String findAllOrderByUid(String uid){
        //System.out.println(uid);
        //System.out.println(JSON.toJSONString(orderService.findAllOrderByUid(Integer.valueOf(uid))));
        Integer id = Integer.valueOf(uid);

        /*前端所需数据：  oid:'',
                        sid:'',
                        logoSrc:'/pic/kfcLogo.png',
                        shopName:'华莱士·全鸡汉堡',
                        orderStat:1,
                        goodTotalPrice,
                        totalPrice:'50',
                        createTime:'2021.8.9',
                        addressMessage:'',
                        goods:'',
                        orderNumber:'',
                        deliveryPrice:''
                        dmId:''
                        */
        //1、根据uid获取所有的order
        /*可以获取的数据：  oid:'',
                          sid:'',
                            aid:'',
                        orderStat:1,
                        totalPrice:'50',
                        createTime:'2021.8.9',
                        goodTotalPrice:'',
                        orderNumber:'',
                        deliveryPrice:''*/
        List<OrderInfoVO> orderInfoVOS = new ArrayList<>();
        List<Order> orders = orderMapper.findAllOrdersByUid(id);
        System.out.println("orders:"+orders);
        for (Order order : orders) {
            //2、根据aid获取到具体的地址信息
            Address address = addressMapper.findAddressByAid(order.getAid());

            //3、根据sid获取到具体的店铺信息
            Shop shop = shopMapper.findShopBySid(order.getSid());

            //4、根据oid获取该订单所有的good
            List<OrderGood> orderGoods = orderMapper.findAllGoodsByOid(order.getOid());
            List<Good> goods = new ArrayList<>();
            for (OrderGood orderGood : orderGoods) {
                //给good获取对应的count
                Good good = goodMapper.findGoodByGid(orderGood.getGid());
                good.setCount(orderGood.getCount());
                goods.add(good);
            }
            //5、OrderInfoVO对象数据组合
            OrderInfoVO orderInfoVO = new OrderInfoVO(order.getOid(), shop.getSid(), shop.getLogoSrc(), shop.getShopName(),
                    order.getOrderStat(), order.getGoodTotalPrice(), order.getDeliveryPrice(),
                    order.getTotalPrice(), order.getCreateTime(), goods, address, order.getOrderNumber());
            orderInfoVOS.add(orderInfoVO);
        }
        System.out.println(orderInfoVOS);

        return JSON.toJSONString(orderInfoVOS);
    }

    /*获取所有待接单的订单*/
    @RequestMapping("/getFreeOrders")
    @AddRedis
    public String getFreeOrders(){
//        List<OrderFreeVO> freeOrders = orderService.getFreeOrders();
        List<OrderFreeVO> list = new ArrayList<>();

        //1、获取所有状态为2的订单（1：已支付，待商家接单；2：商家已接单，待骑手接单；3：骑手已接单，待送达；4、已送达）
        List<Order> orders = orderMapper.findOrderByOrderStat(2);

        for (Order order : orders) {
            //2、根据每个订单里的sid获取店铺信息
            Shop shop = shopMapper.findShopBySid(order.getSid());
            //3、根据aid获取地址信息
            Address address = addressMapper.findAddressByAid(order.getAid());
            //4、根据uid获取userName和phone
            User user = userMapper.findUserByUid(order.getUid());
            OrderFreeVO orderFreeVO = new OrderFreeVO(user.getUsername(), user.getPhone(), order, shop, address);
            list.add(orderFreeVO);
        }
        List<OrderFreeVO> freeOrders = list;

        System.out.println(freeOrders);
        return JSON.toJSONString(freeOrders);
    }

    /*获取所有待商家接单的订单*/
    @RequestMapping("/getAllNewOrderBySid")
    @AddRedis
    public String getAllNewOrderBySid(String sid,String currentPage,String pageSize){
//        HashMap<String, Object> hashMap = orderService.getAllOrderBySidAndStat(sid, currentPage, Integer.valueOf(pageSize),1);
        List<NewOrderInfoVO> list = new ArrayList<>();
        //获取sid的所有待接订单的数量
        Integer count = orderMapper.getOrderCountBySidAndStat(sid,1);
        PageTool pageTool = new PageTool(count, currentPage, Integer.valueOf(pageSize));
        //根据sid获取到所有的订单,分页查询
        List<Order> orders = orderMapper.findAllOrderBySidAndStat(sid, pageTool.getStartIndex(), pageTool.getPageSize(),1);
        for (Order order : orders) {
            //根据uid获取用户信息
            User userInfo = userMapper.findUserByUid(order.getUid());
            //根据aid获取收货地址
            Address addressInfo = addressMapper.findAddressByAid(order.getAid());
            //根据oid获取订单商品等信息
            //获取对应的goodId
            List<OrderGood> orderGoods = orderGoodMapper.findGidByOid(order.getOid());
            List<Good> goodInfo = new ArrayList<>();
            for (OrderGood orderGood : orderGoods) {
                Good good = goodMapper.findGoodByGid(orderGood.getGid());
                good.setCount(orderGood.getCount());
                goodInfo.add(good);
            }
            //获取骑士信息
            Delivery delivery = null;
            if(order.getDid()!=null){
                delivery = deliveryMapper.findDeliveryByDid(order.getDid());
            }
            //封装数据
            NewOrderInfoVO newOrderInfoVO = new NewOrderInfoVO(userInfo, order,goodInfo, addressInfo,delivery);
            list.add(newOrderInfoVO);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orders",list);
        hashMap.put("totalCount",count);

        return JSON.toJSONString(hashMap,SerializerFeature.WriteMapNullValue);
    }

    /*获取所有待商家接单的订单*/
    @RequestMapping("/getAllOldOrderBySid")
    @AddRedis
    public String getAllOldOrderBySid(String sid,String currentPage,String pageSize){
//        HashMap<String, Object> hashMap = orderService.getAllOldOrderBySid(sid, currentPage, Integer.valueOf(pageSize));
        List<NewOrderInfoVO> list = new ArrayList<>();
        //获取sid的所有待接订单的数量
        Integer count = orderMapper.getOldOrderCountBySid(sid);
        PageTool pageTool = new PageTool(count, currentPage,Integer.valueOf(pageSize));
        //根据sid获取到所有的订单,分页查询
        List<Order> orders = orderMapper.findAllOldOrderBySid(sid, pageTool.getStartIndex(), pageTool.getPageSize());
        for (Order order : orders) {
            //根据uid获取用户信息
            User userInfo = userMapper.findUserByUid(order.getUid());
            //根据aid获取收货地址
            Address addressInfo = addressMapper.findAddressByAid(order.getAid());
            //根据oid获取订单商品等信息
            //获取对应的goodId
            List<OrderGood> orderGoods = orderGoodMapper.findGidByOid(order.getOid());
            List<Good> goodInfo = new ArrayList<>();
            for (OrderGood orderGood : orderGoods) {
                Good good = goodMapper.findGoodByGid(orderGood.getGid());
                good.setCount(orderGood.getCount());
                goodInfo.add(good);
            }
            //获取骑士信息
            Delivery delivery = null;
            if(order.getDid()!=null){
                delivery = deliveryMapper.findDeliveryByDid(order.getDid());
            }
            //封装数据
            NewOrderInfoVO newOrderInfoVO = new NewOrderInfoVO(userInfo, order,goodInfo, addressInfo,delivery);
            list.add(newOrderInfoVO);
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orders",list);
        hashMap.put("totalCount",count);

        return JSON.toJSONString(hashMap, SerializerFeature.WriteMapNullValue);
    }

    /*商家接收订单*/
    @RequestMapping("/shopPickOrder")
    public String shopPickOrder(String oid){
//        boolean flag = orderService.shopPickOrder(oid);

        Integer row = orderMapper.shopPickOrder(oid,dateUtil.getNowTime());
        if(row>0){
            return "success";
        }
        return "fail";
    }


    /*骑手接受订单*/
    @RequestMapping("/pickOrderByDid")
    public String pickOrderByDid(String oid,String did){
//        boolean flag = orderService.pickOrderByDid(oid, did);
        Integer row = orderMapper.pickOrderByDid(oid, did, dateUtil.getNowTime());
        if(row>0){
            return "success";
        }
        return "fail";
    }

    /*获取骑手所接的单*/
    @RequestMapping("/getAllOrderByDid")
    @AddRedis
    public String getAllOrderByDid(String did){
//        List<OrderFreeVO> orders = orderService.getAllOrderByDid(did);
        List<OrderFreeVO> list = new ArrayList<>();
        //根据did获取所接的订单
        List<Order> orders = orderMapper.findAllOrdersByDid(did);
        //每个订单
        for (Order order : orders) {
            //获取用户信息
            User user = userMapper.findUserByUid(order.getUid());

            //获取地址信息
            Address address = addressMapper.findAddressByAid(order.getAid());

            //获取店铺信息
            Shop shop = shopMapper.findShopBySid(order.getSid());
            OrderFreeVO orderFreeVO = new OrderFreeVO(user.getUsername(), user.getPhone(), order, shop, address);
            list.add(orderFreeVO);
        }
        List<OrderFreeVO> orderFreeVOList = list;

        return JSON.toJSONString(orderFreeVOList,SerializerFeature.WriteMapNullValue);
    }

    /*根据oid获取订单信息*/
    @RequestMapping("/getOrderByOid")
    @AddRedis
    public String getOrderByOid(String oid){
//        Order order = orderService.getOrderByOid(oid);
        Order order = orderMapper.findOrderByOid(oid);
        return JSON.toJSONString(order,SerializerFeature.WriteMapNullValue);
    }

    /*骑手确认订单送达*/
    @RequestMapping("/deliverySuccess")
    public String deliverySuccess(String oid) {
        System.out.println("oid:"+oid);
//        boolean flag = orderService.deliverySuccess(oid);

        //改变订单状态
        Integer row = orderMapper.deliverySuccess(oid, dateUtil.getNowTime());
        //获取到订单
        List<OrderGood> orderGoods = orderGoodMapper.findGidByOid(Integer.valueOf(oid));
        //将订单里的商品的数量存入对应商品的销量里
        for (OrderGood orderGood : orderGoods) {
            //商品增加销量
            goodMapper.addGoodSales(orderGood.getGid(),orderGood.getCount());
        }
        //店铺增加销量
        shopMapper.addTotalSales(oid);
        //骑手增加接单量
        deliveryMapper.addTotalNum(oid);
        //用户增加下单量
        userMapper.addScore(oid);
        System.out.println("row:"+row);
        if(row>0){
            return "success";
        }
        return "fail";
    }

}
