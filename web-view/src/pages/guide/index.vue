<template>
    <div id="guide-page">
        <mt-swipe 
            class="box"
            ref="swipe"
            :auto="0"
            :continuous="false">
            <mt-swipe-item class="item">
                <img src="./../../assets/img/guide_1.png" alt="">
                <p class="title">replace</p>
                <p class="text">engineer wants to replace an unknownfaulty component</p>
            </mt-swipe-item>
            <mt-swipe-item class="item">
                <img src="./../../assets/img/guide_2.png" alt="">
                <p class="title">search</p>
                <p class="text">goes to e-commerce website to search for component</p>
                <div class="goHome"><span @click="goHome">Click Enter</span></div>
            </mt-swipe-item>
        </mt-swipe>
        <div class="time">
            {{time}}<span>skip</span>
        </div>
    </div>
</template>
<script>
/** 
 * @author ny
 * @page 引导页
*/
export default {
    name: 'Guide',
    data(){
        return{
            time:5,
            timeOut: null,
        }
    },
    mounted(){
        let num = 5;
        this.timeOut = setInterval(() => {
            num -= 0.5;
            if(this.time != Math.floor(num)){
                this.time = Math.floor(num)
            }
            if((num == 2.5) && !this.$refs.swipe.index){
                this.$refs.swipe.next();
            }
            if(num == 0){
                clearInterval(this.timeOut);
                this.num = 5;
                this.time = 5; 
                this.timeOut = null;
                this.goHome()
            }
        }, 500);
    },
    beforeDestroy() { 
        this.timeOut = null;
        clearInterval(this.timeOut);       
    },
    methods: {
        goHome(){
            this.$router.replace({
                name: 'home'
            })
        }
    },

}
</script>
<style lang="scss" scoped>
#guide-page{
    height: 100%;
    position: relative;
    .box{
        .item{
            overflow: hidden;
            display: flex;
            flex-direction: column;
            align-items: center;
            img{
                display: block;
                width: 6.02rem;
                height: 5.12rem;
                margin: 2rem auto 0.17rem;
            }
            .title{
                line-height: 0.98rem;
                font-size: 0.7rem;
                color: $color;
                text-align: center;
            }
            .text{
                width: 5rem;
                margin: 0.2rem auto 0;
                line-height: 0.5rem;
                font-size: 0.36rem;
                color: #666;
                text-align: center;
            }
            .goHome{
                margin: 0 auto;
                margin-top: 0.2rem;
                height: 0.36rem;
                    text-align: center;
                span{
                    display:inline-block;
                    color: $color;
                    font-size: 0.36rem;
                }
            }
        }
        /deep/
        .mint-swipe-indicators .is-active{
            background: $color;
        }
    }
    .time{
        display: inline-block;
        position: absolute;
        top: 0.4rem;
        right:0.25rem;
        background: #CCC;
        line-height: 0.32rem;
        padding: 0.03rem 0.15rem;
        border-radius: 0.19rem;
        color: #FFF;
        font-size: 0.07rem;
        span{
            padding-left: 0.1rem;
        }
    }
}
</style>
