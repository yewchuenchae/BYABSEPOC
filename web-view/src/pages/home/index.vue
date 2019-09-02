<template>
<keep-alive>
    <div id="home-page">
        <div class="search">
            <img class="search-icon" src="./../../assets/img/search_icon.png" alt="">
            <!-- <input v-model='sku' placeholder="Enter part number/keywords" type="text"/> -->
            <form @submit.prevent="formSubmit" action="javascript:return true">
                <input type="search" v-model="sku" @keydown="search2($event)"  placeholder="Enter part number">
            </form>
            <img class="search-camera" @click="camera" src="./../../assets/img/search_camera.png" alt="">
        </div>
        <div 
            class="box">
            <div 
                class="item"
                v-for="(item, idx) in list"
                :key="idx"
                @click="goDetails(item.productId)">
                <p class="brand">{{item.brand}}</p>
                <p class="productId">{{item.productId}}</p>
                <p class="category">{{item.category}}</p>
                <p class="family">{{item.family}}</p>
                <p class="description">{{item.description}}</p>
            </div>
            <div v-show='list.length==0' class="no-data">
                NO Data
            </div>
        </div>
        <input 
            id="camera" 
            type="file" 
            accept="image/gif,image/jpeg,image/jpg,image/png,image/svg" 
            capture="camera"
            @change="changeFile();">
    </div>
</keep-alive>
</template>
<script>
/** 
 * @author ny
 * @page 首页
*/
import { Indicator } from 'mint-ui'
export default {
    name: 'Home',
    data(){
        return{
            sku:'',
            list:[],
        }
    },
    mounted(){
        this.list = [];
    },
    methods: {
        formSubmit () {
            return false
        },
        search(){
            console.log(this)
            this.goDetails(this.sku)
        },
        search2(ev){
            if(ev.keyCode == 13) {  //键盘回车的编码是13
                this.search();
            }
        },
        /** 
         * @method 进入详情页
         * @param productId 商品sku
        */
        goDetails(productId){
            this.$router.push({
                name: 'details',
                query: {
                    productId
                }
            })
        },
        /** 
         * @method 点击相机回调
        */
        camera(){
            let camera = document.getElementById('camera');
            camera.click();
        },
        /** 
         * @method 监听图片选择
        */
        changeFile(){
            let camera = document.getElementById('camera');
            let req = {
                file: camera.files[0]
            }
            Indicator.open();
            this.apis.upload(req).then((res)=>{
                Indicator.close();
                if(res.code == 200){
                    this.list = res.data;
                }else{
                    this.list = [];
                }
            }).catch((err)=>{
                Indicator.close();
                console.log(err)
            })
        }
    }
}
</script>
<style lang="scss" scoped>
#home-page{
    height:100%;
    display: flex;
    flex-direction: column;
    .search{
        box-sizing:border-box;
        display: flex;
        flex-direction: row;
        align-items: center;
        margin: 0.2rem 0.24rem 0.5rem;
        height: 0.8rem;
        padding: 0 0.3rem;
        overflow: hidden;
        border-radius: 0.4rem;
        background:rgba(247,247,247,1);
        .search-icon{
            width: 0.3rem;
            height: 0.3rem;
            margin-right: 0.2rem;
        }
        form{
            flex:1;
            height: 0.37rem;
            margin-right: 0.2rem;
            input{
                width: 100%;
                font-size: 0.26rem;
                height: 0.37rem;
                line-height: 0.37rem;
                background:rgba(247,247,247,1);
                border:0;
            }
        }
        .search-camera{
            height: 0.34rem;
            height: 0.3rem;
        }
    }
    .box{
        box-sizing: border-box;
        padding-top: 0.1rem 0 0;
        position: relative;
        flex: 1;
        overflow-y: auto;
        .item{
            box-sizing: border-box;
            margin: 0.3rem 0.24rem 0;
            height: 3rem;
            padding: 0.2rem;
            border-radius: 0.18rem;
            overflow: hidden;
            box-shadow:0.01rem 0 0.17rem 0.03rem rgba(61,205,88,0.1);
            p{
                width:100%;
                font-size: 0.28rem;
            }
            .brand{
                // font-size: 0.34rem;
                font-weight: 600;
            }
            .productId{
                line-height: 0.30rem;
                // font-size: 0.22rem;
                margin-top:0.12rem;
            }
            .category,.family{
                line-height: 0.36rem;
                // font-size: 0.28rem;
                margin-top:0.04rem;
            }
            .description{
                margin-top:0.12rem;
                // font-size: 0.26rem;
                line-height: 0.36rem;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 2;/*超出3行部分显示省略号，去掉该属性 显示全部*/
                -webkit-box-orient: vertical;
            }
        }
        .no-data{
            text-align: center;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%,-50%,)
        }
    }
    #camera{
        display: none;
    }
}
</style>
