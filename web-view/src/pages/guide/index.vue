<template>
    <div id="guide-page">
        <mt-swipe 
            class="box"
            ref="swipe"
            :auto="0"
            :continuous="false"
            @before="onBefore"
            @end="onEnd"
            @change="onChange">
            <mt-swipe-item class="item1">
                1
            </mt-swipe-item>
            <mt-swipe-item class="item2">
                2
                <span @click="goHome">立即体验</span>
                <mt-button type="primary" @click="goHome">立即体验</mt-button>
                <!-- <mt-button @click.native="goHome">按钮</mt-button> -->
                <!-- <button @click.native="goHome">立即体验</button> -->
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
        onBefore(){
            console.log('onBefore')
        },
        onEnd(){
            console.log('onEnd')
        },
        onChange(idx){
            console.log('onChange',idx)
            if(idx == 2){
                this.$router.replace({
                    name: 'home'
                })
            }
        },
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
    .box{
        .item1{
            background: red;
        }
        .item2{
            background: blue;
            position: relative;
        }
    }
}
</style>
