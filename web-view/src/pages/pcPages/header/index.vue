<template>
    <div class="head">
        <div class="center">
            <div class="left">{{logo}}</div>
            <div class="right">
                <div style="margin-right: 80px;display: inline-block;padding: 23px 0;">
                    <span>{{contact}}</span>
                    <span> | </span>
                    <span>{{support}}</span>
                    <span> | </span>
                    <span style="margin-right:0">
                        <img src="../../../assets/img/language_white.png" alt="" width="20px" height='20px' style="margin-right:10px;vertical-align: middle">{{language}}({{selectedLan}})</span>
                    <span style="font-size: 12px"> > </span>
                </div>
                <span style="display: inline-block;vertical-align: middle">
                    <img  class="user-head" src="https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=862689421,1137849620&fm=173&app=25&f=JPEG?w=610&h=358&s=D5BA77DBDAE2DB470A0CE7080300D0D7"/>
                </span>
            </div>
        </div>
    </div>
</template>
<script>
export default {
  name: 'Header',
  data(){
    return{
        logo: 'CompanyABC',
        contact:'Contact Us',
        support: 'Support',
        language: 'Language',
        selectedLan: 'English'
    }
  },
  methods: {
  }
}
</script>
<style  lang="scss" scoped>
.head{
    width: 100%;
    min-width: 1200px;
    height: 70px;
    background: #006488;
    .center{
        width: 1200px;
        margin: 0 auto;
        .left{
            color: #fff;
            font-size:28px;
            font-family:PingFang-SC-Heavy,PingFang-SC;
            font-weight:800;
            line-height: 40px;
            display: inline-block;
            padding: 15px 0;
        }
        .right{
            display: inline-block;
            float: right;
            font-size:18px;
            font-family:PingFangSC-Regular,PingFang SC;
            font-weight:400;
            color:rgba(255,255,255,1);
            line-height:25px;
            span{margin-right: 20px;vertical-align: middle}
        }
        .user-head{
            width: 48px;
            height: 48px;
            padding: 10px 0;
            border-radius: 100%;
            display: inline-block;
        }
    }
}

</style>