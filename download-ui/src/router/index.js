import Vue from "vue";
import VueRouter from "vue-router";



/**
 * 初始化路由实例
 * @param isAsync 是否异步路由模式
 * @returns {VueRouter}
 */
function initRouter(isAsync) {
    console.log(isAsync)
    const options = isAsync ? require('./map/router.async.map.js').default : require('./map/router.map.js').default
    return new VueRouter({options})
}

Vue.use(VueRouter);


export default initRouter;
