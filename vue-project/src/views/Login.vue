<template>
    <div class="content">
        <el-page-header @back="goBack" content="选择登录类型"></el-page-header>
        <div class="registerType">
            <el-button type="primary" icon="el-icon-edit" @click="toUser">我是顾客</el-button>
            <el-button type="primary" icon="el-icon-share" @click=toRider>我是骑手</el-button>
            <el-button type="primary" icon="el-icon-share" @click=toManager>我是店家或管理员</el-button>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Login",
        methods:{
            toUser(){
                this.$router.replace('/toUserLogin');
            },

            toRider(){
                this.$router.replace('/toRiderLogin');
            },

            toManager(){
                this.$router.replace('/toManagerLogin');
            },

            goBack(){
                this.$router.replace('/')
            }
        }
    }
</script>

<style scoped>
    .el-button{
        width: 200px;
        height: 80px;
    }

    .registerType{
        width: 700px;
        height: 100px;
        margin: 250px auto;
        padding:auto 60px;
    }
</style>
