import Vue from 'vue'
import i18n from './../i18n'
import store from './../vuex'
Vue.prototype.utils = {
    /** 
     * @method 设置当前语言
     * @param {String} language 语言类型
     * @param {Function} cb 修改语言后的回调函数 修改成功函数入参为true 修改失败函数入参为false
    */
    setLanguage(language, cb){
        // 判断要设置的语言是否存在
        let flag = false;
        for(let i in i18n.messages){
            if(i == language){
                flag = true
            }
        }
        // 修改当前语言
        let status = null;
        if(flag){
            i18n.locale = language;
            store.commit('SET_LANGUAGE', language);
            status = true;
        }else{
            status = false;
        }
        // 执行回调函数
        cb && cb(status);
    },
}
/** 
 * @method loading全局调用
 * @param {Boolean} status loading状态
*/
Vue.prototype.spin = function (status) {
    store.commit('SET_LOADING', status)
}