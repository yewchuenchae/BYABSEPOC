module.exports = {
    config: {
        '/apis': {
            // // target: 'http://161.117.199.186:6060',
            target: 'http://161.117.94.146:8080',
            secure: false,  // https配置
            changeOrigin: true, //是否跨域
            pathRewrite: {
                '^/apis': ''
            }
        }
    }
}