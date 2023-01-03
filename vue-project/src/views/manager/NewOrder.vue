<template>
    <div style="text-align: center;margin: auto">
        <el-table
                :data="orders"
                style="width:1300px;border-radius: 15px;margin-top: 20px;padding-left: 5px">
            <el-table-column
                    label="订单号"
                    prop="orderInfo.orderNumber" >
            </el-table-column>
            <el-table-column
                    label="下单用户"
                    prop="userInfo.username" >
            </el-table-column>
            <el-table-column
                    label="订单详情"
                    prop="orderInfo" >
                <template slot-scope="scope">
                    <el-popover
                            placement="right"
                            trigger="click">
                        <div>
                            <el-table :data="scope.row.goodInfo">
                                <el-table-column width="200" property="goodName" label="商品名称"></el-table-column>
                                <el-table-column width="80" property="count" label="数量">
                                    <template slot-scope="scope">
                                        ×&nbsp;{{scope.row.count}}
                                    </template>
                                </el-table-column>
                            </el-table>
                            <div style="margin: 5px;color: #409EFF">总价：￥{{scope.row.orderInfo.totalPrice}}</div>
                        </div>
                        <el-button slot="reference">查看详情</el-button>
                    </el-popover>
                </template>
            </el-table-column>
            <el-table-column
                    label="送达地址"
                    prop="addressInfo.address" >
            </el-table-column>
            <el-table-column
                    label="创建时间"
                    prop="orderInfo.createTime" >
            </el-table-column>
            <el-table-column
                    align="right" >
                <template slot="header" slot-scope="scope">
                    <el-input
                            v-model="search"
                            size="mini"
                            placeholder="输入关键字搜索"/>
                </template>
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="primary"
                            @click="pick(scope.row)">接单</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--分页-->
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="pageSizes"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
        </el-pagination>
    </div>
</template>

<script>
    export default {
        name: "NewOrder",
        created(){
            this.sid = this.$store.getters.getSid;
            console.log(this.sid);
            this.axios.get("http://localhost:8084/getAllNewOrderBySid",{
                params:{
                    sid:this.sid,
                    pageSize: this.pageSize,
                    currentPage: this.currentPage
                }
            }).then(resp=>{
                console.log(resp.data);
                this.orders = resp.data.orders;
                this.total = resp.data.totalCount;
            })
        },
        data(){
            return{
                sid:'',
                orders:[
                    {
                        orderNumber:'',
                        userInfo:'',
                        orderInfo:'',
                        addressInfo:'',
                        createTime:''
                    }
                ],

                /*分页*/
                pageSize:5,
                total: 0,
                currentPage: 1,
                pageSizes:[1,2,3,4,5,6,7,8,9,10],

                search:'',
            }
        },
        methods:{
            /*接单*/
            pick(item){
                this.$confirm("确定要接单吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'info'
                }).then(() => {
                    this.axios.get("http://localhost:8084/shopPickOrder",{
                        params:{
                            oid:item.orderInfo.oid
                        }
                    }).then(resp=>{
                        if (resp.data === "success") {
                            this.$message({
                                message: "接单成功！！！",
                                type: 'success'
                            });
                            this.flush();
                        } else {
                            this.$message({
                                message: "接单失败！！！",
                                type: 'warning'
                            });
                            this.flush();
                        }
                    })
                })
            },

            /*刷新*/
            flush(){
                this.$router.push({
                    name:"blank",
                    params:{
                        url:"newOrder"
                    }
                })
            },

            /*分页相关*/
            handleSizeChange(val) {
                this.pageSize = val;
                const that = this;
                this.axios.get("http://localhost:8084/getAllNewOrderBySid",{
                    params:{
                        sid:this.sid,
                        pageSize: this.pageSize,
                        currentPage: this.currentPage
                    }
                }).then(resp=>{
                    this.orders = resp.data.orders;
                    this.total = resp.data.totalCount;
                });
            },
            handleCurrentChange(val) {
                this.currentPage = val;
                const that = this;
                this.axios.get("http://localhost:8084/getAllNewOrderBySid",{
                    params:{
                        sid:this.sid,
                        pageSize: this.pageSize,
                        currentPage: this.currentPage
                    }
                }).then(resp=>{
                    this.orders = resp.data.orders;
                    this.total = resp.data.totalCount;
                });
            }
        }
    }
</script>

<style scoped>
    .el-table{
        line-height: 50px;
        margin: auto;
    }

    .cell{
        width: 50px;
    }
</style>
