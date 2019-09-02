import Vue from 'vue';
// 自定义组件
import SimpleCropper from './../components/SimpleCropper.vue';

// 配置挂载组件
const components = {
    // 组件名称: 组件
    'SimpleCropper': SimpleCropper,
}

// 组件挂载
for(let i in components){
    const compItem = {
        install: function(Vue){
            Vue.component(i, components[i])
        }
    }
    Vue.use(compItem)
}