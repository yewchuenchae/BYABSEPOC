## 项目说明
	
>项目采用了vue-cli(2.9.6)脚手架创建,引用了[mint-ui](http://mint-ui.github.io/#!/zh-cn)组件库，采用了[scss](https://www.sass.hk/)预编译语言，已经配置了组件按需加载!  
更多操作可参考[http://vuejs-templates.github.io/webpack/](http://vuejs-templates.github.io/webpack/) 与 [https://vue-loader-v14.vuejs.org](https://vue-loader-v14.vuejs.org)
>>公共路由配置文件: src/router/public-router.js
>>鉴权路由配置文件: src/router/private-router.js  
>>页面组件: src/pages/*  
>>组件文件: src/components/*  
>>全局状态管理: src/store/*  
>>全局组件: src/global/components.js 
>>全局过滤器: src/global/filter.js 
>>方法集: src/utils/*   
>>全局scss变量文件: src/assets/style/public.scss  
>>开发环境全局变量设置: cofig/dev.env.js  
>>生产环境全局变量设置: cofig/prod.env.js  

***
文件命名规范：  
文件名使用语义化的英文名称，命名格式可参考下方格式   
文件名称：使用 - 拼接  
组件名称：大驼峰  
变量名称：小驼峰  

***
	
### 安装
```
npm install
```
### 启动项目
```
// 方法一
npm run dev
// 方法二
npm start
```
### 普通打包
```
npm run build
```
> 保留 js的关系映射文件 *.js.map  
> 保留 css的关系映射文件 *.css.map  
> 保留控制台输出  

### 精简打包
```
npm run uat
```
> 去除 js的关系映射文件 *.js.map  
> 去除 css的关系映射文件 *.css.map  
> 去除控制台输出  


## 组件说明
组件名称|作用|api
:---|:---|:--- 
vuex|全局状态管理|[https://vuex.vuejs.org/zh/guide/](https://vuex.vuejs.org/zh/guide/)  
vue-router|路由管理|[https://vuex.vuejs.org/zh/guide/](https://vuex.vuejs.org/zh/guide/)  
axios|接口交互|[https://www.kancloud.cn/yunye/axios/234845](https://www.kancloud.cn/yunye/axios/234845)  
vue-i18n|国际化|[http://kazupon.github.io/vue-i18n/](http://kazupon.github.io/vue-i18n/)  
babel-polyfill|兼容IE|[https://babeljs.io/docs/en/6.26.3/babel-polyfill](https://babeljs.io/docs/en/6.26.3/babel-polyfill)  
## 前后端数据交互封装说明
```
待完善
```
