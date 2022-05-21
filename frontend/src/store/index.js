import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || null,
    username: localStorage.getItem('username') || null,
    starPPTSet: JSON.parse(localStorage.getItem('starPPTSet')) || null,
    starMP4Set: JSON.parse(localStorage.getItem('starMP4Set')) || null,
    lesson: JSON.parse(localStorage.getItem('lesson')) || null,
    chapter: JSON.parse(localStorage.getItem('chapter')) || null,
    knowledge: JSON.parse(localStorage.getItem('knowledge')) || null,
    historySearch: JSON.parse(localStorage.getItem('historySearch')) || null
  },
  mutations: {
    logIn (state, data) {
      localStorage.setItem('token', data.token)
      state.token = data.token
    },
    setUser (state, data) {
      localStorage.setItem('username', data.username)
      state.username = data.username
      localStorage.setItem('starPPTSet', JSON.stringify(data.starPPTSet))
      state.starPPTSet = data.starPPTSet
      localStorage.setItem('starMP4Set', JSON.stringify(data.starMP4Set))
      state.starMP4Set = data.starMP4Set
    },
    logOut (state) {
      // 移除token
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('starPPTSet')
      localStorage.removeItem('starMP4Set')
      localStorage.removeItem('chapter')
      localStorage.removeItem('knowledge')
      localStorage.removeItem('historySearch')
      state.token = null
      state.username = null
      state.starPPTSet = null
      state.starMP4Set = null
      state.chapter = null
      state.knowledge = null
      state.historySearch = null
    },
    setLesson (state, lesson) {
      localStorage.setItem('lesson', JSON.stringify(lesson))
      state.lesson = lesson
    },
    setChapter (state, chapter) {
      localStorage.setItem('chapter', JSON.stringify(chapter))
      state.chapter = chapter
    },
    setKnowledge (state, knowledge) {
      localStorage.setItem('knowledge', JSON.stringify(knowledge))
      state.knowledge = knowledge
    },
    setParent (state, parent) {
      state.knowledge.parent = parent
    },
    setCodes (state, codes) {
      state.knowledge.codes = codes
    },
    setPre (state, predecessors) {
      state.knowledge.pre = predecessors
    },
    setSuc (state, successors) {
      state.knowledge.suc = successors
    },
    setStarPPTSet (state, starPPTSet) {
      state.starPPTSet = starPPTSet
      localStorage.setItem('starPPTSet', JSON.stringify(starPPTSet))
    },
    setStarMp4Set (state, starMP4Set) {
      state.starMP4Set = starMP4Set
      localStorage.setItem('starMP4Set', JSON.stringify(starMP4Set))
    },
    setHistorySearch (state, historySearch) {
      state.historySearch = historySearch
      localStorage.setItem('historySearch', JSON.stringify(historySearch))
    }
  },
  actions: {}
})
