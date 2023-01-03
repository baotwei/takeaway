<template>
    <div style="text-align: center;margin: auto">
        <div style="height: 50px;margin-left: 50px">
            <el-button type="primary"
                       @click="add" style="float: left">新增</el-button>
        </div>
        <el-table
                :data="goods"
        style="width:1300px;border-radius: 15px">
            <el-table-column
                    label="商品名"
                    prop="goodName" >
            </el-table-column>
            <el-table-column
                    label="图片"
                    prop="goodPic" >
                <template slot-scope="scope">
                    <el-image :src="scope.row.goodPic" style="width: 100px"/>
                </template>
            </el-table-column>
            <el-table-column
                    label="价格"
                    prop="price" >
            </el-table-column>
            <el-table-column
                    label="类别"
                    prop="category" >
            </el-table-column>
            <el-table-column
                    label="销量"
                    prop="sales" >
            </el-table-column>
            <el-table-column
                    label="描述"
                    prop="desc" >
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
                            @click="edit(scope.row)">修改</el-button>
                    <el-button
                            size="mini"
                            type="danger"
                            @click="delete(scope.row)">下架</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--点击新增后的弹窗-->
        <el-dialog title="商品信息" :visible.sync="addDialogVisible" style="line-height: 15px;height: 1200px">
            <el-form :model="newGoodInfo" status-icon :rules="newGoodRules" ref="newGoodInfo" style="text-align: left">
                <el-form-item label="商品名" :label-width="formLabelWidth" prop="goodName">
                    <el-input v-model="newGoodInfo.goodName" autocomplete="off"  placeholder="请输入商品名称"></el-input>
                </el-form-item>
                <el-form-item label="图片" :label-width="formLabelWidth">
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
                            :on-success="handleSuccessAdd"
                            :on-remove="handleRemove">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                    <el-dialog :visible.sync="dialogPicVisible">
                        <img width="100%" :src="dialogImageUrl" alt="">
                    </el-dialog>
                </el-form-item>
                <el-form-item label="价格" :label-width="formLabelWidth" prop="price">
                    <el-input v-model="newGoodInfo.price" autocomplete="off" placeholder="请输入商品价格"></el-input>
                </el-form-item>
                <el-form-item label="类别" :label-width="formLabelWidth" prop="category">
                    <el-select v-model="newGoodInfo.cid" placeholder="请选择商品类别">
                        <template v-for="category in categories">
                            <el-option :label="category.categoryName" :value="category.cid"></el-option>
                        </template>
                    </el-select>
                </el-form-item>
                <el-form-item label="描述" :label-width="formLabelWidth" prop="desc">
                    <el-input v-model="newGoodInfo.desc" autocomplete="off" placeholder="请输入商品描述"  clearable maxlength="10"
                              show-word-limit></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="flush">取 消</el-button>
                <el-button type="primary" @click="addSave">保 存</el-button>
            </span>
        </el-dialog>

        <!--点击修改后的弹窗-->
        <el-dialog title="商品信息" :visible.sync="editDialogVisible" style="line-height: 15px">
            <el-form :model="goodInfo" status-icon :rules="goodRules" ref="goodInfo" style="text-align: left">
                <el-form-item label="商品名" :label-width="formLabelWidth" prop="goodName">
                    <el-input v-model="goodInfo.goodName" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="图片" :label-width="formLabelWidth">
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
                    <el-dialog :visible.sync="dialogPicVisible">
                        <img width="100%" :src="dialogImageUrl" alt="">
                    </el-dialog>
                </el-form-item>
                <el-form-item label="价格" :label-width="formLabelWidth" prop="price">
                    <el-input v-model="goodInfo.price" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="类别" :label-width="formLabelWidth" prop="category">
                    <el-select v-model="goodInfo.cid" placeholder="请选择商品类别">
                        <template v-for="category in categories">
                            <el-option :label="category.categoryName" :value="category.cid"></el-option>
                        </template>

                    </el-select>
                </el-form-item>
                <!--<el-form-item label="销量" :label-width="formLabelWidth" prop="deliveryCost">
                    <el-input v-model="sales" autocomplete="off"></el-input>
                </el-form-item>-->
                <el-form-item label="描述" :label-width="formLabelWidth" prop="desc">
                    <el-input v-model="goodInfo.desc" autocomplete="off"  clearable></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="flush">取 消</el-button>
                <el-button type="primary" @click="save">保 存</el-button>
            </span>
        </el-dialog>





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
        name: "GoodsManage",
        created(){
            this.sid = this.$store.getters.getSid;
            const pageSize = this.pageSize;
            const currentPage = this.currentPage;
            this.axios.get("http://localhost:8084/findGoodsAndCategoryByPage",{
                params:{
                    uid:this.$store.getters.getShopManager.smid,
                    pageSize: pageSize,
                    currentPage: currentPage
                }
            }).then(resp=>{
                this.total = resp.data.totalCount;
                console.log(resp.data);
                this.goods = resp.data.goods;
            })
        },
        methods: {
            /*保存添加的数据*/
            addSave(){
                console.log(this.newGoodInfo);
                this.$refs['newGoodInfo'].validate((valid) => {
                    if (valid) {
                        this.$confirm("确定要新增这个商品吗？", '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'info'
                        }).then(() => {
                            console.log(this.newGoodInfo);
                            this.newGoodInfo.sid = this.sid;
                            this.axios.post("http://localhost:8084/addGoodMessage", this.newGoodInfo).then(resp => {
                                if (resp.data === "success") {
                                    this.$message({
                                        message: "添加成功！！！",
                                        type: 'success'
                                    });
                                    this.flush();
                                } else {
                                    this.$message({
                                        message: "添加失败！！！",
                                        type: 'warning'
                                    });
                                    this.flush();
                                }
                            })
                        }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消'
                            });
                        })
                    } else {
                        return false;
                    }
                })
            },

            /*点击新增按钮*/
            add(){
                this.addDialogVisible = true;
                this.getAllCategories(null);
            },

            /*点击修改按钮*/
            edit(row) {
                this.editDialogVisible = true;
                console.log(row);
                this.getAllCategories(row);
            },

            //获取所有类别
            getAllCategories(row){
                if(row!==null) {
                    this.goodInfo = row;
                }else {
                    this.newGoodInfo = {};
                }
                this.axios.get("http://localhost:8084/getAllCategoryBySid",{
                    params:{
                        sid:this.sid
                    }
                }).then(resp=>{
                    console.log(resp.data);
                    this.categories = resp.data;
                })
            },

            /*取消保存*/
            flush(){
                this.dialogVisible = false;
                this.$router.push({
                    name:"blank",
                    params:{
                        url:"goodsManage"
                    }
                })
            },

            /*保存修改*/
            save() {
                console.log(this.editShopInfo);
                this.$refs['goodInfo'].validate((valid) => {
                    if (valid) {
                        this.$confirm("确定要保存修改吗？", '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'info'
                        }).then(() => {
                            console.log(this.goodInfo);
                            this.axios.post("http://localhost:8084/updateGoodMessage", this.goodInfo).then(resp => {
                                if (resp.data === "success") {
                                    this.$message({
                                        message: "保存成功！！！",
                                        type: 'success'
                                    });
                                    this.flush();
                                } else {
                                    this.$message({
                                        message: "保存失败！！！",
                                        type: 'warning'
                                    });
                                    this.flush();
                                }
                            })
                        }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消'
                            });
                        })
                    } else {
                        return false;
                    }
                })
            },






            delete(row){
                console.log(row);
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
                    this.goodInfo.goodPic = file.response.message; //将返回的文件储存路径赋值picture字段
                }
            },

            handleSuccessAdd(res, file) {
                this.$message({
                    type: 'info',
                    message: '图片上传成功',
                    duration: 6000
                });
                console.log(file);
                if (file.response.code===200) {
                    this.newGoodInfo.goodPic = file.response.message; //将返回的文件储存路径赋值picture字段
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

            /*分页相关*/
            handleSizeChange(val) {
                this.pageSize = val;
                const that = this;
                this.axios.get("http://localhost:8084/findGoodsAndCategoryByPage",{
                    params:{
                        uid:this.$store.getters.getShopManager.smid,
                        pageSize: this.pageSize,
                        currentPage: this.currentPage
                    }
                }).then(resp=>{
                    this.total = resp.data.totalCount;
                    console.log(resp.data);
                    this.goods = resp.data.goods;
                });
            },
            handleCurrentChange(val) {
                this.currentPage = val;
                const that = this;
                this.axios.get("http://localhost:8084/findGoodsAndCategoryByPage",{
                    params:{
                        uid:this.$store.getters.getShopManager.smid,
                        pageSize: this.pageSize,
                        currentPage: this.currentPage
                    }
                }).then(resp=>{
                    this.total = resp.data.totalCount;
                    console.log(resp.data);
                    this.goods = resp.data.goods;
                });
            }
        },

        data() {

            /*商品信息非空判断和唯一验证*/
            const validateGoodName = (rule, value, callback) => {
                if (value === ''||value==null) {
                    callback(new Error('请输入商品名'));
                } else {
                    if(value!==this.goodInfo.goodName){
                        this.axios.get("http://localhost:8084/checkGoodName",{
                            params:{
                                goodName:value
                            }
                        }).then(resp=>{
                            if(resp.data==='none'){
                                callback();
                            }else {
                                callback(new Error('该商品名已存在！！'));
                            }
                        })
                    }else {
                        callback();
                    }
                }
            };

            const validateNewGoodName = (rule, value, callback) => {
                if (value === ''||value==null) {
                    callback(new Error('请输入商品名'));
                } else {
                    if(value!==this.newGoodInfo.goodName){
                        this.axios.get("http://localhost:8084/checkGoodName",{
                            params:{
                                goodName:value
                            }
                        }).then(resp=>{
                            if(resp.data==='none'){
                                callback();
                            }else {
                                callback(new Error('该商品名已存在！！'));
                            }
                        })
                    }else {
                        callback();
                    }
                }
            };


            /*const validateCategoryName = (rule, value, callback) => {
                if (value === ''||value==null) {
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
            };*/

            const validatePrice = (rule, value, callback) => {
                if (value === ''||value==null) {
                    callback(new Error('请输入价格'));
                } else {
                    callback();
                }
            };

            /*const validateSales = (rule, value, callback) => {
                if (value === ''||value==null) {
                    callback(new Error('请输入起送价格'));
                } else {
                    callback();
                }
            };*/

            const validateDesc = (rule, value, callback) => {
                if (value === ''||value === null) {
                    callback(new Error('请输入商品描述'));
                } else {
                    callback();
                }
            };




            return {
                goodInfo:{
                    cid:'',
                    category:'',
                    goodName: '',
                    goodPic: '',
                    price: '',
                    sales: '',
                    desc: ''
                },
                newGoodInfo:{
                    cid:'',
                    sid:'',
                    category:'',
                    goodName: '',
                    goodPic: '',
                    price: '',
                    sales: '',
                    desc: ''
                },
                categories:[],
                //控制弹窗是否显示
                editDialogVisible: false,
                addDialogVisible: false,

                formLabelWidth: '120px',

                //文件上传的参数
                dialogImageUrl: '',
                dialogPicVisible: false,
                fileList:[],
                picture:'',
                fileLimit:1,

                /*分页*/
                pageSize: 5,
                total: 0,
                currentPage: 1,
                pageSizes: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],

                search:'',

                goodRules:{
                    goodName:[
                        { validator:validateGoodName,trigger: 'blur'}
                    ],
                    /*categoryName:[
                        { validator:validateCategoryName,trigger: 'blur'}
                    ],*/
                    price:[
                        { validator:validatePrice,trigger: 'blur'}
                    ],
                    /*sales:[
                        { validator:validateSales,trigger: 'blur'}
                    ],*/
                    desc:[
                        { validator:validateDesc,trigger: 'blur'}
                    ],

                },
                newGoodRules:{
                    goodName:[
                        { validator:validateNewGoodName,trigger: 'blur'}
                    ],
                    /*categoryName:[
                        { validator:validateCategoryName,trigger: 'blur'}
                    ],*/
                    price:[
                        { validator:validatePrice,trigger: 'blur'}
                    ],
                    /*sales:[
                        { validator:validateSales,trigger: 'blur'}
                    ],*/
                    desc:[
                        { validator:validateDesc,trigger: 'blur'}
                    ],

                },

                goods: [
                    {
                    categoryName:'汉堡',
                    goodName: '法式汉堡',
                    goodPic: 'pic/dinner.png',
                    price: '8',
                    sales: '50',
                    desc: '法国特质汉堡'
                }, {
                        categoryName:'汉堡',
                        goodName: '法式汉堡',
                        goodPic: 'pic/dinner.png',
                        price: '8',
                        sales: '50',
                        desc: '法国特质汉堡'
                }, {
                        categoryName:'汉堡',
                        goodName: '法式汉堡',
                        goodPic: 'pic/dinner.png',
                        price: '8',
                        sales: '50',
                        desc: '法国特质汉堡'
                }, {
                        categoryName:'汉堡',
                        goodName: '法式汉堡',
                        goodPic: 'pic/dinner.png',
                        price: '8',
                        sales: '50',
                        desc: '法国特质汉堡'
                }]
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
