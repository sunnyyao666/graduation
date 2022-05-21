<template>
  <el-container class="main-container">
    <el-card class="know-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 25px">我的收藏</span>
      </div>
      <div class="text item">
        <table style="width:370px;border-spacing: 40px;">
          <tbody>
          <tr>
            <td style="height: 120px">用户名</td>
            <td>{{ this.$store.state.username }}</td>
          </tr>
          <tr>
            <td>我的收藏</td>
            <td>
              <ul style="line-height: 50px">
                <li>
                  <a href="javascript:void(0);" @click="toChildren('ppt')">PPT收藏</a>
                </li>
                <li>
                  <a href="javascript:void(0);" @click="toChildren('video')">教学视频收藏</a>
                </li>
              </ul>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </el-card>
    <el-container style="margin:30px 0 30px 430px">
      <router-view/>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Star',
  methods: {
    toChildren (to) {
      this.$router.push('/main/star/' + to)
    }
  }
}
</script>

<style scoped>
.know-card {
  width: 400px;
  position: fixed;
  margin: 30px;
  height: 600px;
}

.know-card td {
  text-align: center;
  font-size: 18px;
  font-weight: bolder;
  line-height: 35px;
}
</style>
