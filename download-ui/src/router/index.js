import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);

const routes = [
    {
        path: "/",
        redirect: '/index' // 页面从定向到首页
    }
]

const router = new VueRouter({
    mode: "hash",
    // base: process.env.BASE_URL,
    routes
});


export default router;