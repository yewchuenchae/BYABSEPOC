<template>
    <div>
        <div style="padding: 0 0.24rem;display: flex;">
            <span class="company-name">Company xxx</span>
            <span class="company-ico">
                <img src='../../assets/img/basketball_ico.png' @click="goPC"/>
                <img src='../../assets/img/shoppingcart_company.png' />
                <img src='../../assets/img/user_ico.png' class="user" />
            </span>
        </div>
        <div class="search-div">
            <input 
                type='text' 
                class="search-input" 
                placeholder="Enter part number/keywords"
                @focus="onFocus">
            <img src='../../assets/img/search_icon.png' class="search-ico">
        </div>
        <div class="company-banner">
            <img src='../../assets/img/company_logo.png'>
        </div>
        <div 
            v-for="(item,index) in listOne" 
            :key='index'>
            <mt-cell :title="item" class="list-one"></mt-cell>
        </div>
        <div class="company-title">Popular Categories</div>
        <div 
            v-for="(item,index) in listTwo" 
            :key="index+'a'">
            <template v-if='item.title'>
                <mt-cell 
                    :title="item.title"  
                    is-link>
                    <span><img slot="icon" :src="item.icon" class="img-icon"></span>
                </mt-cell>
            </template>
            <mt-cell v-else :title="item.title"  is-link></mt-cell>
        </div>
    </div>
</template>
<script>
import cameIcon  from '../../assets/img/camera_blue.png'
export default {
    name: 'Company',
    data(){
        return{
            listOne: ['All Products','All Brands','My Account','Support'],
            listTwo:[{
                title: 'Use Pixie to Find Your Products',
                icon: cameIcon
            },{
                title: 'Sensors & Transducers'
            },{
                title: 'Semiconductors'
            },{
                title: 'Use Pixie to Find Your Products'
            }]
        }
    },
    mounted(){
        
    },
    methods: {
        /** 
         * @method 进入PC
        */
        goPC(){
            this.$router.push({
                name: 'pcHome'
            })
        },
        /** 
         * @method 监听输入框聚集
        */
        onFocus(){
            this.$router.push({name:'list'})
        }
    },
    components: {}
}
</script>
<style lang="scss" scoped>
.company-name{
    font-size:0.44rem;
    font-family:PingFang-SC-Heavy,PingFang-SC;
    font-weight:800;
    color:rgba(51,51,51,1);
    line-height:62px;
    flex:1;
}
.company-ico{
    display: flex;
    align-items: center;
    justify-items: center;
    img{
        display: inline-block;
        width: 0.44rem;
        height: 0.44rem;
        margin-right: 0.4rem;
        &.user{
            width: 0.82rem;
            height: 0.82rem;
            margin-right: 0;
        }
    }
}
.search-div{
    padding: 0 0.24rem;
    position: relative;
    .search-input{
        width: calc(100% - #{0.8rem});
        height: 0.8rem;
        padding-left: 0.8rem;
        background: #F7F7F7;
        border-radius: 0.4rem;
        outline: none;
        border: none
    }
    .search-ico{
        width: 0.3rem;
        height: 0.3rem;
        position: absolute;
        top: 0.28rem;
        left: 0.6rem;
    }
}
.company-banner{
    padding: 0.2rem 0.24rem;
    img{
        width: 100%;
        height: 3rem;
    }
}
.list-one{
    background-color:#FAFAFA;
    .mint-cell-wrapper{
        background-color:#FAFAFA;
    }
}
.img-icon{
    width: 0.4rem;
    height: 0.4rem;
}
.company-title{
    font-size:0.38rem;
    font-family:PingFang-SC-Heavy,PingFang-SC;
    font-weight:800;
    color:rgba(0,100,136,1);
    line-height: 0.53rem;
    padding: 0.4rem 0 0.2rem 0.24rem;
    background: #fff;
}

</style>