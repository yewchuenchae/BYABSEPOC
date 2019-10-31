<template>
    <footer>
        <div class="center">
            <p>{{lineOne}}</p>
            <p>{{lineTwo}}</p>
        </div>
    </footer>
</template>
<script>
export default {
  name: 'Footer',
  data(){
    return{
        lineOne: 'Copyright 20000 = 1088XXXX Co., Ltd. reserves all rights. Privacy protection / legal declaration ',
        lineTwo: 'ICP Provision 05028939 Public Security Provision 1101080201489030'
    }
  },
  methods: {
  }
}
</script>
<style  lang="scss" scoped>
footer{
    width: 100%;
    min-width: 1200px;
    height: 60px;
    background: #2D2D2D;
    .center{
        width: 1200px;
        margin: 0 auto;
        padding: 12px 0;
        p{
            font-size:12px;
            font-family:MicrosoftYaHei;
            color:rgba(255,255,255,1);
            line-height:18px;
            text-align: center;
        }
    }
}

</style>