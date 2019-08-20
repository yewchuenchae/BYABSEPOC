import axios from 'axios';

/** 
 * @method restful请求封装
 * @param api
 * @param {string} api.url 后台地址
 * @param {string} api.type 请求方式 get post put delete
 * @param {boolean} api.placeholder 是否启用占位符替换 true启用 false不启用 启用后会自动匹配url上的占位符与入参进行替换
 * @param {boolean} api.formData 是否启用formData数据格式如参 true启用 false不启用 启用后会自动将入参格式转为formData格式，仅在post请求有效
 * @param {object}request 请求参数 默认为空对象
 * @return {Params}
*/
function request(api = { type: 'get' }, request = {}) {
    // 替换占位符
    if (api.placeholder) {
        for (var i in request) {
            if (api.url.indexOf(`{${i}}`) != -1) {
                api.url = api.url.replace(`{${i}}`, `${request[i]}`);
            }
        }
    }
    // 创建一个promise并返回
    return new Promise((resolve, reject) => {
        // 设置请求超时定时器-此处我设置的150000毫秒
        let timeOut = setTimeout(() => {
            console.error('timeout：', '请求超时');
            reject(new Error('请求超时'))
        }, 15000)
        // get请求
        if (api.type == "get") {
            // 返回一个axios请求
            return axios({
                // 传入请求地址
                url: api.url,
                // 设置请求方式
                method: 'get',
                // 设置url携带参数
                params: { ...request },
                // 设置请求头
                headers: { 'Content-Type': 'application/json;charset=utf-8' },
            })
                .then(function (response) {
                    // 请求成功清除定时器
                    clearTimeout(timeOut);
                    // 执行resolve
                    resolve(response.data);
                })
                .catch(function (error) {
                    // 执行reject
                    reject(new Error(error));
                    // 控制台打印错误信息
                    console.error('error：', error);
                });
        } else
            // post请求
            if (api.type == "post") {
                // 转换数据格式为formData格式
                if (api.formData && api.type == "post") {
                    const formdata = new FormData();
                    for (let i in request) {
                        formdata.append(i, request[i])
                    }
                    request = formdata;
                }
                // 返回一个axios请求
                return axios({
                    // 传入请求地址
                    url: api.url,
                    // 设置请求方式
                    method: 'post',
                    // 设置body携带参数
                    data: request,
                    // 设置请求头
                    headers: { 'Content-Type': 'application/json;charset=utf-8' },
                })
                    .then(function (response) {
                        // 请求成功清除定时器
                        clearTimeout(timeOut);
                        // 执行resolve
                        resolve(response.data);
                    })
                    .catch(function (error) {
                        // 执行reject
                        reject(new Error(error));
                        // 控制台打印错误信息
                        console.error('error：', error);
                    });
            } else
                // put请求
                if (api.type == "put") {
                    // 返回一个axios请求
                    return axios({
                        // 传入请求地址
                        url: api.url,
                        // 设置请求方式
                        method: 'put',
                        // 设置body携带参数
                        params: {},
                        // 设置body携带参数
                        data: request,
                        // 设置请求头
                        headers: { 'Content-Type': 'application/json;charset=utf-8' },
                    })
                        .then(function (response) {
                            // 请求成功清除定时器
                            clearTimeout(timeOut);
                            // 执行resolve
                            resolve(response.data);
                        })
                        .catch(function (error) {
                            // 执行reject
                            reject(new Error(error));
                            // 控制台打印错误信息
                            console.error('error：', error);
                        });
                } else
                    // delete请求
                    if (api.type === "delete") {
                        // 返回一个axios请求
                        return axios({
                            // 传入请求地址
                            url: api.url,
                            // 设置请求方式
                            method: 'delete',
                            // 设置url携带参数
                            params: { ...request },
                            // 设置请求头
                            headers: { 'Content-Type': 'application/json;charset=utf-8' },
                        })
                            .then(function (response) {
                                // 请求成功清除定时器
                                clearTimeout(timeOut);
                                // 执行resolve
                                resolve(response.data);
                            })
                            .catch(function (error) {
                                // 执行reject
                                reject(new Error(error));
                                // 控制台打印错误信息
                                console.error('error：', error);
                            });
                    }
    })
}
export default request;