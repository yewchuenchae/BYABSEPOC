<template>
<keep-alive>
    <div id="list-page">
        <!-- <div class="search">
            <img class="search-icon" src="./../../assets/img/search_icon.png" alt="">
            <form @submit.prevent="formSubmit" action="javascript:return true">
                <input 
                  type="search" 
                  v-model="sku1" 
                  @keydown="search1($event)" 
                  :placeholder="$t('message.input')">
            </form>
            <img class="search-camera" ref="camera" @click="camera" src="./../../assets/img/search_camera.png" alt="">
        </div> -->
        <div 
            class="box">
            <div 
                class="item"
                v-for="(item, idx) in list"
                :key="idx">
                <div class="top">
                  <div class="goods-img">
                    <img class="logo" src="./../../assets/img/item_icon.png" alt="">
                    <img class="icon" v-lazy="item.url" alt="">
                  </div>
                  <div class="info">
                    <p class="productId">{{item.productId}}</p>
                    <p class="category">{{item.category}}</p>
                    <p class="family">{{item.family}}</p>
                    <p class="description">
                      {{item.description.length > 65 ? item.description.slice(0,65) : item.description}}
                    </p>
                  </div>
                  <div class="shopping-basket">
                    <img class="shopping_basket" src="./../../assets/img/shopping_basket.png" alt="">
                    <!-- <img class="mouse" src="./../../assets/img/mouse.png" alt=""> -->
                  </div>
                </div>
                <div
                  class="bottom"
                  @click="goDetails(item.productId)">
                  <!-- View More Attributes Here! -->
                  {{$t('button.lookInfo')}}
                </div>
            </div>
            <div v-show='list.length==0' class="no-data">
                <!-- NO Data -->
            </div>
        </div>
        <!-- 底部弹窗 -->
        <mt-popup
          v-model="popupVisible"
          popup-transition="popup-fade">
          <p class="unable-find">
            {{$t('button.unableFind')}}
          </p>
          <div class="unable-find-mian">
            <form class='unable-find-form' @submit.prevent="formSubmit" action="javascript:return true">
              <input 
                type="search" 
                v-model="sku2" 
                @keydown="search2($event)" 
                :placeholder="$t('message.input')">
            </form>
            <img src="./../../assets/img/camera.png" alt="" @click="camera">
          </div>
          <p class="product">
            <!-- Open Product Category -->
            {{$t('button.openProduct')}}
          </p>
        </mt-popup>
        <p class="popup-btn" @click="popupVisible=true;">
          {{$t('button.unableFind')}}
        </p>
        <!-- 裁剪 -->
        <div id="demo">
            <!-- 遮罩层 -->
            <div class="container" v-show="panel">
                <div>
                <img id="image" :src="url" alt="Picture">
                </div>
                <button type="button" id="button" @click="crop">{{$t('button.confirm')}}</button>
            </div>
            <div class="label">
                <input type="file" id="change" @change="change">
                <label for="change"></label>
            </div>
        </div>
    </div>
</keep-alive>
</template>
<script>
/** 
 * @author ny
 * @page 首页
*/
import { Indicator,Toast } from 'mint-ui'
import Cropper from 'cropperjs'
export default {
    name: 'List',
    data(){
        return{
            // 输入sku
            sku1:'',
            // 商品列表
            list:[],
            // 选中文件
            picValue:'',
            // 裁剪器
            cropper:'',
            croppable:false,
            // 打开遮罩
            panel:false,
            // 选中文件的url
            url: '',
            // 底部弹窗开关
            popupVisible: false,
            // 底部输入sku
            sku2:'',
        }
    },
    mounted(){
        this.list = [];
        //初始化这个裁剪框
        let self = this;
        let image = document.getElementById('image');
        this.cropper = new Cropper(image, {
            aspectRatio: NaN,
            viewMode: 1,
            background:false,
            zoomable:false,
            guides:false,
            resizable:false,
            ready: function () {
                self.croppable = true;
            }
        });
        if(!sessionStorage.getItem("open")){
          this.$nextTick(()=>{
            sessionStorage.setItem("open",'1')
            // Toast({
            //   message: this.$t('message.selectMessage'),
            //   duration: 1500
            // });
          })
        }
    },
    methods: {
        /** 
         * @method 阻止表单提交
        */
        formSubmit () {
            return false
        },
        /** 
         * @method 顶部输入sku搜索
        */
        search1(ev){
            if(ev.keyCode == 13) {  //键盘回车的编码是13
                this.goDetails(this.sku1)
            }
        },
        /** 
         * @method 底部输入sku搜索
        */
        search2(ev){
            if(ev.keyCode == 13) {  //键盘回车的编码是13
                this.popupVisible = false;
                this.goDetails(this.sku2)
            }
        },
        /** 
         * @method 进入详情页
         * @param productId 商品sku
        */
        goDetails(productId){
            let req = {
                searchCriteria: productId,
                language: this.$i18n.locale,
            }
            Indicator.open();
            this.apis.search(req).then((res)=>{
                Indicator.close();
                if(res.code == 200){
                    this.list = res.data;
                }else{
                    Toast({
                        message: res.message,
                        duration: 1500
                    });
                }
            }).catch((err)=>{
                Indicator.close();
                console.log(err)
            })
        },
        /** 
         * @method 点击相机回调
        */
        camera(){
            this.popupVisible = false;
            console.log(111)
            let camera = document.getElementById('change');
            camera.click();
        },
        /** 
         * @method 文件流转url
        */
        getObjectURL (file) {
            let url = null;
            if (window.createObjectURL!=undefined) { // basic
                url = window.createObjectURL(file)
            } else if (window.URL!=undefined) { // mozilla(firefox)
                url = window.URL.createObjectURL(file)
            } else if (window.webkitURL!=undefined) { // webkit or chrome
                url = window.webkitURL.createObjectURL(file)
            }
            return url
        },
        /** 
         * @method 监听选择文件
        */
        change (e) {
            let files = e.target.files || e.dataTransfer.files;
            // 判断是否选中图片
            if (!files.length) return;
            // console.log(files[0])
            // 判断图片类型
            let flag = true;
            if(
              (files[0].type != 'image/jpg') &&
              (files[0].type != 'image/jpeg') &&
              (files[0].type != 'image/png')
            ){
              Toast({
                message: this.$t('message.uploadFile'),
                duration: 1500
              });
              return
            }
            // 打开遮罩
            this.panel = true;
            // 获取选中文件
            this.picValue = files[0];
            // 将选中文件url
            this.url = this.getObjectURL(this.picValue);
            //每次替换图片要重新得到新的url
            if(this.cropper){
                this.cropper.replace(this.url);
            }
            // 打开遮罩
            this.panel = true;
        },
        /** 
         * @method 裁剪上传
        */
        crop () {
            // 关闭遮罩
            this.panel = false;
            let croppedCanvas;
            let roundedCanvas;
            // 判断面板是否打开
            if (!this.croppable) {
                return;
            }
            // 裁剪图片
            croppedCanvas = this.cropper.getCroppedCanvas();
            roundedCanvas = this.getRoundedCanvas(croppedCanvas);
            // console.log(this.dataURLtoFile(roundedCanvas.toDataURL(),this.picValue.name))
            // console.log(this.dataURLtoFile(roundedCanvas.toDataURL('image/jpg',0.6),this.picValue.name))
            let req = {
                language: this.$i18n.locale,
                file: this.dataURLtoFile(
                    roundedCanvas.toDataURL(this.picValue.type,0.5),
                    this.picValue.name
                )
            }
            // console.log(req)
            Indicator.open();
            // 图片上传
            this.apis.upload(req).then((res)=>{
                Indicator.close();
                if(res.code == 200){
                    this.sku2="";
                    this.list = res.data;
                }else if(res.code == 801){
                    Toast({
                        message: res.message,
                        duration: 1500
                    });
                }else{
                    this.list = [];
                }
            }).catch((err)=>{
                Indicator.close();
                console.log(err)
            })
        },
        /** 
         * @method 获取裁剪内容
        */
        getRoundedCanvas (sourceCanvas) {
            var canvas = document.createElement('canvas');
            var context = canvas.getContext('2d');
            var width = sourceCanvas.width;
            var height = sourceCanvas.height;
            canvas.width = width/2;
            canvas.height = height/2;
            // 设置图片是否平滑的属性 
            context.imageSmoothingEnabled = true;
            // 在画布上绘制图像
            context.drawImage(sourceCanvas, 0, 0, width, height, 0, 0, width/2, height/2);
            return canvas;
        },
        /** 
         * @method 将base64转换为文件
        */
        dataURLtoFile(dataurl, filename) {
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
            while(n--){
                u8arr[n] = bstr.charCodeAt(n);
            }
            return new File([u8arr], filename, {type:mime == 'image/png' ? mime : 'image/jpg'});
        },
    },
}
</script>
<style lang="scss" scoped>
#list-page{
    height:100%;
    display: flex;
    flex-direction: column;
    position: relative;
    font-family: 'Nunito', sans-serif;
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
            input::-webkit-input-placeholder {
              /* placeholder颜色  */
              color: #aab2bd;
              /* placeholder字体大小  */
              font-size: 0.26rem;
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
            margin: 0.2rem 0.24rem 0;
            // height: 2.94rem;
            padding: 0.2rem;
            border-radius: 0.18rem;
            overflow: hidden;
            box-shadow:0.01rem 0 0.17rem 0.03rem rgba(61,205,88,0.1);
            .top{
              display: flex;
              flex-direction: row;
              // align-items: center;
              .goods-img{
                  width: 1.7rem;
                  img{
                    width: 100%;
                  }
                  .logo{
                    margin-bottom: 0.1rem;
                  }
                  .icon[lazy=loading] {
                    height: 100px;
                    margin: auto;
                    background: #666 no-repeat fixed center;
                  }
              }
              .info{
                  margin-left: 0.3rem;
                  flex: 1;
                  font-size: 0.22rem;
                  overflow: hidden;
                  box-sizing: border-box;
                  padding-right:0.2rem;
                  p{
                      width:100%;
                      color: #999;
                  }
                  .brand{
                      font-weight: 600;
                      display: flex;
                      flex-direction: row;
                      line-height: 0.33rem;
                      img{
                        width: 1.1rem;
                        height: 0.33rem;
                        display: inline-block;
                        margin-right: 0.1rem;
                      }
                      span{
                        flex: 1;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                        color: #333;
                      }
                  }
                  .productId{
                      line-height: 0.30rem;
                      margin-top:0.2rem;
                      font-size: 0.26rem;
                      font-family: 'ArialroundMT', Arial, Helvetica, sans-serif;
                  }
                  .category,.family{
                      line-height: 0.30rem;
                      margin-top:0.08rem;
                  }
                  .description{
                      margin-top:0.08rem;
                      line-height: 0.30rem;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      display: -webkit-box;
                      -webkit-line-clamp: 2;/*超出3行部分显示省略号，去掉该属性 显示全部*/
                      -webkit-box-orient: vertical;
                  }
              }
              .shopping-basket{
                width: 0.8rem;
                height:0.8rem;
                background: #d1f9de;
                border-radius: 0.1rem;
                margin-top: 0.2rem;
                // display: flex;
                // justify-content: center;
                // align-items: center;
                img{
                  width: 0.6rem;
                  height: 0.6rem;
                }
                .shopping_basket{
                  margin: 0.1rem 0 0 0.1rem;
                }
                .mouse{
                  margin: 0.1rem 0 0 0.15rem;
                }
              }
            }
            .bottom{
              margin-top: 0.08rem;
              text-align: center;
              line-height: 0.6rem;
              background: #d1f9de;
              color: #333;
              border-radius: 0.1rem;
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
    .popup-btn{
      position: fixed;
      bottom: 0;
      left: 50%;
      height: 0.5rem;
      width: 100%;
      max-width: 480px;
      line-height: 0.5rem;
      text-align: center;
      background: #808080;
      color: #FFF;
      cursor: pointer;
      z-index:5;
      transform: translate3d(-50%, 0, 0);
    }
    .mint-popup{
      top: 100%;
      left: 50%;
      width: 100%;
      max-width: 480px;
      transform: translate3d(-50%, -100%, 0);
      height: 2rem;
      background: #808080;
      box-sizing: border-box;
      padding: 0 0.5rem;
      .unable-find{
        color: #FFF;
        line-height: 0.5rem;
        height:0.4rem;
        width:100%;
        text-align: center;
        margin-bottom: 0.12rem;
        font-family: 'Nunito', sans-serif;  
      }
      .unable-find-mian{
        height: 0.8rem;
        overflow: hidden;
        .unable-find-form{
          margin-top: 0.1rem;
          float: left;
          overflow: hidden;
          border-radius: 0.04rem;
          input{
            background: rgb(209,248,221);
            width: 5rem;
            padding: 0 0.2rem;
            line-height: 0.5rem;
            height: 0.5rem;
            border: 0;
            font-size: 0.26rem;
            font-family: 'Nunito', sans-serif;
          }
          input::-webkit-input-placeholder {
            /* placeholder颜色  */
            color: #aab2bd;
            /* placeholder字体大小  */
            font-size: 0.26rem;
            text-align: center;
            font-family: 'Nunito', sans-serif;
          }
        }
        img{
          height: 0.6rem;
          float: left;
          margin-left: 0.5rem;
        }
      }
      .product{
        width: 100%;
        height: 0.5rem;
        line-height: 0.5rem;
        background: rgb(209,248,221);
        box-sizing: border-box;
        padding: 0 0.2rem;
        color: #aab2bd;
        text-align: center;
        border-radius: 0.04rem;
        font-size: 0.12rem;
        font-family: 'Nunito', 'sans-serif';
      }
    }
    /deep/
    .v-modal{
      left: 50%;
      width: 100%;
      max-width: 480px;
      transform: translate3d(-50%, 0, 0);
      background: rgba(0,0,0,0);
    }
}
</style>

<style scoped="">
  #demo #button {
    position: absolute;
    right: 0.4rem;
    bottom: 0.6rem;
    min-width: 1.6rem;
    height: 0.8rem;
    border:none;
    border-radius: 0.05rem;
    background:white;
    font-size: 0.32rem;
  }
  #demo .container {
    z-index: 99;
    width: 100%;
    position: absolute;
    padding-top: 60px;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    background:rgba(0,0,0,1);
  }
  #demo #image {
    max-width: 100%;
  }
  #demo .label{
      display: none;
  }
  .cropper-view-box,.cropper-face {
    border-radius: 50%;
  }
  .cropper-container {
    font-size: 0;
    line-height: 0;
    position: relative;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    direction: ltr;
    -ms-touch-action: none;
    touch-action: none
  }
  .cropper-container img {
    display: block;
    min-width: 0 !important;
    max-width: none !important;
    min-height: 0 !important;
    max-height: none !important;
    width: 100%;
    height: 100%;
    image-orientation: 0deg
  }
  .cropper-wrap-box,
  .cropper-canvas,
  .cropper-drag-box,
  .cropper-crop-box,
  .cropper-modal {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  }
  .cropper-wrap-box {
    overflow: hidden;
  }
  .cropper-drag-box {
    opacity: 0;
    background-color: #fff;
  }
  .cropper-modal {
    opacity: .5;
    background-color: #000;
  }
  .cropper-view-box {
    display: block;
    overflow: hidden;

    width: 100%;
    height: 100%;

    outline: 1px solid #39f;
    outline-color: rgba(51, 153, 255, 0.75);
  }
  .cropper-dashed {
    position: absolute;

    display: block;

    opacity: .5;
    border: 0 dashed #eee
  }
  .cropper-dashed.dashed-h {
    top: 33.33333%;
    left: 0;
    width: 100%;
    height: 33.33333%;
    border-top-width: 1px;
    border-bottom-width: 1px
  }
  .cropper-dashed.dashed-v {
    top: 0;
    left: 33.33333%;
    width: 33.33333%;
    height: 100%;
    border-right-width: 1px;
    border-left-width: 1px
  }
  .cropper-center {
    position: absolute;
    top: 50%;
    left: 50%;

    display: block;

    width: 0;
    height: 0;

    opacity: .75
  }
  .cropper-center:before,
  .cropper-center:after {
    position: absolute;
    display: block;
    content: ' ';
    background-color: #eee
  }
  .cropper-center:before {
    top: 0;
    left: -3px;
    width: 7px;
    height: 1px
  }
  .cropper-center:after {
    top: -3px;
    left: 0;
    width: 1px;
    height: 7px
  }
  .cropper-face,
  .cropper-line,
  .cropper-point {
    position: absolute;

    display: block;

    width: 100%;
    height: 100%;

    opacity: .1;
  }
  .cropper-face {
    top: 0;
    left: 0;

    background-color: #fff;
  }
  .cropper-line {
    background-color: #39f
  }
  .cropper-line.line-e {
    top: 0;
    right: -3px;
    width: 5px;
    cursor: e-resize
  }
  .cropper-line.line-n {
    top: -3px;
    left: 0;
    height: 5px;
    cursor: n-resize
  }
  .cropper-line.line-w {
    top: 0;
    left: -3px;
    width: 5px;
    cursor: w-resize
  }
  .cropper-line.line-s {
    bottom: -3px;
    left: 0;
    height: 5px;
    cursor: s-resize
  }
  .cropper-point {
    width: 5px;
    height: 5px;

    opacity: .75;
    background-color: #39f
  }
  .cropper-point.point-e {
    top: 50%;
    right: -3px;
    margin-top: -3px;
    cursor: e-resize
  }
  .cropper-point.point-n {
    top: -3px;
    left: 50%;
    margin-left: -3px;
    cursor: n-resize
  }
  .cropper-point.point-w {
    top: 50%;
    left: -3px;
    margin-top: -3px;
    cursor: w-resize
  }
  .cropper-point.point-s {
    bottom: -3px;
    left: 50%;
    margin-left: -3px;
    cursor: s-resize
  }
  .cropper-point.point-ne {
    top: -3px;
    right: -3px;
    cursor: ne-resize
  }
  .cropper-point.point-nw {
    top: -3px;
    left: -3px;
    cursor: nw-resize
  }
  .cropper-point.point-sw {
    bottom: -3px;
    left: -3px;
    cursor: sw-resize
  }
  .cropper-point.point-se {
    right: -3px;
    bottom: -3px;
    width: 20px;
    height: 20px;
    cursor: se-resize;
    opacity: 1
  }
  @media (min-width: 768px) {

    .cropper-point.point-se {
      width: 15px;
      height: 15px
    }
  }
  @media (min-width: 992px) {

    .cropper-point.point-se {
      width: 10px;
      height: 10px
    }
  }
  @media (min-width: 1200px) {

    .cropper-point.point-se {
      width: 5px;
      height: 5px;
      opacity: .75
    }
  }
  .cropper-point.point-se:before {
    position: absolute;
    right: -50%;
    bottom: -50%;
    display: block;
    width: 200%;
    height: 200%;
    content: ' ';
    opacity: 0;
    background-color: #39f
  }
  .cropper-invisible {
    opacity: 0;
  }
  .cropper-bg {
    background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAAA3NCSVQICAjb4U/gAAAABlBMVEXMzMz////TjRV2AAAACXBIWXMAAArrAAAK6wGCiw1aAAAAHHRFWHRTb2Z0d2FyZQBBZG9iZSBGaXJld29ya3MgQ1M26LyyjAAAABFJREFUCJlj+M/AgBVhF/0PAH6/D/HkDxOGAAAAAElFTkSuQmCC');
  }
  .cropper-hide {
    position: absolute;

    display: block;

    width: 0;
    height: 0;
  }
  .cropper-move {
    cursor: move;
  }
  .cropper-crop {
    cursor: crosshair;
  }
  .cropper-disabled .cropper-drag-box,
  .cropper-disabled .cropper-face,
  .cropper-disabled .cropper-line,
  .cropper-disabled .cropper-point {
    cursor: not-allowed;
  }
</style>
