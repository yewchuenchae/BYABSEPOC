import Vue from 'vue';
import store from './../vuex'
import VueI18n from 'vue-i18n'
import zh_CN from './config/zh-CN'
import EN from './config/EN'
import RU from './config/RU'
import PT from './config/PT'

Vue.use(VueI18n)
const i18n = new VueI18n({
    locale: store.state.language || 'EN',
    messages: {
        ZH: zh_CN,
        EN: EN,
        RU: RU,
        PT: PT,
    }
})

export default i18n;