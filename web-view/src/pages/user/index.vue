<template>
    <div id="user-page">
        <my-dom></my-dom>
    </div>
</template>
<script>
/** 
 * @author ny
 * @page 个人页
*/
export default {
    name: 'User',
    data(){
        return{
            value:''
        }
    }
}
</script>
<style lang="scss" scoped>
#user-page{
    p{
        color:$color;
    }
}
</style>
