// https://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,
  parserOptions: {
    parser: 'babel-eslint'
  },
  env: {
    browser: true,
  },
  extends: [
    // https://github.com/vuejs/eslint-plugin-vue#priority-a-essential-error-prevention
    // consider switching to `plugin:vue/strongly-recommended` or `plugin:vue/recommended` for stricter rules.
    'plugin:vue/essential', 
    // https://github.com/standard/standard/blob/master/docs/RULES-en.md
    'standard'
  ],
  // required to lint *.vue files
  plugins: [
    'vue'
  ],
  // add your custom rules here
  rules: {
    "generator-star-spacing": 0, // 允许异步等待 async-await
    "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off", // 生产环境禁止使用debugger
    "semicolon": 0,
    "eol-last": 0, // 结尾处需要换行符
    "indent": 0, // 预期缩进2个空格
    "arrow-spacing": 0, // 箭头函数剪口与代码块见加空格
    "space-before-blocks": 0, // { 前缺少空格
    "space-before-function-paren": 0, // 函数括号后缺少空格
    "no-undef": 0, // 禁止使用未声明的变量
    "no-unused-expressions": 0, // 禁止使用未声明的表达式
    "no-unused-labels": 0, // 禁止使用未声明的标签
    "no-unused-vars": 0, // 禁止使用未声明的变量
    "semi": 0, // 禁止使用不必要的分号
    "comma-dangle": 0, // 禁止不必要的 , 尾随
    "quotes": 0, // 字符串必须使用单引号
    "eqeqeq": 0, // 使用"===",而不是"=="
    "keyword-spacing": 0, // 关键字后必须有一个空格
    "no-trailing-spaces": 0, // 禁止在行末添加空格尾随
    "no-multi-spaces": 0, // 禁止多个空格
    "key-spacing": 0, // 强制对象key与value中间添加一个空格
    "prefer-promise-reject-errors": 0, // promise中需要传入一个Error对象(new Error)
    "no-multiple-empty-lines": 0, //  文档结尾空行过多，默认0
    "comma-spacing": 0, // 逗号后边添加空格
    "space-infix-ops": 0, // 运算符周围有空格
    "camelcase": 0, // 强制驼峰命名法
    "object-curly-even-spacing": 0,
    "standard/object-curly-even-spacing": 0, // 大括号内使用空格风格一致
    "padded-blocks": 0,  // 禁止代码块空白行
    "spaced-comment": 0, // 注释前需要一个空格
    "no-new": 0,  // 不允许new一个实例后不赋值或者不比较
    "func-call-spacing": 0, // 禁止在函数名称与它的左括号间添加空格
    "no-duplicate-case": 2, // switch中的case标签不能重复
    "no-tabs": 0, // 禁止使用制表符
    "one-var": 0, // 声明变量不换行
  }
}
