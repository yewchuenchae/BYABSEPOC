import rq from "./../rq";

let configUrl = process.env.NODE_ENV === "production" ? "/api" : "apis/api";
export default {
    upload(req){  // req入参 默认空对象----非必填
        return rq(
          {
            type: "post", // 请求方式get、post、delete、put，默认get----非必填
            url: `${configUrl}/product/search`, // 接口地址----必填
            placeholder: false, // 是否替换url接口占位符，默认不替换----非必填
            formData: true // 是否将数据转为formData----非必填
          },
          req
        );
    },
    search(req){  // req入参 默认空对象----非必填
        return rq(
          {
            type: "get", // 请求方式get、post、delete、put，默认get----非必填
            url: `${configUrl}/product/search/fuzzy`, // 接口地址----必填
            placeholder: false, // 是否替换url接口占位符，默认不替换----非必填
            formData: false // 是否将数据转为formData----非必填
          },
          req
        );
    },
};
