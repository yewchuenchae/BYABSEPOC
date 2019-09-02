<template>
    <div id="details-page">
        <div id="widgetFrame" style="width:1280px; height:800px"></div>
    </div>
</template>
<script>
/** 
 * @author ny
 * @page 详情页
*/
export default {
    name: 'Details',
    data(){
        return{
            value:''
        }
    },
    mounted(){
        let id = this.$route.query.productId;
        
        if(id){
            id = id.toUpperCase();
            var reference = id;
            var language = 'EN';
            var country = 'SCHINT';
            var project = 'ANY';
            var token = 'Bearer 377feb05b91a96a12a6d06232db552c8';
            var widgetOptions = {};
            var wrapperInstance = seWidgetWrapper.startFromReference(reference, language, country, project, token, widgetOptions);
        }

        // var dataset = {
        //     type: "ez",
        //     value: 'ZELIO_PLUGIN_RELAY',
        //     // value: 'ZELIO_CTRL',
        //     // value: 'ZELIO_TIME',    // 0
        //     // value: 'ZELIO_SSR',      // 0
        //     // value: 'Altivar 12',      
        //     // value: 'Altivar Easy 310',      
        //     // value: 'Altivar 320',            
        //     // value: 'Altivar 340', 
        // };
        // var language = 'EN';
        // var country = 'SCHINT';
        // var project = 'ANY';
        // var token = 'Bearer 377feb05b91a96a12a6d06232db552c8';
        // var widgetOptions = {};
        // var wrapperInstance = seWidgetWrapper.startWidget(dataset, language, country, project, token, widgetOptions);
    }
}
</script>
<style lang="scss" scoped>
#details-page{
}
</style>
