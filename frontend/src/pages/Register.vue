<template>
  <div id="base_register">
    <el-header style="height: 20%;padding-left: 5em;">
      <p style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
        创建JavaKG账户</p>
    </el-header>
    <el-form :model="registerForm" :rules="rules" class="register_container" label-position="left"
             label-width="0px" v-loading="loading" ref="registerForm" style="height: 60%;">
      <h3 class="register_title">注册</h3>
      <el-form-item prop="username" label=" " style="margin: 45px;">
        <label class="date">用&nbsp;&nbsp;户&nbsp;&nbsp;名:</label>
        <el-input type="text" v-model="registerForm.username"
                  auto-complete="off" placeholder="请填写用户名" class="input">
        </el-input>
      </el-form-item>
      <el-form-item prop="password" label=" " style="margin: 45px;">
        <label class="date">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码:</label>
        <el-input type="password" v-model="registerForm.password"
                  auto-complete="off" placeholder="请填写密码" class="input">
        </el-input>
      </el-form-item>
      <el-form-item prop="repassword" label=" " style="margin: 45px;">
        <label class="date">确认密码:</label>
        <el-input type="password" v-model="registerForm.repassword"
                  auto-complete="off" placeholder="请确认密码" class="input">
        </el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="primary" style="width: 40%;background: dodgerblue;border-radius: 4px;height: 40px;"
                   v-on:click="register('registerForm')">注册
        </el-button>
      </el-form-item>
      <p>
        已有账户？
        <router-link to="login" style="text-decoration: none;color: red;">
          登录
        </router-link>
      </p>
    </el-form>
    <el-footer style="text-align: center;height: 10%;padding-top: 20px;">Copyright © 2022 FDU18SS YHT</el-footer>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data () {
    const usernameCheck = (rule, value, callback) => {
      const reg = /^[-a-zA-Z]([a-zA-Z0-9]|[-]|[_]){4,31}$/
      if (!value || value === '') {
        return callback(new Error('不能为空'))
      } else if (!reg.test(value)) {
        return callback(new Error('账号只能包含字母，数字或两种特殊字符（-_）且只能以字母或-开头，长度为5-32个字符'))
      } else {
        return callback()
      }
    }
    const passwordCheck = (rule, value, callback) => {
      const reg = /^(?![\d]+$)(?![a-zA-Z]+$)(?![_-]+$)[\da-zA-Z_-]{6,32}$/
      if (!value || value === '') {
        return callback(new Error('不能为空'))
      } else if (!reg.test(value)) {
        return callback(new Error('密码需要字母，数字或者特殊字符（-_）至少包含两种，长度为6-32个字符'))
      } else if (value.indexOf(this.registerForm.username) !== -1) {
        return callback(new Error('密码中不得含有用户名'))
      } else {
        if (this.registerForm.repassword !== '') {
          this.$refs['registerForm'].validateField('repassword')
        }
        return callback()
      }
    }
    const repasswordCheck = (rule, value, callback) => {
      if (!value || value === '') {
        return callback(new Error('不能为空'))
      } else if (value !== this.registerForm.password) {
        return callback(new Error('确认密码与密码不一致'))
      } else {
        return callback()
      }
    }

    return {
      registerForm: {
        username: '',
        password: '',
        repassword: ''
      },

      rules: {
        // blur 失去鼠标焦点时触发验证
        username: [{required: true, message: '须填写用户名', trigger: 'blur'}, {validator: usernameCheck, trigger: 'blur'}],
        password: [{required: true, message: '须填写密码', trigger: 'blur'}, {validator: passwordCheck, trigger: 'blur'}],
        repassword: [{required: true, message: '须确认密码', trigger: 'blur'}, {
          validator: repasswordCheck,
          trigger: 'blur'
        }]
      },
      loading: false
    }
  },
  methods: {
    register (formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$axios.post('/user/register', {
            username: this.registerForm.username,
            password: this.registerForm.password
          })
            .then(resp => {
              // 根据后端的返回数据修改
              if (resp.status === 200 && resp.data.hasOwnProperty('id')) {
                // 跳转到login
                this.$message({
                  message: '注册成功!',
                  type: 'success'
                })
                this.$router.replace('/logIn')
              } else {
                this.$message({
                  message: '注册失败，请稍后再试。',
                  type: 'error',
                  showClose: true
                })
              }
            })
            .catch(error => {
              console.log(error)
              this.$message({
                message: '用户名已被注册，请更换!',
                type: 'error',
                showClose: true,
                duration: 0
              })
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
#base_register {
  background: url("../assets/background/newbackground.jpg") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

p {
  margin: 0;
}

.date {
  float: left;
  padding-right: 12px;
  text-align: left;
  font-size: 17px;
}

.input {
  width: 350px;
  padding: 0;
}

.register_container {
  border-radius: 15px;
  background: #fff;
  background-clip: padding-box;
  margin: 0 auto;
  width: 800px;
  padding: 80px 35px 0 35px;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.register_title {
  margin: 0 auto 10px auto;
  text-align: center;
  color: #505458;
  font-size: 25px;
}
</style>
