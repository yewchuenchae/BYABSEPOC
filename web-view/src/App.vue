<template>
  <div id="app">
      <router-view></router-view>
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
  margin: 0 auto;
}
</style>
