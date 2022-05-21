import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '../pages/HelloWorld'
import LogIn from '../pages/LogIn'
import Register from '../pages/Register'
import Main from '../pages/Main'
import KG from '../pages/KG'
import LessonDetail from '../pages/LessonDetail'
import ChapterDetail from '../pages/ChapterDetail'
import KnowledgeDetail from '../pages/KnowledgeDetail'
import KnowledgeInfo from '../pages/KnowledgeInfo'
import KnowledgeCode from '../pages/KnowledgeCode'
import KnowledgePre from '../pages/KnowledgePre'
import KnowledgeSuc from '../pages/KnowledgeSuc'
import store from '../store'
import {Message} from 'element-ui'
import KnowledgePPT from '../pages/KnowledgePPT'
import KnowledgeVideo from '../pages/KnowledgeVideo'
import Star from '../pages/Star'
import StarPPT from '../pages/StarPPT'
import StarVideo from '../pages/StarVideo'
import ChangePassword from '../pages/ChangePassword'

Vue.use(Router)

const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new Router({
  routes: [{
    path: '/', name: 'HelloWorld', redirect: '/logIn', component: HelloWorld
  }, {
    path: '/logIn', name: 'LogIn', component: LogIn, meta: {tag: 'logged'}
  }, {
    path: '/register', name: 'Register', component: Register, meta: {tag: 'logged'}
  }, {
    path: '/main',
    name: 'Main',
    component: Main,
    meta: {requiresAuth: true},
    children: [
      {path: 'kg', name: 'KG', component: KG},
      {
        path: 'lesson',
        name: 'LessonDetail',
        component: LessonDetail,
        beforeEnter (to, from, next) {
          if (store.state.lesson) {
            next()
          } else {
            Message({
              message: '请先选择课程', type: 'error', showClose: true
            })
            router.replace('/main/kg')
          }
        }
      },
      {
        path: 'chapter',
        name: 'ChapterDetail',
        component: ChapterDetail,
        beforeEnter (to, from, next) {
          if (store.state.chapter) {
            next()
          } else {
            Message({
              message: '请先选择一个章节', type: 'error', showClose: true
            })
            router.replace('/main/kg')
          }
        }
      },
      {
        path: 'knowledge',
        name: 'KnowledgeDetail',
        component: KnowledgeDetail,
        beforeEnter (to, from, next) {
          if (store.state.knowledge) {
            next()
          } else {
            Message({
              message: '请先选择一个知识点', type: 'error', showClose: true
            })
            router.replace('/main/kg')
          }
        },
        children: [
          {path: 'info', name: 'KnowledgeInfo', component: KnowledgeInfo, meta: {mainPath: '/main/knowledge'}},
          {path: 'code', name: 'KnowledgeCode', component: KnowledgeCode, meta: {mainPath: '/main/knowledge'}},
          {path: 'pre', name: 'KnowledgePre', component: KnowledgePre, meta: {mainPath: '/main/knowledge'}},
          {path: 'suc', name: 'KnowledgeSuc', component: KnowledgeSuc, meta: {mainPath: '/main/knowledge'}},
          {path: 'ppt', name: 'KnowledgePPT', component: KnowledgePPT, meta: {mainPath: '/main/knowledge'}},
          {path: 'video', name: 'KnowledgeVideo', component: KnowledgeVideo, meta: {mainPath: '/main/knowledge'}}
        ]
      },
      {
        path: 'star',
        name: 'Star',
        component: Star,
        children: [
          {path: 'ppt', name: 'StarPPT', component: StarPPT, meta: {mainPath: '/main/star'}},
          {path: 'video', name: 'StarVideo', component: StarVideo, meta: {mainPath: '/main/star'}}
        ]
      },
      {path: 'password', name: 'ChangePassword', component: ChangePassword}
    ]
  }]
})

// 前端登录拦截
router.beforeEach(function (to, from, next) {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (store.state.token) {
      next()
    } else {
      Message({
        message: '账号未登录', type: 'error', showClose: true
      })
      next({
        path: '/logIn'
      })
    }
  } else if (to.meta.tag === 'logged') {
    if (store.state.token) {
      Message({
        message: '账号已登录',
        showClose: true
      })
      next({
        path: '/main'
      })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
