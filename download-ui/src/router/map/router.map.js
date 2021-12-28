const routes = [
    {
        path: "/",
        redirect: '/index', // 页面从定向到首页
        component: ()=>import("@/pages/index/")
    }
]

export default routes;
