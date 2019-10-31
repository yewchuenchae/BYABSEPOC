<template>
  <div class="jl_dialog" :class="{hide:!showDlg}">
    <div class="UImask" @click="maskHandle"></div>
    <div class="UIdialog">
      <div class="UIdialog_hd">
        <strong class="UIdialogTitle">{{title}}</strong>
        <span class="close" @click="onCancel">×</span>
      </div>
      <div class="UIdialog_bd">
        <p class="title">Find your Schneider Electric Relays & Machine Drives</p>
        <div class="content">
            <div style="width:150px;height:50px;background: #E9E9E9;display: flex;justify-content: center;align-items: center;">
                <img src="../assets/img/search_camera.png" alt="" width="25px" height='25px'>
                <span style="margin-left: 10px">Photo</span>
            </div>
        </div>
        <p class="desc">This Application is support on Android 8.0 and Above,and IO12.3.1and above. The image search works best with photo that are at least 6MP.</p>
      </div>
      
    </div>
  </div>
</template>

<script>
export default {
  name: "jlDialog",
  props: {
    showDlg: {
      type: Boolean,
      default: false
    },
    title: {
      default: null
    },
    isDisabled: {
      type: Boolean,
      default: false
    },
    maskHide: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    maskHandle() {
      if (this.maskHide) {
        console.log(2222);
        this.$emit("onCancel");
      }
    },
    onConfirm() {
      if (!this.isDisabled) {
        this.$emit("onConfirm");
      }
    },
    onCancel() {
      this.$emit("onCancel");
    }
  }
};
</script>
  <style lang="scss" scoped>
.UImask {
  position: fixed;
  z-index: 1000;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
}
.UIdialog {
  text-align: left;
  position: fixed;
  z-index: 5000;
  width: 80%;
  max-width: 600px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  text-align: center;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #eee;
}
.UIdialog_hd {
  height: 30px;
  position: relative;
  .close{
      position: absolute;
      top: 20px;
      right: 20px;
      font-size: 20px;
      color: #999999;
      cursor: pointer;
  }
}
.UIdialogTitle {
  font-weight: 400;
  font-size: 18px;
}
.UIdialog_bd {
  padding: 0 30px 30px;
  min-height: 40px;
  word-wrap: break-word;
  word-break: break-all;
  .title{
    font-size:18px;
    font-family:PingFang-SC-Heavy,PingFang-SC;
    font-weight:800;
    color:rgba(51,51,51,1);
    line-height:25px;
  }
  .content{
      width: 100%;
      height: 159px;
      background: #F6F6F6;
      margin: 30px 0 20px 0;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
  }
  .desc{
    font-size:14px;
    font-family:PingFangSC-Regular,PingFang SC;
    font-weight:400;
    color:rgba(51,51,51,1);
    line-height:21px;
  }
}
.hide{
    display: none;
}
</style>