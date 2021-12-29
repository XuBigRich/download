const routes = [
    {
        path: "/",
        redirect: '/login'
    },
    {
        path: "/login",
        name: '登录', // 页面从定向到首页
        component: () => import("../../pages/login/login")
    },
    {
        path: "/index",
        name: '首页', // 页面从定向到首页
        component: () => import("@/pages/index/index")
    },
]

export default routes;
