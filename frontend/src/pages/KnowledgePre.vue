<template>
  <el-card style="margin: auto;width:600px;height:650px">
    <div slot="header" class="clearfix">
      <span style="font-size: 25px">前驱知识点</span>
    </div>
    <div class="text item" style="display: flex;align-items: center;justify-content: center;height: 500px">
      <ul style="font-weight: bolder;font-size: 22px;line-height: 55px;letter-spacing: 2px">
        <li v-for="k in $store.state.knowledge.pre" :key="k.id" :value="k">
          <a href="javascript:void(0);" @click="toKnowledge(k)">{{ k.name }}</a>
        </li>
      </ul>
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'KnowledgePre',
  methods: {
    toKnowledge (k) {
      this.$axios.get('/knowledge/get?id=' + k.id
      )
        .then(resp => {
          this.$store.commit('setKnowledge', resp.data)
          this.$router.replace('/main/knowledge/info')
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

</style>
