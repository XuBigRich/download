import Vue from "vue";
import VueRouter from "vue-router";





Vue.use(VueRouter);
// const routes = [
//     {
//         path: "/",
//         redirect: '/login'
//     },
//     {
//         path: "/login",
//         name: '登录', // 页面从定向到首页
//         component: () => import("@/pages/login/login")
//     },
//     {
//         path: "/index",
//         name: '首页', // 页面从定向到首页
//         component: () => import("@/pages/index/index.vue")
//     }
// ]


// const  router = new VueRouter({routes});
// export default router;

/**
 * 初始化路由实例
 * @param isAsync 是否异步路由模式
 * @returns {VueRouter}
 */
function initRouter(isAsync) {
    const routes = isAsync ? require('./map/router.async.map.js').default : require('./map/router.map.js').default
    console.log(routes)
    //这个地方的传参名 必须为routes，否则 路由不生效
    return new VueRouter({routes})
}

export default initRouter;
