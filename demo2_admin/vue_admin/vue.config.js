/**
 * 文件名：vue.config.js
 * 描述：项目配置文件
 * 作者：TechRice
 * 时间：2021-6-20
 */

 module.exports = {
    // 项目路径
    // publicPath: './',    // 配置为相对路径，默认为根路径 '/'
    // 开发配置
    devServer: {
        open: true,      // 自动打开浏览器
        hot: true,       // 实时打包编译
        inline: true,    // 表示实时刷新浏览器
        port: '8080'     // 指定端口号，默认为 8080
    }
}