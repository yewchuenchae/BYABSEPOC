<template>
    <div id="home-page">
        <div class="search">
            <img class="search-icon" src="./../../assets/img/search_icon.png" alt="">
            <p>Enter part number/keywords</p>
            <img class="search-camera" src="./../../assets/img/search_camera.png" alt="">
        </div>
        <div class="tab">
            <span :class="active == 0 ? 'active' : ''" @click="changeActive(0)">All Products</span>
            <span :class="active == 1 ? 'active' : ''" @click="changeActive(1)">Manufacturers</span>
        </div>
        <div 
            class="box"
            v-infinite-scroll="loadMore"
            infinite-scroll-disabled="loading"
            infinite-scroll-distance="10">
            <div 
                class="item"
                v-for="(item, idx) in list"
                :key="idx">
                <img :src="item.img" alt="">
                <p>{{item.title}}</p>
            </div>
        </div>
    </div>
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
            loading: false,
            active:0,
            list:[],
        }
    },
    mounted(){
        this.list = [
            {
                img:'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=264542271,452860046&fm=115&gp=0.jpg',
                title: 'Electromechanical Relays'
            },
            {
                img:'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4021696477,125879773&fm=115&gp=0.jpg',
                title: 'Variable Speed Drives'
            },
            {
                img:'http://img0.imgtn.bdimg.com/it/u=550933263,2383635639&fm=15&gp=0.jpg',
                title: 'Ethernet Swiches'
            },
            {
                img:'http://img0.imgtn.bdimg.com/it/u=1065322161,573763851&fm=15&gp=0.jpg',
                title: 'Electromechanical Relays'
            },
            {
                img:'http://img3.imgtn.bdimg.com/it/u=4206409568,1801685465&fm=26&gp=0.jpg',
                title: 'Variable Speed Drives'
            },
            {
                img:'http://img3.imgtn.bdimg.com/it/u=4071158148,2637611101&fm=15&gp=0.jpg',
                title: 'Ethernet Swiches'
            },
            {
                img:'http://img1.imgtn.bdimg.com/it/u=1977922514,2762107149&fm=15&gp=0.jpg',
                title: 'Variable Speed Drives'
            },
        ];
    },
    methods: {
        /** 
         * @method 切换选中tab
         * @param {Number} active 选中tab下标
        */
        changeActive(active){
            this.active = active;
            Indicator.open();
            setTimeout(() => {
                if(this.active){
                    this.list = [
                        {
                            img: 'http://img1.imgtn.bdimg.com/it/u=3859570704,3506148431&fm=11&gp=0.jpg',
                            title: 'Google',
                        },
                        {
                            img: 'http://img5.imgtn.bdimg.com/it/u=876334890,2639198549&fm=11&gp=0.jpg',
                            title: 'Apple Inc',
                        },
                        {
                            img: 'http://img5.imgtn.bdimg.com/it/u=876334890,2639198549&fm=11&gp=0.jpg',
                            title: 'Microsoft',
                        },
                        {
                            img: 'http://img1.imgtn.bdimg.com/it/u=3859570704,3506148431&fm=11&gp=0.jpg',
                            title: 'Google',
                        },
                        {
                            img: 'http://img5.imgtn.bdimg.com/it/u=876334890,2639198549&fm=11&gp=0.jpg',
                            title: 'Apple Inc',
                        },
                        {
                            img: 'http://img5.imgtn.bdimg.com/it/u=876334890,2639198549&fm=11&gp=0.jpg',
                            title: 'Microsoft',
                        },
                    ]
                }else{

                    this.list = [
                        {
                            img:'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=264542271,452860046&fm=115&gp=0.jpg',
                            title: 'Electromechanical Relays'
                        },
                        {
                            img:'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4021696477,125879773&fm=115&gp=0.jpg',
                            title: 'Variable Speed Drives'
                        },
                        {
                            img:'http://img0.imgtn.bdimg.com/it/u=550933263,2383635639&fm=15&gp=0.jpg',
                            title: 'Ethernet Swiches'
                        },
                        {
                            img:'http://img0.imgtn.bdimg.com/it/u=1065322161,573763851&fm=15&gp=0.jpg',
                            title: 'Electromechanical Relays'
                        },
                        {
                            img:'http://img3.imgtn.bdimg.com/it/u=4206409568,1801685465&fm=26&gp=0.jpg',
                            title: 'Variable Speed Drives'
                        },
                        {
                            img:'http://img3.imgtn.bdimg.com/it/u=4071158148,2637611101&fm=15&gp=0.jpg',
                            title: 'Ethernet Swiches'
                        },
                        {
                            img:'http://img1.imgtn.bdimg.com/it/u=1977922514,2762107149&fm=15&gp=0.jpg',
                            title: 'Variable Speed Drives'
                        },
                    ];
                }
                Indicator.close();
            }, 500);
        },
        /** 
         * @method 监听滚动到底部
        */
        loadMore(){
            if(!this.loading){
                this.loading = true;
                Indicator.open();
                setTimeout(() => {
                    this.list = [...this.list,...this.list.splice(0,10)];
                    this.loading = false;
                    Indicator.close();
                }, 3000);
            }
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
        p{
            flex:1;
            font-size: 0.26rem;
            color:#999;
            line-height: 0.37rem;
        }
        .search-camera{
            height: 0.34rem;
            height: 0.3rem;
        }
    }
    .tab{
        margin: 0.2rem 0.24rem 0;
        height: 0.48rem;
        span{
            display: inline-block;
            line-height: 0.42rem;
            color: #999;
            font-size: 0.3rem;
            margin-right: 0.5rem;
            &:last-child{
                margin:0;
            }
        }
        .active{
            color: $color;
            line-height: 0.48rem;
            font-size: 0.34rem;
        }
    }
    .box{
        box-sizing: border-box;
        padding-top: 0.1rem 0 0;
        flex: 1;
        overflow-y: auto;
        .item{
            box-sizing: border-box;
            margin: 0.3rem 0.24rem 0;
            height: 2rem;
            padding: 0.2rem;
            border-radius: 0.18rem;
            overflow: hidden;
            box-shadow:0.01rem 0 0.17rem 0.03rem rgba(61,205,88,0.1);
            display: flex;
            align-items: center;
            img{
                height: 1.6rem;
                width: 1.6rem;
                margin-right: 0.3rem;
            }
            p{
                line-height: 0.45rem;
                font-size: 0.32rem;
            }
        }
    }
}
</style>
