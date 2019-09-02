<template>
    <div id="selection-page">
<<<<<<< HEAD
      <img src="./../../assets/img/selection_1.png" alt="">
      <p class="desc-box" @click="goGuide">Replacing Schneider Electric Relay</p>
      <p class="desc-box drive-box" @click="goGuide">Replacing Non-Schneider Drive</p>

=======
      <div class="head-img">
        <img src="./../../assets/img/selection_1.png" alt="">
      </div>
      <p class="desc-box" @click="goGuide">Relacing a Schneider Electric Relay</p>
      <p class="desc-box drive-box" @click="goGuide">Relacing a Non-Schneider Drive</p>
>>>>>>> 6dda5037bd20a542e5572691dae15b1fa9cd77a0
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
    img{
      display: block;
      height: 1.34rem;
      width: 4.50rem;
      margin: 2.67rem auto;
    }
    .desc-box{
      box-sizing: border-box;
      margin: 0.2rem 0.24rem 0;
      height: 1.2rem;
      text-align: center;
      line-height: 1.2rem;
      background: #FFF;
      border-radius: 8px;
      color: #666;
      box-shadow: 1px 0px 17px 3px rgba(61,205,88,0.1);
    }
    .drive-box{
      margin-top:0.5rem;
    }
  }

</style>
