<template>
    <div>
        <van-image
                width="100"
                height="100"
                src="/pic/paysuccess.png"
        />
        <van-cell-group>
            <!--<van-cell>
                <van-col span="21" style="width: 140px">商品总金额</van-col>
                <van-col style="width: 50px;margin-left: 80px">￥{{goodTotalPrice}}</van-col>
            </van-cell>
            <van-cell>
                <van-col span="21" style="width: 140px">配送费</van-col>
                <van-col style="width: 50px;margin-left: 80px">￥{{deliveryPrice}}</van-col>
            </van-cell>-->
            <van-cell>
                <van-col span="21" style="width: 140px">支付金额</van-col>
                <van-col style="width: 50px;margin-left: 80px">￥{{orderInfo.totalPrice}}</van-col>
            </van-cell>
        </van-cell-group>
        <van-cell-group>
            <van-cell>
                <van-col span="21" style="width: 60px">订单号</van-col>
                <van-col style="width: 200px;margin-left: 25px">{{orderInfo.orderNumber}}</van-col>
            </van-cell>
        </van-cell-group>
        <van-cell-group>
            <van-cell>
                <van-col span="21" style="width: 60px">订单状态</van-col>
                <van-col style="width: 140px;margin-left: 60px" v-if="orderInfo.orderStat===1">已支付，等待商家接单</van-col>
                <van-col style="width: 140px;margin-left: 60px" v-if="orderInfo.orderStat===2">商家已接单</van-col>
                <van-col style="width: 140px;margin-left: 60px" v-if="orderInfo.orderStat===3">骑手已接单，等待送达</van-col>
                <van-col style="width: 140px;margin-left: 60px" v-if="orderInfo.orderStat===4">已送达</van-col>
            </van-cell>
            <van-cell>
                <van-col span="21" style="width: 60px">创建时间</van-col>
                <van-col style="width: 140px;margin-left: 80px">{{orderInfo.createTime}}</van-col>
            </van-cell>
            <!--<van-cell>
                <van-col span="21" style="width: 140px">配送方式</van-col>
                <van-col style="width: 80px;margin-left: 60px">旋风骑士</van-col>
            </van-cell>-->
        </van-cell-group>
        <van-button type="primary" size="large" @click="showOrderInfo">查看订单详情</van-button>
        <!--遮挡板-->
        <div style="width:100%; height: 50px;bottom: 0; position: fixed;background-color: #ffffff; z-index: 100">

        </div>
    </div>
</template>

<script>
    import OrderInfo from "./OrderInfo";
    import Toast from "vant/lib/toast";

    export default {
        /*orderNumber:"202108040080",
        totalPrice:this.totalPrice,
        goods:this.selectedGoods,
        addressMessage:this.addressMessage,
        orderStat:1*/
        name: "PaySuccess",
        created() {
            Toast("支付成功");
            this.orderInfo.oid = this.$route.params.oid;
            this.orderInfo.totalPrice = this.$route.params.totalPrice;
            this.orderInfo.goodTotalPrice = this.$route.params.goodTotalPrice;
            this.orderInfo.deliveryPrice = this.$route.params.deliveryPrice;
            this.orderInfo.orderNumber = this.$route.params.orderNumber;
            this.orderInfo.goods = this.$route.params.goods;
            this.orderInfo.addressMessage = this.$route.params.addressMessage;
            this.orderInfo.shopName = this.$route.params.shopName;
            this.orderInfo.orderStat = this.$route.params.orderStat;
            this.orderInfo.createTime = this.$route.params.createTime;
        },
        data(){
            return{
                /*goodTotalPrice:null,
                deliveryPrice:null,
                totalPrice:null,
                orderNumber:null,
                goods:null,
                addressMessage: null,
                orderStat:null,
                createTime:null,*/
                orderInfo: {
                    goodTotalPrice:null,
                    deliveryPrice:null,
                    totalPrice:null,
                    orderNumber:null,
                    goods:null,
                    addressMessage: null,
                    shopName:null,
                    orderStat:null,
                    createTime:null,
                    /*deliveryManId*/
                    oid:''
                }
            }
        },
        methods:{
            showOrderInfo(){
                //Toast("查看订单详情");
                this.$router.push({
                    name:'orderInfo_user',
                    params:{
                        orderInfo:this.orderInfo
                    }
                })
            }
        }

    }
</script>

<style scoped>

</style>
