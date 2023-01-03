<template>
<div>
    <el-main >
        <el-form :model="shopForm" status-icon :rules="shopRules" ref="shopForm" label-width="150px" style="width: 500px;margin: auto;">
            <el-form-item label="店铺名:" prop="shopName">
                <el-input type="text" :disabled="editDisabled" v-model="shopForm.shopName"></el-input>
            </el-form-item>
            <el-form-item label="店铺Logo" style="text-align: left">
                    <img width="200px;" height="200px" :src="shopForm.logoSrc" alt="" v-if="editDisabled">
                <template v-if="!editDisabled">
                    <el-upload
                            ref="upload"
                            action="http://localhost:8084/upload"
                            name="picture"
                            list-type="picture-card"
                            :limit="1"
                            :file-list="fileList"
                            :on-exceed="onExceed"
                            :before-upload="beforeUpload"
                            :before-remove="beforeRemove"
                            :on-preview="handlePreview"
                            :on-success="handleSuccess"
                            :on-remove="handleRemove">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog :visible.sync="dialogVisible">
                        <img width="100%" :src="dialogImageUrl" alt="">
                    </el-dialog>
                </template>

            </el-form-item>
            <el-form-item label="营业许可证:" prop="license">
                <el-input type="text" :disabled="editDisabled" v-model="shopForm.license" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="配送费￥:" prop="deliveryCost">
                <el-input type="text" :disabled="editDisabled" v-model="shopForm.deliveryCost" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="预估配送时间(分钟):" prop="deliveryTime">
                <el-input type="text" :disabled="editDisabled" v-model="shopForm.deliveryTime" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="起送费￥:" prop="minCost">
                <el-input type="text" :disabled="editDisabled" v-model.number="shopForm.minCost"></el-input>
            </el-form-item>
            <el-form-item label="联系电话:" prop="phone">
                <el-input type="text" :disabled="editDisabled" v-model="shopForm.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺详细地址:" prop="addressDetail">
                <el-input type="text" :disabled="editDisabled" v-model="shopForm.addressDetail" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺描述:" prop="desc">
                <el-input type="text" autosize :disabled="editDisabled" v-model="shopForm.desc" autocomplete="off" maxlength="12"
                          show-word-limit></el-input>
            </el-form-item>
            <el-form-item v-if="editDisabled">
                <el-button type="primary" @click="toEditShopForm">修改店铺信息</el-button>
            </el-form-item>
            <template v-if="!editDisabled">
                <el-form-item>
                    <el-button type="primary" @click="save">保存</el-button>
                    <el-button type="primary" @click="flush">取消</el-button>
                </el-form-item>
            </template>

        </el-form>
    </el-main>
    <!--<el-form :label-position="top" label-width="80px" :model="shopMessage">
        <el-form-item label="店铺名称">
            <el-input v-model="shopMessage.shopName"></el-input>
        </el-form-item>
        <el-form-item label="图片">
            <el-input v-model="shopMessage.logoSrc"></el-input>
        </el-form-item>

        <el-form-item label="证件">
            <el-input v-model="shopMessage.license"></el-input>
        </el-form-item>
    </el-form>-->
</div>
</template>

<script>
    import Toast from "vant/lib/toast";

    export default {
        name: "ShopMessage",
        created() {
            const uid = this.$store.getters.getShopManager.smid;
            console.log(uid);
            this.axios.get("http://localhost:8084/findShopByUid",{
                params:{
                    uid:uid
                }
            }).then(resp=>{
                console.log(resp.data);
                this.shopForm = resp.data;
                this.licenseCount = resp.data.license;
            })


        },
        data(){

            /*店铺信息非空判断和唯一验证*/
            const validateShopName = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入店铺名'));
                } else {
                    callback();
                }
            };


            const validateLicense = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入营业许可证'));
                } else {
                    if(value!==this.licenseCount){
                        this.axios.get("http://localhost:8084/findShopByLicense",{
                            params:{
                                license:value
                            }
                        }).then(resp=>{
                            if(resp.data==='none'){
                                callback();
                            }else {
                                callback(new Error('该营业许可证已被使用'));
                            }
                        })
                    }else {
                        callback();
                    }
                }
            };

            const validateDeliveryCost = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入配送费'));
                } else {
                    callback();
                }
            };

            const validateDeliveryTime = (rule, value, callback) => {
                if (value === ''||value === null) {
                    callback(new Error('请输入预估配送时间'));
                } else {
                    callback();
                }
            };

            const validateMinCost = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入起送价格'));
                } else {
                    callback();
                }
            };

            const validateShopPhone = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入店铺联系电话'));
                } else {
                    callback();
                }
            };

            const validateAddressDetail = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入店铺详细地址'));
                } else {
                    callback();
                }
            };

            const validateDesc = (rule, value, callback) => {
                if (value === ''||value === null) {
                    callback(new Error('请输入店铺描述'));
                } else {
                    callback();
                }
            };

            return{
                editDisabled:true,


                shopForm: {
                    shopName:'',
                    logoSrc: '',
                    license: '',
                    deliveryCost:'',
                    deliveryTime:null,
                    minCost:'',
                    phone:'',
                    addressDetail:'',
                    desc:null
                },


                //文件上传的参数
                dialogImageUrl: '',
                dialogVisible: false,
                fileList:[],
                picture:'',
                fileLimit:1,

                licenseCount:'',
                shopMessage:{
                    sid:'',
                    logoSrc:'',
                    shopName:'',
                    license:'',
                    stat:'',
                    deliveryCost:'',
                    deliveryTime:'',
                    minCost:'',
                    totalSales:'',
                    phone:'',
                    desc:''
                },

                shopRules:{
                    shopName:[
                        { validator:validateShopName,trigger: 'blur'}
                    ],
                    license: [
                        { validator:validateLicense,trigger: 'blur'}
                    ],
                    deliveryCost:[
                        { validator:validateDeliveryCost,trigger: 'blur'}
                    ],
                    deliveryTime:[
                        { validator:validateDeliveryTime,trigger: 'blur'}
                    ],
                    minCost:[
                        { validator:validateMinCost,trigger: 'blur'}
                    ],
                    phone:[
                        { validator:validateShopPhone,trigger: 'blur'}
                    ],
                    addressDetail:[
                        { validator:validateAddressDetail,trigger: 'blur'}
                    ],
                    desc:[
                        { validator:validateDesc,trigger: 'blur'}
                    ]
                }
            }
        },
        methods:{

            /*点击修改按钮*/
            toEditShopForm(){
                this.editDisabled = false;
                console.log("修改")
            },

            //保存
            save(){
                this.$refs['shopForm'].validate((valid) => {
                    if (valid) {
                        this.axios.post("http://localhost:8084/updateShopMessage",this.shopForm)
                            .then(resp=>{
                                if(resp.data === "success"){
                                    console.log("修改成功");
                                    this.$message({
                                        type: 'success',
                                        message: '修改成功',
                                        duration: 6000
                                    });
                                    //刷新
                                    this.flush();
                                }

                            })
                    } else {
                        Toast("请输入店铺信息");
                        return false;
                    }
                });
            },

            //文件上传成功的钩子函数
            handleSuccess(res, file) {
                this.$message({
                    type: 'info',
                    message: '图片上传成功',
                    duration: 6000
                });
                console.log(file);
                if (file.response.code===200) {
                    this.shopForm.logoSrc = file.response.message; //将返回的文件储存路径赋值picture字段
                }
            },

            //删除文件之前的钩子函数
            handleRemove(file, fileList) {
                this.$message({
                    type: 'info',
                    message: '已删除原有图片',
                    duration: 6000
                });
            },
            //图片预览
            handlePreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },


            //上传的文件个数超出设定时触发的函数
            onExceed(files, fileList) {
                this.$message({
                    type: 'info',
                    message: '最多只能上传一个图片',
                    duration: 6000
                });
            },

            //文件上传前的前的钩子函数
            //参数是上传的文件，若返回false，或返回Primary且被reject，则停止上传
            beforeUpload(file){
                const isJPG = file.type === 'image/jpeg';
                const isGIF = file.type === 'image/gif';
                const isPNG = file.type === 'image/png';
                const isBMP = file.type === 'image/bmp';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG && !isGIF && !isPNG && !isBMP) {
                    this.$message.error('上传图片必须是JPG/GIF/PNG/BMP 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 2MB!');
                }
            },

            //删除前提示
            beforeRemove(file) {
                return this.$confirm(`确定移除 ${ file.name }？`);
            },

            //取消
            flush(){
                this.editDisabled = true;
                this.$router.push({
                    name:"blank",
                    params:{
                        url:"shopMessage"
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
