module.exports = {
    config: {
        '/apis': {
            target: 'http://161.117.199.186:6060',
            secure: false,  // https配置
            changeOrigin: true, //是否跨域
            pathRewrite: {
                '^/apis': ''
            }
        }
    }
}