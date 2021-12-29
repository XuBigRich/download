module.exports = {
    devServer:{
        proxy:  process.env.VUE_APP_API_BASE_URL
        // proxy:'http://172.16.19.160:8102/'

    },
}