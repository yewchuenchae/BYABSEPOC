<template>
    <div id="guide-page">
        <mt-swipe 
            class="box"
            ref="swipe"
            :auto="0"
            :continuous="false">
            <mt-swipe-item class="item">
                <img src="./../../assets/img/guide_1.png" alt="">
                <p>search</p>
                <p>goes to e-commerce website to search for component</p>
            </mt-swipe-item>
            <mt-swipe-item class="item">
                <img src="./../../assets/img/guide_2.png" alt="">
                <p>search</p>
                <p>goes to e-commerce website to search for component</p>
                <span @click="goHome">立即体验</span>
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
    mounted(){
        console.log(this.$i18n)
    },
    methods: {
        goHome(){
            console.log(1111)
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
        position: relative;
        img{
            width: 6.02rem;
            height: 5.12rem;
            margin: 2.9rem auto 0.17rem;
        }
    }
}
</style>
