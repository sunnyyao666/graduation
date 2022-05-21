<template>
  <el-container class="main-container">
    <el-card class="node-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 25px">{{ $store.state.chapter.name }}</span>
      </div>
      <div class="text item">
        <table style="width:750px;border-spacing: 50px;">
          <tbody>
          <tr>
            <td>类型</td>
            <td style="color:Pink">章节</td>
          </tr>
          <tr style="height: 400px">
            <td>包含的知识点</td>
            <td>
              <ul>
                <li v-for="k in knowledgeSet" :key="k.name" value="k.name">
                  <a href="javascript:void(0);" @click="toKnowledge(k)">
                    {{ k.name }}
                  </a>
                </li>
              </ul>
              <el-pagination small layout="prev, pager, next" :total="$store.state.chapter.knowledgeSet.length"
                             :page-size="8" hide-on-single-page style="margin: 30px 40px"
                             @current-change="pageChange"/>
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
  name: 'ChapterDetail',
  data () {
    return {
      knowledgeSet: []
    }
  },
  mounted () {
    const ks = this.$store.state.chapter.knowledgeSet
    if (ks.length <= 8) {
      this.knowledgeSet = ks
    } else {
      this.knowledgeSet = ks.slice(0, 8)
    }
  },
  methods: {
    pageChange (p) {
      this.knowledgeSet = this.$store.state.chapter.knowledgeSet.slice((p - 1) * 8, p * 8)
    },
    toKnowledge (knowledge) {
      this.$axios.get('/knowledge/get?id=' + knowledge.id
      )
        .then(resp => {
          this.$store.commit('setKnowledge', resp.data)
          this.$router.push('/main/knowledge')
        })
        .catch(error => {
          console.log(error)
          this.$message({
            message: '知识点获取失败，请稍后重试。',
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
