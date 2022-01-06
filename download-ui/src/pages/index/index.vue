<template>
  <div class="uploadWrapper">
    <div class="btnUpload">
      <input type="file" ref="inputer">
      <div id="picker" class="form-control-focus">点击选择文件上传</div>
    </div>
    <button class="login-btn" @click="startUpload">开始上传</button>
  </div>
</template>

<script>
import {uploadByPieces} from './upload' //引入MD5加密
export default {
  name: "upload",
  data() {
    return {
      projectContract: {},
    }
  },
  mounted() {
  },
  methods: {
    startUpload() {
      console.log("执行了")
      let inputDOM = this.$refs.inputer;
      console.log(inputDOM)
      this.file = inputDOM.files[0];// 通过DOM取文件数据
      console.log(this.file)
      //执行分片上传
      uploadByPieces({
        file: this.file, // 视频实体
        pieceSize: 3, // 分片大小
        success: data => {
          console.log('分片上传视频成功', data)
        },
        error: e => {
          console.log('分片上传视频失败', e)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
