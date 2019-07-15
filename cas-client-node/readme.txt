1.node server引入cas client先从github将依赖下载到本地
2.将app.js casInit.js以及github依赖文件到复制到node工程
3.修改app.js casInit.js中单点登录系统的地址
4.js中默认后端请求路径为"/sso",若后端更改需更改该路径
5.编译工程