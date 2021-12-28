import Vue from "vue";
import Vuex from "vuex";
import { postWisdom } from '../request/http';
import api from '../request/api';
Vue.use(Vuex);

import analy from "./modules/dataAnaly";
import chapterActive from "./modules/chapterActive";

export default new Vuex.Store({
	state: {
		userInfo: {}, // 用户信息
		openIdx: -1,
		leaveShow: false, // 一会回来
		leaveRouterPath: '' // 离开要去的路径
	},	
	mutations: {
		setUserInfo(state, val) {
			state.userInfo = val;
		},
		updateUserInfo(state, val) {
			state.userInfo[val.key] = val.value;
			sessionStorage.setItem("userInfo", JSON.stringify(state.userInfo));
		},
		leaveShowChange(state, value) {
			state.leaveShow = value;
		},
		leaveRouterPathChange(state, value) {
			state.leaveRouterPath = value;
		}
	},
	actions: {
		setUserInfo({ state }) {
			postWisdom(api.userInfo).then(res => {
				state.userInfo = res.data.data;
				sessionStorage.setItem("userInfo", JSON.stringify(res.data.data));
			});
		}
	},
	modules: {
		analy,
		chapterActive
	}
});
