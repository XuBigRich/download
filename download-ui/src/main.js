import Vue from 'vue'
import App from './App.vue'
import initRouter from './router'
const router = initRouter(true)
initRouter()
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
