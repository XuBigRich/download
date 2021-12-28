import Vue from 'vue'
import App from './App.vue'
import initRouter from './router'
import EchojoyMessage from '@/components/public/message.js'
import api from "./request/api";
import store from "./store";
Vue.prototype.api = api;
Vue.prototype.$message = EchojoyMessage;

const router = initRouter(true)
Vue.config.productionTip = false

new Vue({
    render: h => h(App),
    store,
    router,
}).$mount('#app')
