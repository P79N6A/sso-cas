// 引入依赖
const express = require('express');
const path = require('path');
const ejs = require('ejs');
const session = require('express-session');
const cookieParser = require('cookie-parser');
const MemoryStore = require('session-memory-store')(session);
const casInit = require("./casInit")

// 初始化web服务器
const app = express();
app.use(express.static(path.join(__dirname, 'dist')));
ejs.delimiter = '$';
app.engine('html', ejs.__express);
app.set('views', path.join(__dirname));
app.set('view engine', 'html');
app.use(cookieParser())

// cas url 和认证service
app.use(session({
  name: 'NSESSIONID',
  secret: 'Hello I am a long long long secret',
  store: new MemoryStore(), 
}));


// 获取配置数据并接入单点登录
casInit(app)

module.exports = app;