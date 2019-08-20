module.exports = {
    config: {
        '/apis': {
            target: 'http://10.64.152.224:8081',
            secure: false,  // https配置
            changeOrigin: true, //是否跨域
            pathRewrite: {
                '^/apis': ''
            }
        }
    }
}