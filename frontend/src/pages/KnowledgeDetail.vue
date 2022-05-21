<template>
  <el-container class="main-container" id="out-container">
    <el-card class="know-card">
      <div slot="header" class="clearfix">
        <span style="font-size: 25px">{{ $store.state.knowledge.name }}</span>
      </div>
      <div class="text item">
        <table style="width:370px;border-spacing: 30px;">
          <tbody>
          <tr>
            <td>类型</td>
            <td style="color:Orange">知识点</td>
          </tr>
          <tr>
            <td>父级章节</td>
            <td>
              <a href="javascript:void(0);" @click="toParent">
                {{ $store.state.knowledge.parent.name }}
              </a>
            </td>
          </tr>
          <tr>
            <td>详细内容</td>
            <td>
              <ul style="line-height: 50px">
                <li>
                  <a href="javascript:void(0);" @click="toChildren('info')">知识点详细学习</a>
                </li>
                <li v-if="$store.state.knowledge.pre.length>0">
                  <a href="javascript:void(0);" @click="toChildren('pre')">前驱知识点</a>
                </li>
                <li v-if="$store.state.knowledge.suc.length>0">
                  <a href="javascript:void(0);" @click="toChildren('suc')">后继知识点</a>
                </li>
                <li v-if="$store.state.knowledge.codes.length>0">
                  <a href="javascript:void(0);" @click="toChildren('code')">相关代码实例</a>
                </li>
                <li v-if="$store.state.knowledge.pptSet.length>0">
                  <a href="javascript:void(0);" @click="toChildren('ppt')">相关PPT资源</a>
                </li>
                <li v-if="$store.state.knowledge.mp4Set.length>0">
                  <a href="javascript:void(0);" @click="toChildren('video')">相关教学视频资源</a>
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
  name: 'KnowledgeDetail',
  created () {
    this.$router.replace('/main/knowledge/info')
  },
  methods: {
    toParent () {
      this.$store.commit('setChapter', this.$store.state.knowledge.parent)
      this.$router.push('/main/chapter')
    },
    toChildren (to) {
      this.$router.push('/main/knowledge/' + to)
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
