<template>
  <div id="app">
    <keep-alive>
      <router-view></router-view>
    </keep-alive>
    <router-view name="tabbar"></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
@import './assets/style/reset.css';
#app {
  height:100%;
}
</style>
