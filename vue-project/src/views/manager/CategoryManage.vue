<template>
    <div style="text-align: center;margin: auto">
        <div style="height: 50px;margin-left: 185px">
            <el-button type="primary"
                       @click="add" style="float: left">新增</el-button>
        </div>
        <el-table
                :data="categories"
        style="width: 1050px;border-radius: 15px;padding-left: 5px">
            <el-table-column
                    label="类别名"
                    prop="categoryName">
            </el-table-column>
            <el-table-column
                    align="right" >
                <template slot="header" slot-scope="scope">
                </template>
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            @click="edit(scope.row)">修改</el-button>
                    <el-button
                            size="mini"
                            type="danger"
                            @click="delete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--点击新增后的弹窗-->
        <el-dialog title="类别信息" :visible.sync="addDialogVisible" style="line-height: 15px">
            <el-form :model="newCategoryInfo" status-icon :rules="newCategoryRules" ref="newCategoryInfo" style="text-align: left">
                <el-form-item label="类别名" :label-width="formLabelWidth" prop="categoryName">
                    <el-input v-model="newCategoryInfo.categoryName" autocomplete="off" placeholder="请输入类别名"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="flush">取 消</el-button>
                <el-button type="primary" @click="addSave">新 增</el-button>
            </span>
        </el-dialog>

        <!--点击修改后的弹窗-->
        <el-dialog title="类别信息" :visible.sync="editDialogVisible" style="line-height: 15px">
            <el-form :model="categoryInfo" status-icon :rules="categoryRules" ref="categoryInfo" style="text-align: left">
                <el-form-item label="类别名" :label-width="formLabelWidth" prop="categoryName">
                    <el-input v-model="categoryInfo.categoryName" autocomplete="off"></el-input>
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
        name: "CategoryManage",
        created(){
            console.log(this.$store.getters.getSid);
            this.sid = this.$store.getters.getSid;
            this.axios.get("http://localhost:8084/findCategoriesByPage", {
                params: {
                    uid: this.$store.getters.getShopManager.smid,
                    pageSize: this.pageSize,
                    currentPage: this.currentPage
                }
            }).then(resp => {
                this.categories = resp.data.categories;
                this.total = resp.data.count;
            });
        },

        data() {
            /*类别信息非空判断和唯一验证*/
            const validateCategoryName = (rule, value, callback) => {
                if (value === ''||value==null) {
                        callback(new Error("请输入类别名"))
                } else {
                    if (value !== this.categoryInfo.categoryName) {
                        this.axios.get("http://localhost:8084/findCategoryNameBySid", {
                            params: {
                                categoryName: value,
                                sid: this.categoryInfo.sid
                            }
                        }).then(resp => {
                            if (resp.data === 'none') {
                                callback();
                            } else {
                                callback(new Error('该类别已存在'));
                            }
                        })
                    }else {
                        callback();
                    }
                }
            };
            const validateNewCategoryName = (rule, value, callback) => {
                console.log(value)
                if (value === ''||value==null) {
                    callback(new Error("请输入类别名"))
                } else {
                    if (value !== this.newCategoryInfo.categoryName) {
                        this.axios.get("http://localhost:8084/findCategoryNameBySid", {
                            params: {
                                categoryName: value,
                                sid: this.sid
                            }
                        }).then(resp => {
                            if (resp.data === 'none') {
                                callback();
                            } else {
                                callback(new Error('该类别已存在'));
                            }
                        })
                    }else {
                        callback();
                    }
                }
            };

                    return {
                        sid:'',
                        formLabelWidth: '120px',

                        categoryInfo: {
                            categoryName: '',
                            sid:'',
                            cid:''
                        },
                        newCategoryInfo: {
                            categoryName: '',
                            sid:'',
                            cid:''
                        },
                        categoryRules: {
                            categoryName: [
                                {validator: validateCategoryName, trigger: 'blur'}
                            ],

                        },
                        newCategoryRules:{
                            categoryName: [
                                {validator: validateNewCategoryName, trigger: 'blur'}
                            ],
                        },

                        editDisabled: true,
                        editDialogVisible: false,
                        addDialogVisible: false,

                        /*分页*/
                        pageSize: 5,
                        total: 0,
                        currentPage: 1,
                        pageSizes: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],

                        categories: [
                            {
                                categoryName: '招牌脆皮炸鸡'
                            }
                        ]
                    }
                },
                methods: {
                    /*保存新增的类别信息*/
                    addSave(){
                        this.$refs['newCategoryInfo'].validate((valid) => {
                            if (valid) {
                                this.$confirm("确定要新增类别吗？", '提示', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'info'
                                }).then(() => {
                                    this.newCategoryInfo.sid = this.sid;
                                    console.log(this.newCategoryInfo);
                                    this.axios.post("http://localhost:8084/addCategoryMessage", this.newCategoryInfo).then(resp => {
                                        if (resp.data === "success") {
                                            this.$message({
                                                message: "新增成功！！！",
                                                type: 'success'
                                            });
                                            this.flush();
                                        } else {
                                            this.$message({
                                                message: "新增失败！！！",
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
                    add(){
                      this.addDialogVisible = true;
                    },
                    edit(item){
                        console.log(item);
                        this.editDialogVisible = true;
                        this.categoryInfo.categoryName = item.categoryName;
                        this.categoryInfo.sid = item.sid;
                        this.categoryInfo.cid = item.cid;
                    },

                /*取消保存*/
                flush() {
                    this.editDialogVisible = false;
                    this.$router.push({
                        name: "blank",
                        params: {
                            url: "categoryManage"
                        }
                    })
                },

            /*保存修改*/
            save()
            {
                console.log(this.categoryInfo);
                this.$refs['categoryInfo'].validate((valid) => {
                    if (valid) {
                        this.$confirm("确定要保存修改吗？", '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'info'
                        }).then(() => {
                            console.log(this.categoryInfo);
                            this.axios.post("http://localhost:8084/updateCategoryMessage", this.categoryInfo).then(resp => {
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
            }
        ,


            /*分页相关*/
            handleSizeChange(val)
            {
                this.pageSize = val;
                const that = this;
                this.axios.get("http://localhost:8084/findCategoriesByPage", {
                    params: {
                        uid: this.$store.getters.getShopManager.smid,
                        pageSize: this.pageSize,
                        currentPage: this.currentPage
                    }
                }).then(resp => {
                    this.categories = resp.data.categories;
                    this.total = resp.data.count;
                });
            }
        ,
            handleCurrentChange(val)
            {
                this.currentPage = val;
                const that = this;
                this.axios.get("http://localhost:8084/findCategoriesByPage", {
                    params: {
                        uid: this.$store.getters.getShopManager.smid,
                        pageSize: this.pageSize,
                        currentPage: this.currentPage
                    }
                }).then(resp => {
                    this.categories = resp.data.categories;
                    this.total = resp.data.count;
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
