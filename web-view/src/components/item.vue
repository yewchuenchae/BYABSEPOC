<template>
  <div class="item-comp">
    <div id="widgetFrame" style="width:1280px; height:800px"></div>
  </div>
</template>
<script>
export default {
  name: 'item',
  data(){
    return{}
  },
  props:['productId'],
  mounted(){
    let reference = this.productId;
    let language = 'EN';
    let country = 'SCHINT';
    let project = 'ANY';
    let token = 'Bearer 377feb05b91a96a12a6d06232db552c8';
    let widgetOptions = {};
    let wrapperInstance = seWidgetWrapper.startFromReference(reference, language, country, project, token, widgetOptions);
  }
}
</script>
<style lang="scss" scope>
  #widgetFrame{
    // width: 100% !important;
  }
</style>