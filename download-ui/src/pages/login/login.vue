<template>
  <div class="login">
    <div class="login-container">
      <div class="login-head">
        <div class="logo">
          <img src="@/assets/images/top_logo.png" alt="">
          <div class="logo-text">
            <span>LAMS</span>
            <span>学习活动管理系统</span>
          </div>
        </div>
        <div class="download-btn" @click="downloadClient">
          <img src="@/assets/images/login_ico_download_dis.png" alt="">
          <span>下载客户端</span>
        </div>
      </div>
      <div class="logo-banner">
        <img src="@/assets/images/login_img_banner.png" alt="">
      </div>
      <div class="logoin-box">
        <ul class="login-title">
          <li>环环相扣</li>
          <li>精准教学</li>
        </ul>
        <div class="login-method" v-if="!forgotPasswordState" @keypress.enter="hLogin">
          <ul>
            <li
                v-for="(item,index) in loginMethods"
                :key="index"
                @click="loginMethodsIdx = index"
                :class="[loginMethodsIdx == index ? 'active-li' : '']">
              {{item.name}}
            </li>
          </ul>
          <div class="line"></div>
          <!-- 二维码登录 -->
          <div class="QR-code" v-if="loginMethodsIdx == 0">
            <div class="code-img">
              <img src="@/assets/images/code.png" alt="">
            </div>
            <span>新用户？首次登录请点击我呦~</span>
            <a href="javascript:;">请使用LAMS移动端扫码登录</a>
          </div>
          <!-- 账号密码登录 -->
          <div class="account-password" v-else>
            <div class="account">
              <input type="text" v-model="userInfo.username" placeholder="请输入账号/手机号">
              <img src="@/assets/images/login_ico_user.png" alt="">
            </div>
            <div class="password">
              <input type="password" ref="pwd" v-model="userInfo.password" placeholder="请输入密码">
              <img src="@/assets/images/login_ico_key.png" alt="">
              <template>
                <img v-if="togglePwd" class="login-eye" src="@/assets/images/login_ico_eye.png" @click="hTogglePwd">
                <img v-else class="login-close" src="@/assets/images/login_ico_close.png" @click="hTogglePwd">
              </template>
            </div>
            <div class="password-options">
              <div class="remember-password" @click="rememberPassword = !rememberPassword">
                <img v-if="rememberPassword" src="@/assets/images/list_ico_hook.png" alt="">
                <div v-else class="circle"></div>
                <span>记住密码</span>
              </div>
              <span class="forget-password" @click="hForgotPassword">忘记密码?</span>
            </div>
            <div class="login-btn" @click="hLogin">登录</div>
          </div>
        </div>
        <!-- 忘记密码 -->
        <forgotPassword v-else @updateForgot="updateForgot"></forgotPassword>
        <div class="bottom-agreement">
          <span>使用协议</span>
          <div class="line"></div>
          <span>隐私政策</span>
        </div>
      </div>
    </div>
    <div class="record">备案号 鲁ICP备17011970号-10</div>
  </div>
</template>

<script>
import CryptoJS from "crypto-js";
import forgotPassword from '@/components/login/forgotPassword.vue';
export default {
  components: {
    forgotPassword
  },
  data() {
    return {
      userInfo: {
        username: "",
        password: "",
        role: "0"
      },
      loginMethods: [
        { name: "扫码登录" },
        { name: "账号密码" }
      ], // 登录方式
      loginMethodsIdx: 1, // 登录方式选中项索引
      rememberPassword: false, // 记住密码
      togglePwd: true, // 密码明文密文转换
      forgotPasswordState: false
    };
  },
  mounted() {
    sessionStorage.clear();
    this.getCookie();
  },
  methods: {
    // 下载客户端
    downloadClient() {
      this.$message({
        message: "客户端功能暂未开放，如需使用敬请留意",
        type: "info",
        duration: 1000
      });
    },
    // 密码明文，密文转换
    hTogglePwd() {
      this.$refs.pwd.type = this.$refs.pwd.type == "password" ? "text" : "password";
      this.togglePwd = this.$refs.pwd.type == "password" ? true : false;
    },
    // 忘记密码
    hForgotPassword() {
      this.forgotPasswordState = true;
    },
    // 修改忘记密码状态
    updateForgot(val) {
      this.forgotPasswordState = val;
    },
    // 登录前做基础校验，校验通过做登录操作
    hLogin() {
      let { username, password, role} = this.userInfo;
      if (username == '' || password == '') {
        this.$message({
          type: "info",
          message: "用户名或密码不能为空",
          duration: 1000
        });
        return;
      } else if (password.length < 6) {
        this.$message({
          type: "info",
          message: "密码长度最小为6",
          duration: 1000
        });
        return;
      }
      this.doLogin(username, password, role);
    },
    // 调用接口，做登录操作
    doLogin(username, password, role) {
      let { rememberPassword } = this;
      let data = {
        username,
        password,
        role
      };
      this.postKnow(this.api.login, data).then((res) => {
        if (res.data.code === 200) {
          if (rememberPassword) {
            this.setCookie(username, password, 10);
          } else {
            this.clearCookie();
          }
          this.$message({
            type: "success",
            message: "登录成功",
            duration: 1000
          });
          this.$store.dispatch('setUserInfo');
          sessionStorage.setItem("userId", res.data.data.id);
          sessionStorage.setItem("token", res.data.data.token);
          sessionStorage.setItem("myClass", JSON.stringify(res.data.data.classList));
          username = '';
          password = '';
          this.$router.replace("/index");
        } else {
          this.$message({
            message: "用户名或密码错误，请重新登录！",
            type: "warning",
            duration: 1000
          });
        }
      });
    },
    setCookie(username, password, exdays) {
      let userPwd = CryptoJS.AES.encrypt(password, 'secret key 123'); // 使用CryptoJS方法加密
      let exdate = new Date(); // 获取时间
      exdate.setTime(exdate.getTime() + 24 * 60 * 60 * 1000 * exdays);
      // 字符串拼接cookie
      window.document.cookie = 'username=' + username + ';path=/;expires=' + exdate.toGMTString();
      window.document.cookie = 'password=' + userPwd + ';path=/;expires=' + exdate.toGMTString();
    },
    getCookie() {
      if (document.cookie.length > 0) {
        let arr = document.cookie.split(';'); // 这里显示的格式需要切割一下自己可输出看下
        for (let i = 0; i < arr.length; i++) {
          let arr2 = arr[i].split('='); // 再次切割
          // 判断查找相对应的值
          if (arr2[0].trim() == 'username') {
            this.userInfo.username = arr2[1]; // 保存到保存数据的地方
          } else if (arr2[0].trim() == 'password') {
            // 拿到拿到加密后的密码arr2[1]并解密
            let bytes = CryptoJS.AES.decrypt(arr2[1].toString(), 'secret key 123');
            let plaintext = bytes.toString(CryptoJS.enc.Utf8); // 拿到解密后的密码（登录时输入的密码）
            this.userInfo.password = plaintext;
          }
        }
        this.rememberPassword = true;
      }
      this.userInfo.role = "0";
    },
    // 清除Cookie
    clearCookie() {
      this.setCookie('', '', -1); // 修改2值都为空，天数为负1天就好了
    }
  }
};
</script>

<style lang="less" scoped>
@import "@/assets/css/views/login/login.less";
.record{
  text-align: center;
  margin-top: 40px;
  font-size: 14px;
  color: #666;
}
</style>
