<template>
    <div class="tabbar-comp">
        <div :class="['tabbar-item', `${$route.name == 'home' ? 'active' : ''}`]">
            <img 
                :src="$route.name == 'home' ? require('./../assets/img/tabbar_home_active.png') : require('./../assets/img/tabbar_home_active.png')" alt="">
            <p>home page</p>
        </div>
        <div class="tabbar-item camera">
            <div class="camera-tutton">
                <img src="./../assets/img/camera.png" alt="">
            </div>
        </div>
        <div :class="['tabbar-item', `${$route.name == 'user' ? 'active' : ''}`]">
            <img 
                :src="$route.name == 'user' ? require('./../assets/img/tabbar_user.png') : require('./../assets/img/tabbar_user.png')" alt="">
            <p>my</p>
        </div>
    </div>
</template>
<script>
/** 
 * @author ny
 * @component 底部导航栏
*/
export default {
    data(){
        return{}
    },
    mounted(){
        console.log(this.$route)
    }
}
</script>
<style lang="scss" scoped>
.tabbar-comp{
    width: 100%;
    height: 1rem;
    position: fixed;
    bottom: 0;
    left: 0;
    background: #FFF;
    box-shadow:0 0.02rem 0.05rem 0 rgba(0,0,0,0.5);
    display: flex;
    .tabbar-item{
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        position: relative;
        img{
            height: 0.38rem;
        }
        p{
            line-height: 0.33rem;
            font-size: 0.24rem;
            margin-top: 0.08rem;
            color: #999;
        }
        .camera-tutton{
            height: 1.4rem;
            width: 1.4rem;
            border-radius: 0.7rem;
            background: $color;
            position: absolute;
            bottom: 0.45rem;
            left: 0;
            right: 0;
            margin: 0 auto;
            display: flex;
            justify-content: center;
            align-items: center;
            img{
                width: 0.48rem;
                height: 0.44rem;
            }
        }
    }
    .active{
        p{
            color: #333;
        }
    }
}
</style>