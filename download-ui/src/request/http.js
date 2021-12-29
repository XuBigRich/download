import Vue from "vue";
import axios from "axios";
import Qs from "qs";
import router from "../router";

// 李晓 http://172.16.18.171:8082
axios.defaults.baseURL = "";
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

// 请求拦截
axios.interceptors.request.use(
	config => {
		console.log(config)

		if (!config.headers.noConfigToken) {
			config.headers.Authorization = sessionStorage.getItem('token') || '';
		}
		// config.headers.("Access-Control-Allow-Headers", "X-Requested-With");
		// config.headers("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
		console.log(config)
		return config;
	},
	error => {
		return Promise.error(error);
	}
);

// 响应拦截
axios.interceptors.response.use(
	response => {
		if (response.status == 200) {
			if (response.data.code == 403 || response.data.code == 105) {
				if (document.getElementsByClassName('el-message').length == 0) {
					this.$message({
						type: 'error',
						message: response.data.msg,
						duration: 1000
					});
				}
				router.replace({
					path: "/login"
				});
				return false;
			} else {
				return Promise.resolve(response);
			}
		} else {
			return Promise.reject(response);
		}
	},
	error => {
		this.message.error(error.response.data.msg);
		return Promise.reject(error);
	}
);

// json格式
const postWisdom = (url, params) => {
	return axios({
		method: "post",
		url,
		data: params,
		timeout: 15000
	}).then(res => {
		return Promise.resolve(res);
	}).catch(err => {
		return Promise.reject(err);
	});
};

const getWisdom = (url, data) => {
	return axios({
		method: "get",
		url,
		params: data,
		timeout: 15000
	}).then(res => {
		return Promise.resolve(res);
	}).catch(err => {
		return Promise.reject(err);
	});
};

//  from-data格式
const postKnow = (url, params) => {
	return axios({
		method: "post",
		url,
		data: Qs.stringify(params),
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		},
		timeout: 7000
	}).then(res => {
		return Promise.resolve(res);
	}).catch(err => {
		return Promise.reject(err);
	});
};

// 下载文件流
const downFile = (url, params) => {
	return axios({
		method: "post",
		url,
		data: Qs.stringify(params),
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
		},
		timeout: 7000,
		responseType: 'blob'
	}).then(res => {
		return Promise.resolve(res);
	}).catch(err => {
		return Promise.reject(err);
	});
};


export {
	postWisdom,
	getWisdom,
	postKnow
};

Vue.prototype.postWisdom = postWisdom;
Vue.prototype.getWisdom = getWisdom;
Vue.prototype.postKnow = postKnow;
Vue.prototype.downFile = downFile;
