<template>
  <div id="base_login">
    <el-header style="height: 20%;padding-left: 5em;">
      <p style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
        登录JavaKG</p>
      <p style="margin: 0;padding:1% 0;text-align: left;background-color: white;">JavaKG使用Cookies进行用户身份鉴定</p>
    </el-header>
    <el-form :model="logInForm"
             :rules="rules"
             class="login_container"
             label-position="left"
             label-width="0px"
             v-loading="loading"
             ref="logInForm"
             style="height: 60%;margin-bottom: 50px">
      <h3 class="login_title">登录</h3>
      <el-form-item prop="username" style="margin: 45px;" label=" ">
        <label class="date">用户名:</label>
        <el-input type="text"
                  v-model="logInForm.username"
                  autocomplete="off"
                  placeholder="请填写用户名"
                  width="350px"
                  class="input"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="password" style="margin: 45px;" label=" ">
        <label class="date">密&nbsp;&nbsp;&nbsp;码:</label>
        <el-input type="password"
                  v-model="logInForm.password"
                  auto-complete="off"
                  placeholder="请填写密码"
                  width="350px"
                  class="input"
        >
        </el-input>
      </el-form-item>
      <el-form-item style="width: 100%;">
        <el-button type="primary"
                   style="width: 300px;background: dodgerblue;border-radius: 4px;height: 40px;margin-top: 50px"
                   @click="logIn('logInForm')">登录
        </el-button>
      </el-form-item>
      <p style="padding-top: 5px">
        如果您还没有账号,
        <router-link to="register" style="text-decoration: none;color: red;">
          请注册
        </router-link>
      </p>
      <p style="padding-top:5px">
        <router-link to="register" style="text-decoration: none;color: red;">
          忘记密码？
        </router-link>
      </p>
      <p style="padding-top:5px">
        <router-link to="register" style="text-decoration: none;color: red;">
          登录有问题？
        </router-link>
      </p>
    </el-form>
    <el-footer style="text-align: center;height:5% ">Copyright © 2022 FDU18SS YHT</el-footer>
  </div>

</template>

<script>
export default {
  name: 'LogIn',
  data () {
    const dataValid = (rule, value, callback) => {
      if (!value || value === '') {
        return callback(new Error('不能为空'))
      }
      return callback()
    }
    return {
      logInForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{required: true, message: '须填写用户名', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}],
        password: [{required: true, message: '须填写密码', trigger: 'blur'}, {validator: dataValid, trigger: 'blur'}]
      },
      loading: false
    }
  },
  methods: {
    logIn (formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$axios.post('/user/logIn', {
            username: this.logInForm.username,
            password: this.logInForm.password
          }
          )
            .then(resp => {
              if (resp.status === 200 && resp.headers.hasOwnProperty('token')) {
                this.$message({
                  message: '登录成功！',
                  type: 'success'
                })
                this.$store.commit('logIn', resp.headers)
                this.$store.commit('setUser', resp.data)
                this.$router.replace('/main')
              } else {
                this.$message({
                  message: '登录失败，请重试。',
                  type: 'error',
                  showClose: true
                })
                this.$store.commit('logOut')
              }
            })
            .catch(error => {
              console.log(error)
              this.$message({
                message: '用户名或密码错误，请检查！',
                type: 'error',
                showClose: true
              })
              this.$store.commit('logOut')
            })
        } else {
          this.$message({
            message: '请确保所有信息都已填写！',
            type: 'warning',
            showClose: true
          })
        }
      })
    }
  }
}
</script>

<style scoped>
#base_login {
  background: url("../assets/background/newbackground.jpg") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

p {
  margin: 0;
}

body {
  margin: 0;
  padding: 0;
}

.date {
  float: left;
  /*width: 0;*/
  padding-right: 12px;
  text-align: left;
  font-size: 17px;
}

.input {
  width: 350px;
  padding: 0;
}

.login_container {
  border-radius: 15px;
  background: #fff;
  background-clip: padding-box;
  margin: 0 auto;
  width: 800px;
  padding: 80px 35px 0 35px;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  margin: 0 auto 10px auto;
  text-align: center;
  color: #505458;
  font-size: 25px;
}

input {
  width: 300px;
  height: 24px;
  border: 2px solid #ccc;
  border-radius: 4px;
}
</style>
