import Vue from 'vue';
// 自定义组件
import HelloWorld from './../components/hello-world.vue';

// 配置挂载组件
const components = {
    // 组件名称: 组件
    'my-dom': HelloWorld,
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