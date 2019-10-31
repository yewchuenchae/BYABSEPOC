<template>
  <div id="home-page">
    <div class="top">
      <img src="./../../assets/img/language_logo.png" alt="">
      <span @click="open">{{$t('label.global')}}（{{
        {ZH: '中文', EN: 'English',RU: 'русский', PT: 'português'}[$i18n.locale]
        }}）</span>
      <img src="./../../assets/img/next.png" alt="">
    </div>
    <div class="logo-box">
      <!-- <img src="./../../assets/img/home_logo.png" alt="" class="logo"> -->
      <img src="./../../assets/img/logo_placeholder.png" alt="" class="logo">
    </div>
    <p class="home-text">{{$t('message.homeText')}}</p>
    <div class="button" @click="goSearch">
      <img src="./../../assets/img/home_camera.png" alt="">
      <span>{{$t('button.homeBtn')}}</span>
    </div>
    <mt-popup
      v-model="popupVisible"
      position="bottom">
      <div class="language-list">
        <p 
          :class="$i18n.locale == 'EN' ? 'active' : ''"
          @click="changeLanguage('EN')">
          English
        </p>
        <p 
          :class="$i18n.locale == 'ZH' ? 'active' : ''"
          @click="changeLanguage('ZH')"
        >中文</p>
        <p 
          :class="$i18n.locale == 'RU' ? 'active' : ''"
          @click="changeLanguage('RU')"
        >русский</p>
        <p 
          :class="$i18n.locale == 'PT' ? 'active' : ''"
          @click="changeLanguage('PT')"
        >português</p>
      </div>
    </mt-popup>
  </div>
</template>
<script>
/** 
 * @author ny
 * @class 首页
*/
export default {
  name: 'Home',
  data(){
    return{
      popupVisible: false
    }
  },
  methods: {
    /** 
     * @method 打开语言切换弹窗
    */
    open(){
      this.popupVisible = true;
    },
    /** 
     * @method 切花语言
     * @param {String} 语言编码
    */
    changeLanguage(code){
      let that = this;
      this.utils.setLanguage(code,(status)=>{
        if(status){
          that.popupVisible = false;
        }
      })
    },
    /** 
     * @method 去搜索页
    */
    goSearch(){
      sessionStorage.setItem("open",'')
      this.$router.push({
        name: 'list'
      })
    }
  }
}
</script>
<style lang="scss" scoped>
#home-page{
  height: 100%;
  background: #FFF;
  overflow: hidden;
  .top{
    margin-top: 0.50rem;
    height: 0.42rem;
    line-height: 0.42rem;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-end;
    img:first-child{
      height:100%;
      margin-right: 0.15rem;
    }
    img:last-child{
      height: 0.24rem;
      margin: 0 0.37rem 0 0.3rem;
    }
  }
  .logo-box{
    margin: 2rem auto 0.4rem;
    display: block;
    width: 3.7rem;
    height:2.72rem;
    .logo{
      width: 100%;
      height: auto;
      max-height: 1.6rem;
    }
  }
  .logo{
    margin: 2rem auto 0;
    display: block;
    width: 3.7rem;
    height: 1.12rem;
  }
  .home-text{
    margin: 0.5rem 0.31rem 1.2rem;
    color: #999999;
    font-size: 0.34rem;
    line-height: 0.48rem;
  }
  .button{
    width: 5.78rem;
    height: 1rem;
    border-radius: 0.5rem;
    background: #009530;
    margin: 0 auto;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    color: #FFF;
    font-size: 0.34rem;
    img{
      width: 0.48rem;
      height: 0.44rem;
      margin-right: 0.31rem;
    }
  }
  .mint-popup{
    width: 100%;
    .language-list{
      max-height: 4rem;
      overflow-y: auto;
      p{
        box-sizing: border-box;
        height: 1rem;
        line-height: 1rem;
        font-size: 0.38rem;
        text-align: center;
      }
      .active{
        background: #009530;
        color: #FFF;
      }
    }
  }
}
</style>