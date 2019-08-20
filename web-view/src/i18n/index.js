import Vue from 'vue';
import store from './../vuex'
import VueI18n from 'vue-i18n'
import zh_CN from './config/zh-CN'

Vue.use(VueI18n)
const i18n = new VueI18n({
    locale: store.state.language || 'zh_CN',
    messages: {
        zh_CN: zh_CN
    }
})

export default i18n;