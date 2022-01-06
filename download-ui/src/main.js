import Vue from 'vue'
import App from './App.vue'
import ElementUI from "element-ui";

import EchojoyMessage from '@/components/public/message.js'
import api from "./request/api";
import store from "./store";
// import $ from 'jquery'
// import webuploader from "webuploader"


Vue.prototype.api = api;
Vue.prototype.$message = EchojoyMessage;


// import router from './router'

import initRouter from './router'

console.log(initRouter)
const router = initRouter(false)
console.log(router)
Vue.config.productionTip = false

Vue.use(ElementUI);
// Vue.use($)
// Vue.use(webuploader)

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
