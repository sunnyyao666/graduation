// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import VideoPlayer from 'vue-video-player'
import 'vue-video-player/src/custom-theme.css'
import 'video.js/dist/video-js.css'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VideoPlayer)

const axios = require('axios')
Vue.prototype.$axios = axios
axios.defaults.baseURL = '/api'
axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'

// http request 拦截器
axios.interceptors.request.use(
  config => {
    if (store.state.token) {
      // 判断是否有token，若存在，每个http header加上token
      config.headers.Authorization = 'Bearer ' + store.state.token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// http response 拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    console.log(error.response)
    if (error.response.status === 403) {
      // 清除token 如果不是register/login, 跳转至login
      store.commit('logOut')
      if (router.currentRoute.path !== '/logIn' && router.currentRoute.path !== '/register') {
        router.replace('/logIn')
      }
    }
    return Promise.reject(error.response.data)
  }
)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  components: {App},
  template: '<App/>',
  store,
  router
})
