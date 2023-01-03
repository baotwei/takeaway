<template>
    <div >
        <div style="height: 150px;margin-top: 70px">
            <div style="">
                <van-image
                        v-if="user.picSrc===null"
                        round
                        width="150"
                         height="150"
                        src="https://img01.yzcdn.cn/vant/cat.jpeg"
                />
                <van-image
                        v-if="user.picSrc!==null"
                        round
                        width="150"
                        height="150"
                        :src="user.picSrc"
                />
            </div>
            <div style="margin-top: 50px">
                <van-tag color="#ffe1e1" size="large" text-color="#ad0000">vvvip</van-tag>
                <span style="color: #409EFF;" v-model="user.username">   {{user.username}}</span>
                <h4 style="color: #409EFF;" v-model="user.score">积分： {{user.score}}  </h4>
            </div>
            <div style=" margin-top: 20px">
                <van-button round type="info" style="width: 200px" @click="updateMessage">修改个人信息</van-button>
                <br>
                <br>
                <van-button round type="info" style="width: 200px" @click="getAddress">地址</van-button>
                <br>
                <br>
                <van-button round type="info" style="width: 200px" @click="loginOut">退出登录</van-button>
            </div>

        </div>

    </div>


</template>

<script>
    import Toast from "vant/lib/toast";

    export default {
        name: "My",
        created(){
            this.axios.get("http://localhost:8084/findUserById",{
                params:{
                    id:this.$store.getters.getUser.id
                }
            }).then(resp=>{
                this.user = resp.data;
            });
            console.log(this.user);
        },
        data(){
            return {
                username:'lllllky',
                password:'123456',
                score:12,
                user:{
                    picSrc:''
                }
            }
        },
        methods:{
            updateMessage(){
                console.log("修改个人信息");
                this.$router.push({
                    name:'message',
                    //参数会显示在请求路径中，类似于get传参
                   /* query:{
                        username:this.username,
                        password:this.password
                    }*/

                    //参数不会显示在请求路径中，类似于post传参
                    params:{
                        user:this.user
                    }
                });
            },

            getAddress(){
                this.$router.push('/address');
            },

            //退出登录
            loginOut(){
               Toast("退出登录");
               this.$store.commit("SET_USERINFO",null);
               this.$router.push("/toUserLogin")
            }
        }
    }
</script>

<style scoped>

</style>
