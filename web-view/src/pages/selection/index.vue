<template>
    <div id="selection-page">
      <div class="head-img">
        <img src="./../../assets/img/selection_1.png" alt="">
      </div>
      <p class="desc-box" @click="goGuide">Relacing a Schneider Electric Relay</p>
      <p class="desc-box drive-box" @click="goGuide">Relacing a Non-Schneider Drive</p>

    </div>
</template>

<script>
    export default {
        name: "index",
        methods: {
          goGuide(){
            this.$router.push({
              name: 'guide'
            })
          }
        }
    }
</script>

<style lang="scss" scoped>
  #selection-page{
    overflow: hidden;
    height:100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    .head-img{
      margin-top:3.57rem;
      img{
        height:1.34rem;
        width:4.50rem;
      }
    }
    .desc-box{
      margin-top:2rem;
      height:1.2rem;
      width:7.03rem;
      text-align: center;
      line-height: 1.2rem;
      background:rgba(255,255,255,1);
      box-shadow:1px 0px 17px 3px rgba(61,205,88,0.1);
      border-radius:8px;
      /*font-size:36px;*/
      font-family:PingFangSC;
      font-weight:400;
      color:rgba(102,102,102,1);
    }
    .drive-box{
      margin-top:0.5rem;
    }
  }

</style>
