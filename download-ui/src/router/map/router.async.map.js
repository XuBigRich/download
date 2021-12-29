const routes = {
    login: {
        path: "/",
        name: '登录',
        component: () => import("@/pages/login/login.vue")
    },
    index: {
        path: "/index",
        name: '首页', // 页面从定向到首页
        component: () => import("@/pages/index/index")
    },
}


export default routes;
