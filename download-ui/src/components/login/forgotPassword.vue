<template>
	<div class="forgot-password">
		<!-- 忘记密码-验证身份 -->
		<div class="forgot-password-container" v-if="!updatePasswordState">
			<div class="forgot-title">
				<h3>验证身份</h3>
				<div class="title-right">
					<span>记起密码?</span>
					<a href="javascript:;" @click="hGoLogin">去登录</a>
				</div>
			</div>
			<div class="verification">
				<div class="account">
					<img src="@/assets/images/login_ico_phone.png">
					<input type="text" v-model="userInfo.call" placeholder="请输入与账户绑定的手机号码">
				</div>
				<div class="code">
					<div class="code-input">
						<img src="@/assets/images/login_ico_code.png">
						<input type="text" v-model="userInfo.code" placeholder="请输入验证码">
					</div>
					<div class="send" @click="hGetCode">{{show ? '发送验证码' : `重新发送(${count}s)`}}</div>
				</div>
				<button
					class="next-btn"
					:disabled="userInfo.call == '' || userInfo.code == ''"
					:style="`background: ${userInfo.call !== '' && userInfo.code !== '' ? '#615dff;' : ''}`"
					@click="hNext">下一步</button>
			</div>
		</div>

		<!-- 验证身份成功-修改密码 -->
		<div class="update-password" v-else>
			<div class="update-title">
				<div class="title-left" @click="hPrevious">
					<img src="@/assets/images/login_ico_back.png">
					<span>上一步</span>
				</div>
				<h3>修改密码</h3>
				<div class="title-right">
					<span>记起密码</span>
					<a href="javascript:;" @click="hGoLogin">去登录?</a>
				</div>
			</div>
			<div class="update-form">
				<div class="new-password">
					<input type="password" v-model="userInfo.password" placeholder="请输入新密码(6位)">
				</div>
				<div class="new-password-again">
					<input type="password" v-model="userInfo.againPassword" placeholder="请再次输入密码">
				</div>
				<button class="complete-btn" @click="hUpdateComplete" :disabled="userInfo.password == '' || userInfo.againPassword == ''" :style="`background: ${userInfo.password !== '' && userInfo.againPassword !== '' ? '#615dff;' : ''}`">完成，去登录</button>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			userId: "",
			userInfo: {
				call: '',
				code: '',
				password: "",
				againPassword: ""
			}, // 用户信息
			updatePasswordState: false, // 修改密码状态
			show: true, // 发送验证码/秒数过后重新获取 状态切换
			count: '', // 倒计时秒数
			timer: null // 定时器id
		};
	},
	methods: {
		// 记起密码，回到登录页
		hGoLogin() {
			this.$emit('updateForgot', false);
		},
		// 获取验证码
		hGetCode() {
			if (!this.show) return;
			let { call } = this.userInfo;
			if (this.isPhone(call)) {
				let data = {
					phone: call
				};
				this.postWisdom(this.api.sendMessageAuth, data).then((res) => {
					if (res.data.code != 0) {
						this.$message({
							message: res.data.msg,
							type: "warning",
							duration: 1000
						});
					} else {
						this.countDown();
					}
				}).catch(err=>{
					console.log(err);
				});
			} else {
				this.$message({
					message: "请输入正确格式手机号",
					type: "warning",
					duration: 1000
				});
			}
		},
		// 验证码倒计时
		countDown() {
			const TIME_COUNT = 60;
			if (!this.timer) {
				this.count = TIME_COUNT;
				this.show = false;
				this.timer = setInterval(() => {
					if (this.count > 0 && this.count <= TIME_COUNT) {
						this.count--;
					} else {
						this.show = true;
						clearInterval(this.timer);
						this.timer = null;
					}
				}, 1000);
			}
		},
		// 忘记密码-验证通过，跳转至修改密码
		hNext() {
			let { call, code } = this.userInfo;
			if (call == '' || code == '') {
				this.$message({
					message: "手机号或验证码不允许为空",
					type: "warning",
					duration: 1000
				});
			} else {
				let data = {
					phone: call,
					code
				};
				this.postWisdom(this.api.confirmIdentity, data).then(res => {
					if (res.data.code == 0) {
						this.userId = res.data.data;
						this.updatePasswordState = true;
					} else {
						this.$message({
							message: res.data.msg,
							type: "warning",
							duration: 1000
						});
					}
				});
			}
		},
		// 修改密码-回到上一步-验证身份
		hPrevious() {
			this.updatePasswordState = false;
		},
		// 修改密码成功，重新登录
		hUpdateComplete() {
			let { password, againPassword } = this.userInfo;
			if (!password) {
				this.$message({
					type: "warning",
					message: "请输入新密码"
				});
				return;
			}
			if (password.length > 6) {
				this.$message({
					type: "warning",
					message: "密码长度不得超过6位"
				});
				return;
			}
			if (password == againPassword) {
				let data = {
					userId: this.userId,
					newPassword: password,
					confirmPassword: againPassword
				};
				this.postWisdom(this.api.updatePassword, data).then(res => {
					if (res.data.code == 0) {
						this.$message({
							message: "修改成功,请重新登录",
							type: "success",
							duration: 1000
						});
						this.hGoLogin();
					} else {
						this.$message({
							message: res.data.msg,
							type: "warning",
							duration: 1000
						});
					}
				});
			} else {
				this.$message({
					message: "两次密码不一致,请重新输入",
					type: "info",
					duration: 1000
				});
			}
		}
	}
};
</script>

<style lang="less" scoped>
@import "@/assets/css/components/login/forgotPassword.less";
</style>
