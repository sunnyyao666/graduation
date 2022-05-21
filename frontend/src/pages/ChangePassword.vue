<template>
  <el-form :model="passwordForm" :rules="rules" class="password_container" label-position="left"
           label-width="0px" v-loading="false" ref="passwordForm" style="height: 80%;">
    <h3 class="password_title">修改密码</h3>
    <el-form-item prop="oldPassword" label=" " style="margin: 45px;">
      <label class="date">原&nbsp;&nbsp;密&nbsp;&nbsp;码:</label>
      <el-input type="password" v-model="passwordForm.oldPassword"
                auto-complete="off" placeholder="请填写原密码" class="input">
      </el-input>
    </el-form-item>
    <el-form-item prop="newPassword" label=" " style="margin: 45px;">
      <label class="date">新&nbsp;&nbsp;密&nbsp;&nbsp;码:</label>
      <el-input type="password" v-model="passwordForm.newPassword"
                auto-complete="off" placeholder="请填写新密码" class="input">
      </el-input>
    </el-form-item>
    <el-form-item prop="repassword" label=" " style="margin: 45px;">
      <label class="date">确认密码:</label>
      <el-input type="password" v-model="passwordForm.repassword"
                auto-complete="off" placeholder="请确认新密码" class="input">
      </el-input>
    </el-form-item>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 40%;background: dodgerblue;border-radius: 4px;height: 40px;"
                 v-on:click="change('passwordForm')">修改密码
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: 'ChangePassword',
  data () {
    const oldPasswordCheck = (rule, value, callback) => {
      if (!value || value === '') {
        return callback(new Error('不能为空'))
      } else {
        return callback()
      }
    }
    const newPasswordCheck = (rule, value, callback) => {
      const reg = /^(?![\d]+$)(?![a-zA-Z]+$)(?![_-]+$)[\da-zA-Z_-]{6,32}$/
      if (!value || value === '') {
        return callback(new Error('不能为空'))
      } else if (!reg.test(value)) {
        return callback(new Error('密码需要字母，数字或者特殊字符（-_）至少包含两种，长度为6-32个字符'))
      } else if (value.indexOf(this.$store.state.username) !== -1) {
        return callback(new Error('密码中不得含有用户名'))
      } else {
        if (this.passwordForm.repassword !== '') {
          this.$refs['passwordForm'].validateField('repassword')
        }
        return callback()
      }
    }
    const repasswordCheck = (rule, value, callback) => {
      if (!value || value === '') {
        return callback(new Error('不能为空'))
      } else if (value !== this.passwordForm.newPassword) {
        return callback(new Error('确认密码与密码不一致'))
      } else {
        return callback()
      }
    }

    return {
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        repassword: ''
      },

      rules: {
        // blur 失去鼠标焦点时触发验证
        oldPassword: [{required: true, message: '须填写原密码', trigger: 'blur'}, {
          validator: oldPasswordCheck,
          trigger: 'blur'
        }],
        newPassword: [{required: true, message: '须填写新密码', trigger: 'blur'}, {
          validator: newPasswordCheck,
          trigger: 'blur'
        }],
        repassword: [{required: true, message: '须确认新密码', trigger: 'blur'}, {
          validator: repasswordCheck,
          trigger: 'blur'
        }]
      }
    }
  },
  methods: {
    change (formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$axios.post('/user/change', {
            password: this.passwordForm.oldPassword,
            newPassword: this.passwordForm.newPassword
          })
            .then(resp => {
              if (resp.status === 200 && resp.data.hasOwnProperty('id')) {
                this.$message({
                  message: '修改密码成功!',
                  type: 'success'
                })
                this.$router.replace('/main/star')
              } else {
                this.$message({
                  message: '修改密码失败，请稍后再试。',
                  type: 'error',
                  showClose: true
                })
              }
            })
            .catch(error => {
              console.log(error)
              this.$message({
                message: '原密码输入有误，请检查！',
                type: 'error',
                showClose: true
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

.password_container {
  border-radius: 15px;
  background: #fff;
  background-clip: padding-box;
  margin: 0 auto;
  width: 800px;
  padding: 80px 35px 0 35px;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.password_title {
  margin: 0 auto 10px auto;
  text-align: center;
  color: #505458;
  font-size: 25px;
}
</style>
