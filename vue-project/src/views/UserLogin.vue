<template>
  <div>
    <el-page-header @back="goBack" content="用户登录"></el-page-header>
<div class="login-form">
  <el-tabs type="border-card">
    <el-tab-pane label="账号登陆">
      <div class="form">
        <el-form :model="usernameForm" status-icon :rules="rules" ref="usernameForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="账号" prop="username">
            <el-input  :placeholder="holder1" type="text" v-model="usernameForm.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="pass">
            <el-input type="password" :placeholder="holder2" v-model="usernameForm.pass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('usernameForm')">登录</el-button>
            <el-button @click="register()">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-tab-pane>
    <el-tab-pane label="手机号登陆">
      <div class="form">
        <el-form :model="phoneForm" status-icon :rules="rules" ref="phoneForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="手机号" prop="phone">
            <el-input :placeholder="holder3" type="text" v-model="phoneForm.phone" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="验证码" style="width: 350px;display: inline-block" prop="code">
            <el-input type="text"  :placeholder="holder4" v-model="phoneForm.code" autocomplete="off"></el-input>
          </el-form-item>
          <span v-if="showCode===true">
                                <el-button  type="button" style="width: 120px;" @click="sentCode">获取验证码</el-button>
                            </span>
          <span v-if="showCode===false">
                                <el-button  type="button" style="width: 120px;">{{count}}</el-button>
                            </span>
          <el-form-item>
            <el-button type="primary" @click="submitForm('phoneForm')">登录</el-button>
            <el-button @click="register()">注册</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-tab-pane>
  </el-tabs>
</div>
  </div>



</template>

<style>


  .login-form{
    width: 600px;
    height: 300px;
    margin: 250px auto;
  }

  .el-tabs{
    width: 500px;
    height: 300px;
    margin: 250px auto;
  }


  .form{
    margin: 50px auto;
  }

</style>
<script>
/*// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'*/


import Toast from "vant/lib/toast";

export default {
  name: 'Home',
    created(){
      console.log(this.$store.getters);
    },
    data() {
      const validateUsername = (rule, value, callback) => {
        if (value === ''||value.trim().length===0) {
          callback(new Error('请输入用户名'));
        } else {
          callback();
        }
      };

      const validatePass = (rule, value, callback) => {
        if (value === ''||value.trim().length===0) {
          callback(new Error('请输入密码'));
        } else {
          callback();
        }
      };

      const validatePhone = (rule, value, callback) => {
        if (value === ''||value.trim().length===0) {
          callback(new Error('请输入手机号'));
        } else {
          this.axios.get("http://localhost:8084/findUserByPhone", {
            params:{
              phone:value
            }
          }).then(resp => {
            if(resp.data === "exist"){
              this.phoneIsExist = true;
              callback();
            }else {
              this.phoneIsExist = false;
              callback(new Error('该手机号未注册'));
            }
          });
        }
      };

      const validateCode = (rule, value, callback) => {
        if (value === ''||value.trim().length===0) {
          callback(new Error('请输入验证码'));
        } else {
          callback();
        }
      };

      return {
        showCode:true,
        holder1:'请输入账号',
        holder2:'请输入密码',
        holder3:'请输入手机号',
        holder4:'请输入验证码',
        usernameForm: {
          username:'lky',
          pass: '123'
        },
        phoneForm:{
          phone:'',
          code:''
        },
        rules: {
          username:[
            { validator: validateUsername, trigger: 'blur' }
          ],
          pass: [
            { validator: validatePass, trigger: 'blur' }
          ],
          phone: [
            { validator: validatePhone, trigger: 'blur' }
          ],
          code: [
            { validator: validateCode, trigger: 'blur' }
          ]
        }
      };
    },

    methods: {
      changeCodeStat(){
        const TIME_COUNT = 60;
        if (!this.timer) {
          this.count = TIME_COUNT;
          this.showCode = false;
          this.timer = setInterval(() => {
            if (this.count > 0 && this.count <= TIME_COUNT) {
              this.count--;
            } else {
              this.show = true;
              clearInterval(this.timer);
              this.timer = null;
            }
          }, 1000)
        }
      },

      /*发送code*/
      sentCode(){
        const phone = this.phoneForm.phone;
        if(phone===null||phone===''||phone.trim().length===0){
          Toast("请输入手机号！！！")
        }else {
          if(this.phoneIsExist){
            this.changeCodeStat();
            this.axios.get("http://localhost:8084/sendCodeUser",{
              params:{
                phone: phone
              }
            }).then(resp=>{
              if(resp.data==="success"){
                Toast("发送成功")
              }else {
                Toast("fail")
              }
            })
          }else {
            Toast("手机号未注册！！！")
          }
        }

      },

      submitForm: function (formName) {
        const that = this;
        if(formName==='usernameForm'){
          this.$refs[formName].validate((valid) => {
            if (valid) {
              this.axios.get("http://localhost:8084/login",{
                params:{
                  username: this.usernameForm.username,
                  password: this.usernameForm.pass
                }
              }).then(function (resp) {
                console.log(resp.data);
                if(resp.data.code === 200){
                  Toast("登陆成功");
                  that.$store.commit("SET_USERINFO",resp.data.data.user);
                  that.$router.push('/user/index')
                }else {
                  Toast("账号或密码错误！！！")
                }
              });

            } else {
              console.log('error submit!!');
              return false;
            }
          });
        }else if(formName==='phoneForm'){
          this.$refs[formName].validate((valid) => {
            if (valid) {
              this.axios.get("http://localhost:8084/loginByPhone",{
                params:{
                  phone: this.phoneForm.phone,
                  code: this.phoneForm.code
                }
              }).then(function (resp) {
                console.log(resp.data);
                if(resp.data.code === 200){
                  Toast("登陆成功");
                  that.$store.commit("SET_USERINFO",resp.data.data.user);
                  that.$router.push('/user/index')
                }else {
                  Toast("手机号与验证码不匹配！！！")
                }
              });

            } else {
              console.log('error submit!!');
              return false;
            }
          });
        }


      },
      register(){
          this.$router.push("/registerType")
      },
      goBack(){
        this.$router.push("/login")
      }
    }
}
</script>
