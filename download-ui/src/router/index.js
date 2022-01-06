import Vue from "vue";
import VueRouter from "vue-router";





Vue.use(VueRouter);

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
