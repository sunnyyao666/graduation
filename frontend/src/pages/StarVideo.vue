<template>
  <el-container style="margin-left: 50px" v-if="this.mp4Set.length>0 & this.$store.state.starMP4Set.length>0">
    <el-container direction="vertical">
      <div style="width: 800px;height: 450px;margin: 100px 0 50px">
        <video-player class="video-player vjs-custom-skin"
                      ref="videoPlayer"
                      :playsinline="true"
                      :options="playerOptions">
        </video-player>
      </div>
      <el-pagination layout="prev, pager, next" :total="mp4Set.length"
                     :page-size="1" hide-on-single-page @current-change="pageChange"/>
    </el-container>
    <el-container direction="vertical">
      <el-button type="warning" icon="el-icon-star-on" circle
                 style="height:50px;width: 50px;font-size: 25px;margin: 0 30px 0 250px" @click="changeStar"></el-button>
      <el-card style="margin: auto 20px;width:300px;height:500px">
        <div slot="header" class="clearfix">
          <span style="font-size: 22px">本视频相关知识点</span>
        </div>
        <div class="text item" style="display: flex;align-items: center;justify-content: center;height: 370px">
          <ul style="font-weight: bolder;">
            <li v-for="k in mp4Set[currentPage].knowledgeSet" :key="k.id" :value="k">
              <a href="javascript:void(0);" @click="toKnowledge(k)"><p>{{ k.name }}</p></a>
            </li>
          </ul>
        </div>
      </el-card>
    </el-container>
  </el-container>
  <el-container v-else>
    <h2 style="margin: 100px auto auto 100px;color: Orange;font-size: 40px">暂未有教学视频收藏！</h2>
  </el-container>
</template>

<script>
export default {
  name: 'StarVideo',
  data () {
    return {
      mp4Set: [{'id': '0', 'url': '', 'knowledgeSet': []}],
      currentPage: 0,
      starIndex: 0,
      playerOptions: {
        playbackRates: [0.5, 1.0, 1.5, 2.0],
        autoplay: false,
        muted: false,
        loop: false,
        preload: 'auto',
        language: 'zh-CN',
        aspectRatio: '16:9',
        fluid: true,
        sources: [{
          type: 'video/mp4',
          src: ''
        }],
        notSupportedMessage: '此视频暂无法播放，请稍后再试',
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true
        }
      }
    }
  },
  created () {
    this.$axios.get('/starMP4/getAll'
    )
      .then(resp => {
        this.mp4Set = resp.data
        if (this.mp4Set.length > 0) {
          this.playerOptions.sources[0].src = this.mp4Set[this.currentPage].url
          this.starIndex = this.findMP4()
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
      this.playerOptions.sources[0].src = this.mp4Set[this.currentPage].url
      this.starIndex = this.findMP4()
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
        this.$axios.get('/starMP4/delete?id=' + this.mp4Set[this.currentPage].id
        )
          .then(resp => {
            this.$store.state.starMP4Set.splice(this.starIndex, 1)
            this.$store.commit('setStarMp4Set', this.$store.state.starMP4Set)

            this.mp4Set.splice(this.currentPage, 1)
            if (this.currentPage >= this.$store.state.starMP4Set.length) {
              this.currentPage--
            }

            if (this.mp4Set.length > 0) {
              this.playerOptions.sources[0].src = this.mp4Set[this.currentPage].url
              this.starIndex = this.findMP4()
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
    findMP4 () {
      let id = this.mp4Set[this.currentPage].id
      for (let i = 0; i < this.$store.state.starMP4Set.length; i++) {
        if (this.$store.state.starMP4Set[i].mp4ID === id) {
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
