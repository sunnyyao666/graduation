<template>
  <el-container id="base_main">
    <el-header style="height: 18%;padding-left: 5em;">
      <el-row style="background-color: White">
        <el-col :span="8">
          <p
            style="margin: 0;padding-top: 2%;text-align: left;background-color: white;font-size: 30px;font-weight: bold;">
            JavaKG</p>
          <p style="margin: 0;padding:1% 0;text-align: left;background-color: White;">Java程序设计基础知识点集合</p>
        </el-col>
        <el-col :span="13" class="center">
          <el-autocomplete
            v-model="search"
            placeholder="搜索知识点" style="width:400px"
            :fetch-suggestions="querySearch"
            @select="handleSelect"
          >
            <el-button slot="append" icon="el-icon-search" id="search"></el-button>
          </el-autocomplete>
        </el-col>
        <el-col :span="2" style="padding-top: 40px">
          <router-link to="Star" style="text-decoration: none;"
                       onmouseover="this.style.fontWeight='bold'"
                       onmouseout="this.style.fontWeight='normal'">
            <el-tooltip content="前往个人收藏页面">
              <p>
                {{ this.$store.state.username }}
              </p>
            </el-tooltip>
          </router-link>
        </el-col>
        <el-col :span="1">
          <el-popover placement="bottom" v-model="visible">
            <p>确认要登出吗？</p>
            <el-row style="text-align: right;margin-top:7px;">
              <el-button size="mini" @click="visible=false">否</el-button>
              <el-button type="primary" size="mini" @click="logOut">是</el-button>
            </el-row>
            <el-button plain slot="reference" style="margin-top: 30px">
              登出
            </el-button>
          </el-popover>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="200px" style="margin-left: 50px;text-align: left;font-weight:bolder">
        <el-menu background-color="#EBEEF5" :default-active="activeMenu" router :default-openeds="['1','2']">
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-share"></i>图谱相关</template>
            <el-menu-item-group>
              <el-menu-item index="/main/kg">图谱详情</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="节点详情">
              <el-menu-item index="/main/lesson">课程详情</el-menu-item>
              <el-menu-item index="/main/chapter">章节详情</el-menu-item>
              <el-menu-item index="/main/knowledge">知识点详情</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-user-solid"></i>个人相关</template>
            <el-menu-item-group>
              <el-menu-item index="/main/star">我的收藏</el-menu-item>
              <el-menu-item index="/main/password">修改密码</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <keep-alive include="KG">
        <router-view/>
      </keep-alive>
    </el-container>
    <el-footer style="text-align: center;height: 10%;padding-top: 20px;">Copyright © 2022 FDU18SS YHT</el-footer>
  </el-container>
</template>

<script>
export default {
  name: 'Main',
  data () {
    return {
      visible: false,
      search: '',
      allKnowledge: []
    }
  },
  methods: {
    logOut () {
      this.$message({
        message: '退出成功！',
        type: 'success'
      })
      this.$store.commit('logOut')
      this.$router.replace('/logIn')
    },
    pushKG () {
      this.$router.push('/main/kg')
    },
    querySearch (queryString, cb) {
      let results = []
      if (queryString === '') {
        return results
      }
      for (let i = 0; i < this.allKnowledge.length; i++) {
        let knowledge = this.allKnowledge[i]
        if (knowledge.name.toLowerCase().indexOf(queryString.toLowerCase()) >= 0) {
          results.push({'value': knowledge.name, 'id': knowledge.id})
        }
      }
      cb(results)
    },
    handleSelect (k) {
      this.search = ''
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
  },
  mounted () {
    this.$axios.get('/knowledge/getAll'
    )
      .then(resp => {
        this.allKnowledge = resp.data
      })
      .catch(error => {
        console.log(error)
        this.$message({
          message: '获取知识点失败，请稍后重试。',
          type: 'error',
          showClose: true
        })
      })
    this.pushKG()
  },
  computed: {
    activeMenu () {
      if (this.$route.meta.mainPath) {
        return this.$route.meta.mainPath
      }
      return this.$route.path
    }
  }
}
</script>

<style scoped>
#base_main {
  background: url("../assets/background/newbackground.jpg") no-repeat center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}

p {
  margin: 0;
}

.child-item .el-menu-item {
  background-color: #1F2D3D !important;
}

.child-item .el-menu-item:hover {
  background-color: #001528 !important;
}

.child-item .el-submenu__title {
  background-color: #1F2D3D !important;
}

.child-item .el-submenu__title:hover {
  background-color: #001528 !important;
}

.center {
  margin-top: 15px;
  padding-right: 500px;
}

#search {
  background-color: #ffc300;
  border-radius: 0;
}
</style>
