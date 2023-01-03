<template>
    <div>
        <van-nav-bar
                title="个人信息"
                left-text="返回"
                left-arrow
                @click-left="onClickLeft"
        />
        <div>
            <van-form @submit="onSubmit">
                <van-field name="uploader" label="文件上传">
                    <template #input>
                        <van-uploader :max-count="1" v-model="uploader" :after-read="uploadImg" accept="image/*"/>
                    </template>
                </van-field>

                <van-field
                        v-model="user.username"
                        name="username"
                        label="用户名"
                        placeholder="用户名"
                        :rules="[{ required: true, message: '请填写用户名' }]"
                />
                <van-field
                        v-model="user.password"
                        type="password"
                        name="password"
                        label="密码"
                        placeholder="密码"
                        :rules="[{ required: true, message: '请填写密码' }]"
                />
                <div style="margin: 16px;">
                    <van-button round block type="info" native-type="submit">提交</van-button>
                </div>
            </van-form>
        </div>
        <!--遮挡板-->
        <div style="width:100%; height: 50px;bottom: 0; position: fixed;background-color: #ffffff; z-index: 100">

        </div>
    </div>
</template>

<script>
    import { Notify } from 'vant';
    export default {
        name: "Message",
        created(){
            console.log(this.$route.params.user.id);
            this.axios.get("http://localhost:8084/findUserById",{
                params:{
                    id: this.$route.params.user.id
                }
            }).then(resp=>{
                console.log("user:");
                console.log(resp.data);
                this.user = resp.data;
                let username = this.user.username;
                this.username = username;
                console.log(this.user);
                let url = {"url":this.user.picSrc};
                console.log(url);
                this.uploader.push(url);
                console.log(this.uploader);
            });

        },
        data(){
            return{
                user:'',
                uploader: [],
                username:'',
                /*username:this.$route.params.username,
                password:this.$route.params.password*/
            }
        },
        methods:{
            onSubmit(values){
                console.log('submit:', values);
                console.log(this.user);
                //验证账号
                if(this.username!==values.username){
                    this.axios.get("http://localhost:8084/findUserByUsername",{
                        params:{
                            username:values.username
                        }
                    }).then(resp=>{
                        if(resp.data==='exist'){
                            Notify({ type: 'warning', message: '用户名已存在！！！' });
                        }
                    })
                }
                //保存用户信息
                this.axios.post("http://localhost:8084/updateUserMessage",this.user).then(resp=>{
                    if(resp.data === "success"){
                        Notify({ type: 'success', message: '修改成功！！！' });
                    }else {
                        Notify({ type: 'warning', message: '修改失败！！！' });
                    }
                    this.$router.push("/my_user");
                })
            },

            onClickLeft(){
                this.$router.replace('/my_user');
            },

            /*上传图片文件*/
            uploadImg(file){
                console.log("file:");
                console.log(file);
                let data = new FormData();
                data.append('picture',file.file);
                this.axios.post("http://localhost:8084/upload",data,{
                    headers: { "Content-Type": "multipart/form-data" }
                }).then(resp=>{
                    console.log("上传后");
                    console.log(resp.data);
                    this.user.picSrc = resp.data.message
                })
               /* let config = {
                    headers:{'Content-Type':'multipart/form-data'} //这里是重点，需要和后台沟通好请求头，Content-Type不一定是这个值
                };
                this.axios.post("http://localhost:8084/upload", file.file,config).then(resp=>{
                    console.log("上传后");
                    console.log(resp.data);
                })*/
            }

      }
    }
</script>

<style scoped>

</style>
