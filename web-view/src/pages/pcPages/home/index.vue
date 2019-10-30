<template>
    <div id="pc-home-page">
        <Header></Header>
        <div style="background: #fff;min-width: 1200px;">
            <div class="contain">
                <div class="search-div">
                    <input 
                        type='text' 
                        class="search-input" 
                        placeholder="Enter what you want to search for…" 
                        @focus="onFocus"/>
                    <span style="display: inline-block;margin-left: 60px;vertical-align: middle">
                        <img src="../../../assets/img/shoppingcart.png" alt="" class="shop-cart">
                        <span>$85.69</span>
                    </span>
                </div>
                <div class="banner">
                    <img src="../../../assets/img/banner_pc.png" alt="" width="1200px" height='300px'>
                </div>
                <div class="desc-con">
                    <ul>
                        <li class="active">Products</li>
                        <li>My Account</li>
                        <li>Services</li>
                        <li>Brands</li>
                    </ul>
                    <div class="desc">
                        <h5 class="title">Popular Categories</h5>
                        <div style="display: flex">
                            <div class="item">
                                <p class="tip">Use Pixie to Find Your Products <span style="margin-left: 16px;cursor: pointer" @click="showDlg = true"><img src="../../../assets/img/camera_blue.png" alt="" width="20px" height='16px'></span></p>
                                <p>Use image to search for relays and machine drives</p>
                            </div>
                            <div class="item">
                                <p class="tip">Test & Measurement</p>
                                <p>Find a vast array of test and measurement products</p>
                            </div>
                            <div class="item">
                                <p class="tip">Semiconductors</p>
                                <p>Choose from audio processor circuits to amplifiers and comparators</p>
                            </div>
                        </div>
                        <div style="display: flex">
                            <div class="item">
                                <p class="tip">Relays</p>
                                <p>Choose from general purpose.interface, solid state or automotive relays</p>
                            </div>
                            <div class="item">
                                <p class="tip">Sensors & Transducers</p>
                                <p>Sensors and transducers for factory to process automation</p>
                            </div>
                            <div class="item">
                                <p class="tip">BOM Management</p>
                                <p>Use this tool to manage your BOM list and EOL cycle</p>
                            </div>
                        </div>
                        <div style="display: flex">
                            <div class="item">
                                <p class="tip">Test & measurement</p>
                                <p>Choose from general purpose</p>
                            </div>
                            <div class="item">
                                <p class="tip">Use Pixie to Find Your Products</p>
                                <p>Sensors and transducers</p>
                            </div>
                            <div class="item">
                                <p class="tip">Relays</p>
                                <p>Use this tool to manage your BOM</p>
                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </div>
        <Footer></Footer>
        <Dialog :maskHide=false :showDlg="showDlg" @onConfirm="dlgConfirm" @onCancel="showDlg=false"></Dialog>
    </div>
</template>
<script>
import Header from '../header'
import Footer from '../footer'
import Dialog from '@/components/dialog.vue';
export default {
    name: 'PCHOME',
    data(){
        return{
        popupVisible: false,
        showDlg: false
        }
    },
    components: {Header,Footer,Dialog},
    methods: {
        open(){
        this.popupVisible = true;
        },
        /** 
         * @method 切花语言
         * @param {String} 语言编码
        */
        changeLanguage(code){
        let that = this;
        this.utils.setLanguage(code,(status)=>{
            if(status){
            that.popupVisible = false;
            }
        })
        },
        //弹窗确定
        dlgConfirm(){
            this.showDlg = false
        },
        /** 
         * @method 监听输入框聚集
        */
        onFocus(){
            this.$router.push({name:'list'})
        }
    }
}
</script>
<style lang="scss" scoped>
#pc-home-page{
    .contain{
        width: 1200px;
        margin: 0 auto;
    }
    .search-div{
        padding: 50px 0;
        text-align: center;
        .search-input{
            width: 758px;
            height: 58px;
            padding-left: 40px;
            background: #F5F5F5;
            border-radius: 40px;
            outline: none;
            border: none
        }
        .shop-cart{
            width: 28px;
            height: 39px;
            display: inline-block;
            vertical-align: middle;
            margin-right: 8px;
        } 
    }
    .desc-con{
        ul{
            padding: 30px 280px;
            font-size:20px;
            font-family:PingFangSC-Regular,PingFang SC;
            font-weight:400;
            color:rgba(0,0,0,1);
            line-height:28px;
            display: flex;
            li{
                margin-right: 100px;
                padding-bottom: 16px;
                cursor: pointer;
                &.active{
                    color: #006488;
                    border-bottom: 4px solid #006488;
                }
            }
            li:last-child{
                margin-right: 0;
            }
        }
        .desc{
            padding-bottom: 41px;
            .title{
                font-size:18px;
                font-family:PingFangSC-Semibold,PingFang SC;
                font-weight:600;
                color:rgba(0,0,0,1);
                line-height:25px;
                margin-bottom: 30px;
            }
            .item{
                flex: 1;
                margin-right: 168px;
                margin-bottom: 25px;
                &:nth-child(3n){
                    margin-right: 0;
                }
                p{
                    font-size: 14px;
                    font-weight:400;
                    color:rgba(102,102,102,1);
                    line-height:20px;
                }
                p.tip{
                    font-size:16px;
                    color:rgba(0,0,0,1);
                    line-height:22px;
                    margin-bottom: 15px;
                }
            }
            
        }
    }
}

</style>