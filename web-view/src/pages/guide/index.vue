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
            idx:''
        }
    },
    methods: {
        goHome(){
            this.$router.replace({
                name: 'home'
            })
        }
    }
}
</script>
<style lang="scss" scoped>
#guide-page{
    height: 100%;
    .box .item{
        overflow: hidden;
        display: flex;
        flex-direction: column;
        align-items: center;
        img{
            display: block;
            width: 6.02rem;
            height: 5.12rem;
            margin: 2.9rem auto 0.17rem;
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
}
</style>
