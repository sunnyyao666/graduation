<template>
  <el-container style="margin-left: 50px;" v-if="this.pptSet.length>0 & this.$store.state.starPPTSet.length>0">
    <el-container direction="vertical">
      <iframe id="iframe1" width="800" height="650" allowtransparency='yes'
              :src="'http://view.officeapps.live.com/op/view.aspx?src='+pptSet[currentPage].url"></iframe>
      <el-pagination layout="prev, pager, next" :total="pptSet.length"
                     :page-size="1" hide-on-single-page @current-change="pageChange"/>
    </el-container>
    <el-container direction="vertical">
      <el-button type="warning" icon="el-icon-star-on" circle
                 style="height:50px;width: 50px;font-size: 25px;margin: 0 30px 0 250px" @click="changeStar"></el-button>
      <el-card style="margin: auto 20px;width:300px;height:500px">
        <div slot="header" class="clearfix">
          <span style="font-size: 22px">本PPT相关知识点</span>
        </div>
        <div class="text item" style="display: flex;align-items: center;justify-content: center;height: 370px">
          <ul style="font-weight: bolder;">
            <li v-for="k in pptSet[currentPage].knowledgeSet" :key="k.id" :value="k">
              <a href="javascript:void(0);" @click="toKnowledge(k)"><p>{{ k.name }}</p></a>
            </li>
          </ul>
        </div>
      </el-card>
    </el-container>
  </el-container>
  <el-container v-else>
    <h2 style="margin: 100px auto auto 100px;color: Orange;font-size: 40px">暂未有PPT收藏！</h2>
  </el-container>
</template>

<script>
export default {
  name: 'StarPPT',
  data () {
    return {
      pptSet: [{'id': '0', 'url': '', 'knowledgeSet': []}],
      currentPage: 0,
      starIndex: 0
    }
  },
  created () {
    this.$axios.get('/starPPT/getAll'
    )
      .then(resp => {
        this.pptSet = resp.data
        if (this.pptSet.length > 0) {
          this.starIndex = this.findPPT()
        }
      })
      .catch(error => {
        console.log(error)
        this.$message({
          message: '获取收藏失败，请稍后重试。',
          type: 'error',
          showClose: true
        })
        this.$router.replace('/main/star')
      })
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
      this.$confirm('是否确认取消收藏?', '确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.get('/starPPT/delete?id=' + this.pptSet[this.currentPage].id
        )
          .then(resp => {
            this.$store.state.starPPTSet.splice(this.starIndex, 1)
            this.$store.commit('setStarPPTSet', this.$store.state.starPPTSet)

            this.pptSet.splice(this.currentPage, 1)
            if (this.currentPage >= this.$store.state.starPPTSet.length) {
              this.currentPage--
            }
            if (this.pptSet.length > 0) {
              this.starIndex = this.findPPT()
            }
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
      }).catch(() => {
      })
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
