<template>
  <div id="app">
    <router-view></router-view>
    <router-view name="tabbar"></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
  data(){
    return{}
  },
  mounted(){
    let browser = {
        versions: (function() {
            var u = navigator.userAgent, app = navigator.appVersion;
            return {     //移动终端浏览器版本信息
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
            };
        })(),
        language: (navigator.browserLanguage || navigator.language).toLowerCase()
    }
     
    if (!browser.versions.mobile) {
      // document.getElementsByTagName('body')[0].className = 'web'     //非移动终端设置了480px宽度
    }
  }
}
</script>

<style>
@import './assets/style/reset.css';
#app {
  height:100%;
  margin: 0 auto;
}
.web{
  max-width: 480px;
  overflow: auto;
}
.web #app{
  overflow: auto;
}
</style>
