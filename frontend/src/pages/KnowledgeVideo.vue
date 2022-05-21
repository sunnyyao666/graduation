<template>
  <el-container style="margin-left: 50px">
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
      <el-button type="warning" :icon="starIndex>=0?'el-icon-star-on':'el-icon-star-off'" circle
                 style="height:50px;width: 50px;font-size: 25px;margin: 0 30px 0 250px" @click="changeStar"></el-button>
      <el-card style="margin: auto 20px;width:300px;height:500px">
        <div slot="header" class="clearfix">
          <span style="font-size: 22px">本视频相关知识点</span>
        </div>
        <div class="text item" style="display: flex;align-items: center;justify-content: center;height: 370px">
          <ul style="font-weight: bolder;">
            <li v-for="k in mp4Set[currentPage].knowledgeSet" :key="k.id" :value="k"
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
  name: 'KnowledgeVideo',
  data () {
    return {
      mp4Set: [{'id': '0', 'url': '', 'knowledgeSet': []}],
      currentPage: 0,
      starIndex: -1,
      playerOptions: {
        playbackRates: [0.5, 1.0, 1.5, 2.0],
        autoplay: false,
        muted: false,
        loop: false,
        preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [{
          type: 'video/mp4', // 类型
          src: '' // url地址
        }],
        notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true, // 当前时间和持续时间的分隔符
          durationDisplay: true, // 显示持续时间
          remainingTimeDisplay: false, // 是否显示剩余时间功能
          fullscreenToggle: true // 是否显示全屏按钮
        }
      }
    }
  },
  created () {
    this.mp4Set = this.$store.state.knowledge.mp4Set
    this.playerOptions.sources[0].src = this.mp4Set[this.currentPage].url
    this.starIndex = this.findMP4()
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
      if (this.starIndex >= 0) {
        this.$axios.get('/starMP4/delete?id=' + this.mp4Set[this.currentPage].id
        )
          .then(resp => {
            this.$store.state.starMP4Set.splice(this.starIndex, 1)
            this.$store.commit('setStarMp4Set', this.$store.state.starMP4Set)
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
        this.$axios.get('/starMP4/add?id=' + this.mp4Set[this.currentPage].id
        )
          .then(resp => {
            this.$store.state.starMP4Set.push({'mp4ID': this.mp4Set[this.currentPage].id})
            this.$store.commit('setStarMp4Set', this.$store.state.starMP4Set)
            this.starIndex = this.$store.state.starMP4Set.length - 1
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
