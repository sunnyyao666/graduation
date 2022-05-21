<template>
  <el-container class="main-container">
    <el-card class="node-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 25px">{{ $store.state.lesson.name }}</span>
      </div>
      <div class="text item">
        <table style="width:750px;border-spacing: 50px;">
          <tbody>
          <tr>
            <td>类型</td>
            <td style="color:Green">课程</td>
          </tr>
          <tr style="height: 400px">
            <td>包含的知识点</td>
            <td>
              <ul>
                <li v-for="c in $store.state.lesson.chapters" :key="c.name" value="c.name">
                  <a href="javascript:void(0);" @click="toChapter(c)">
                    {{ c.name }}
                  </a>
                </li>
              </ul>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </el-card>
  </el-container>
</template>

<script>
export default {
  name: 'LessonDetail',
  methods: {
    toChapter (chapter) {
      this.$axios.get('/chapter/get?id=' + chapter.id
      )
        .then(resp => {
          this.$store.commit('setChapter', resp.data)
          this.$router.push('/main/chapter')
        })
        .catch(error => {
          console.log(error)
          this.$message({
            message: '章节获取失败，请稍后重试。',
            type: 'error',
            showClose: true
          })
        })
    }
  }
}
</script>

<style scoped>
.node-card {
  margin: 30px auto;
  width: 800px;
}

.node-card td {
  width: 50%;
  text-align: left;
  font-size: 18px;
  font-weight: bolder;
  line-height: 35px;
}

.el-pagination::before, .el-pagination::after {
  display: none;
}
</style>
