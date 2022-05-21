<template>
  <el-container style="margin-left: 50px;">
    <el-container direction="vertical">
      <iframe id="iframe1" width="800" height="650" allowtransparency='yes'
              :src="'http://view.officeapps.live.com/op/view.aspx?src='+pptSet[currentPage].url"></iframe>
      <el-pagination layout="prev, pager, next" :total="pptSet.length"
                     :page-size="1" hide-on-single-page @current-change="pageChange"/>
    </el-container>
    <el-container direction="vertical">
      <el-button type="warning" :icon="starIndex>=0?'el-icon-star-on':'el-icon-star-off'" circle
                 style="height:50px;width: 50px;font-size: 25px;margin: 0 30px 0 250px" @click="changeStar"></el-button>
      <el-card style="margin: auto 20px;width:300px;height:500px">
        <div slot="header" class="clearfix">
          <span style="font-size: 22px">本PPT相关知识点</span>
        </div>
        <div class="text item" style="display: flex;align-items: center;justify-content: center;height: 370px">
          <ul style="font-weight: bolder;">
            <li v-for="k in pptSet[currentPage].knowledgeSet" :key="k.id" :value="k"
                v-if="k.id !==$store.state.knowledge.id">
              <a href="javascript:void(0);" @click="toKnowledge(k)"><p>{{ k.name }}</p></a>
            </li>
            <li>
              <p>{{ $store.state.knowledge.name }}</p>
            </li>
          </ul>
        </div>
      </el-card>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'KnowledgePPT',
  data () {
    return {
      pptSet: [{'id': '0', 'url': '', 'knowledgeSet': []}],
      currentPage: 0,
      starIndex: -1
    }
  },
  created () {
    this.pptSet = this.$store.state.knowledge.pptSet
    this.starIndex = this.findPPT()
  },
  methods: {
    pageChange (p) {
      this.currentPage = p - 1
      this.starIndex = this.findPPT()
    },
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
    },
    changeStar () {
      if (this.starIndex >= 0) {
        this.$axios.get('/starPPT/delete?id=' + this.pptSet[this.currentPage].id
        )
          .then(resp => {
            this.$store.state.starPPTSet.splice(this.starIndex, 1)
            this.$store.commit('setStarPPTSet', this.$store.state.starPPTSet)
            this.starIndex = -1
            this.$message({
              message: '取消收藏成功！',
              type: 'success'
            })
          })
          .catch(error => {
            console.log(error)
            this.$message({
              message: '取消收藏失败，请稍后重试。',
              type: 'error',
              showClose: true
            })
          })
      } else {
        this.$axios.get('/starPPT/add?id=' + this.pptSet[this.currentPage].id
        )
          .then(resp => {
            this.$store.state.starPPTSet.push({'pptID': this.pptSet[this.currentPage].id})
            this.$store.commit('setStarPPTSet', this.$store.state.starPPTSet)
            this.starIndex = this.$store.state.starPPTSet.length - 1
            this.$message({
              message: '收藏成功！',
              type: 'success'
            })
          })
          .catch(error => {
            console.log(error)
            this.$message({
              message: '收藏失败，请稍后重试。',
              type: 'error',
              showClose: true
            })
          })
      }
    },
    findPPT () {
      let id = this.pptSet[this.currentPage].id
      for (let i = 0; i < this.$store.state.starPPTSet.length; i++) {
        if (this.$store.state.starPPTSet[i].pptID === id) {
          return i
        }
      }
      return -1
    }
  }
}
</script>

<style scoped>

</style>
