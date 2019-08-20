<template>
    <div id="home-page">
        <mt-search
        v-model="value"
        cancel-text="取消"
        placeholder="搜索">
        </mt-search>
        <p>dfsa<span>dfsaafds</span></p>
        <mt-button type="default" @clcik.native="test">default</mt-button>
        <mt-button type="primary" @clcik="test">primary</mt-button>
        <mt-button type="danger" @clcik.native="test">danger</mt-button>
        <div @click="test">点击1</div><span @click="test">点击2</span>
    </div>
</template>
<script>
/** 
 * @author ny
 * @page 首页
*/
export default {
    name: 'Home',
    data(){
        return{
            value:''
        }
    },
    methods: {
        test(){
            console.log(1)
        }
    }
}
</script>
<style lang="scss" scoped>
#home-page{
    p{
        color:$color;
        span{
            color:red;
        }
    }
}
</style>
