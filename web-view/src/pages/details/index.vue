<template>
    <div id="details-page">
        <!-- 施耐德组件 -->
        <div id="widgetFrame"></div>
        <!-- <div id="widgetFrame" style="width:1280px; height:800px"></div> -->
    </div>
</template>
<script>
/** 
 * @author ny
 * @page 详情页
*/
import CountryCode from './../../assets/js/international';
export default {
    name: 'Details',
    data(){
        return{}
    },
    mounted(){
        // 获取组件sku并初始化
        let id = this.$route.query.productId;
        if(id){
            id = id.toUpperCase();
            var reference = id;
            // console.log(this.$i18n.locale,CountryCode[this.$i18n.locale])
            var language = this.$i18n.locale;
            var country = CountryCode[this.$i18n.locale];
            var project = 'ANY';
            var token = 'Bearer 377feb05b91a96a12a6d06232db552c8';
            var widgetOptions = {};
            var wrapperInstance = seWidgetWrapper.startFromReference(reference, language, country, project, token, widgetOptions);
        }
    }
}
</script>
<style lang="scss" scoped>
#details-page{
    height: 100%;
    #widgetFrame{
        width: 100%;
        height: 100%;
    }
}
</style>
