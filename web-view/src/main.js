// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import 'babel-polyfill'
import App from './App'
import router from './router'
import store from './vuex'
import i18n from './i18n'
import './mint'
import 'mint-ui/lib/style.css'
import './assets/js/flexible'
import '@/global/components.js'
import '@/global/filter.js'



Vue.config.productionTip = false


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
