const request = require('request');
const createError = require('http-errors');
const ConnectCas = require('connect-cas2');
const signale = require('signale');
const bodyParser = require('body-parser');

const configInfo = require("./config/index");

let config = configInfo

function initCasClient({ defaultConfig = {}, outerConfig }, app) {

  function getCasInstance(conf) {
    return new ConnectCas({
      debug: true,
      // 忽略权限认证地址
      ignore: [
        "/keep-online"
      ],
      match: [],
      // NODE服务地址
      servicePrefix: conf.appUrl,
      // CAS服务端地址
      serverPath: conf.infraApiOrigin,
      paths: {
        validate: '/cas/validate',
        serviceValidate: '/sso/serviceValidate',
        proxy: '',
        login: '/sso/login',
        logout: '/sso/logout',
        proxyCallback: '',
      },
      redirect: false,
      gateway: false,
      renew: false,
      slo: true,
      cache: {
        enable: true,
        ttl: 5 * 60 * 1000,
        filter: [],
      },
      fromAjax: {
        header: 'x-client-ajax',
        status: 418,
      },
    });
  }

  const casDefault = getCasInstance(defaultConfig)
  const casDefaultCore = casDefault.core()
  let casOuter = null
  let casOuterCore = null
  if (outerConfig) {
    casOuter = getCasInstance(outerConfig)
    casOuterCore = casOuter.core()
  }

  app.use((req, res, next) => {
    if (req.cookies.outer || req.query.outer && outerConfig) {
      casOuterCore(req, res, next)
    } else {
      casDefaultCore(req, res, next)
    }
  })

  // app.use(casClient.core());

  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded({ extended: true }));

  // 应用登录退出调用接口
  app.get('/logout', function (req, res, next) {
    // res.cookie('usercode','');
    if (req.cookies.outer || req.query.outer && outerConfig) {
      casOuter.logout(req, res, next)
    } else {
      casDefalt.logout(req, res, next)
    }
    // casClient.logout()(req, res, next);
  });

  // refreshtoken
  app.get('/keep-online', function (req, res, next) {
    const reToken = req.cookies.reToken
    let config = null
    if (req.cookies.outer || req.query.outer && outerConfig) {
      config = outerConfig
    } else {
      config = defaultConfig
    }

    res.header("Access-Control-Allow-Origin", "*");   //设置跨域访问
    console.log('reTokenreTokenreTokenreTokenreTokenreToken', reToken, typeof reToken)

    const reUrl = `${config.infraApiOrigin}/oauth/token?grant_type=refresh_token&refresh_token=${reToken}&client_id=f6bdbcf47dca566192230c402e2ed2d0&client_secret=5ebe2294ecd0e0f08eab7690d2a6ee69`
    console.log('refurl----------', reUrl)
    if (reToken === 'undefined') {
      console.log('token为空')
      res.send({
        msg: 'token为空'
      });
    } else {
      request(reUrl, {
        method: 'GET',
      }, function (err, response) {
        console.log('刷新token结果-------------', JSON.parse(response.body))
        const refresh_token = JSON.parse(response.body).refresh_token
        const access_token = JSON.parse(response.body).access_token
        access_token && res.cookie('usertoken', access_token)
        refresh_token && res.cookie('reToken', refresh_token)
        res.send(response);
      })
    }
  });

  // 登录中间件
  app.use('*', (req, res, next) => {
    let config = null
    if (req.cookies.outer || req.query.outer && outerConfig) {
      config = outerConfig
    } else {
      config = defaultConfig
    }
    const usercode = req.session.cas.user // req.session.usercode // 当usercode返回的是整型丢掉了0开头字符
    const username = req.cookies.username
    signale.debug('usercode,username', usercode, username)
    res.cookie('usercode', usercode);

    function reqTokenData() {
      return new Promise((resolve) => {
        const getUserTokenUrl = `${config.infraApiOrigin}/oauth/token?username=f6bdbcf47dca566192230c402e2ed2d0&grant_type=password&client_id=f6bdbcf47dca566192230c402e2ed2d0&client_secret=5ebe2294ecd0e0f08eab7690d2a6ee69&password=5ebe2294ecd0e0f08eab7690d2a6ee69`
        console.log('getUserTokenUrlgetUserTokenUrl', getUserTokenUrl)
        request(getUserTokenUrl, {
          method: 'GET',
        }, function (err, response) {
          if (!err) {
            const refresh_token = JSON.parse(response.body).refresh_token
            const access_token = JSON.parse(response.body).access_token
            console.log('获取token成功------------------', getUserTokenUrl, JSON.parse(response.body), refresh_token, access_token)
            res.cookie('usertoken', access_token)
            res.cookie('reToken', refresh_token)
            resolve({
              refresh_token,
              access_token,
            })
          }
        })
      })
    }

    async function renderIndex() {
      const tokens = await reqTokenData()

      const url = `${config.infraApiOrigin}/api/infra-uuv/v0.1/users/${usercode}`
      signale.success('登录请求用户详情------', url, tokens)
      const userToken = tokens.access_token
      const user = `usercode:${usercode}&username:${username}`;
      request(url, {
        headers: {
          Authorization: `bearer ${userToken}`,
          "us-app": escape(escape("source:基础架构&version:1.0.0.1")),
          User: user,
        },
      }, (error, response, body) => {
        console.log('请求用户详情结束------------', userToken, user, response.statusCode)
        if (!error && response.statusCode == 200) {
          const data = JSON.parse(body)

          res.cookie('username', escape(data.user_name))
          res.cookie('orgID', escape(data.org_id))
          res.cookie('roleID', escape(data.role_id))
          res.cookie('theme', escape(data.theme))
        }

        res.render('index', {
          config,
          cache: false  // 页面不缓存
        });
      })
    }

    renderIndex()
  })

  // catch 404 and forward to error handler
  app.use((req, res, next) => {
    next(createError(404));
  });

  // error handler
  app.use((err, req, res, next) => {
    // set locals, only providing error in development
    res.locals.message = err.message
    res.locals.error = err
    signale.fatal('web服务器内部错误，请查看app.js---------------------------------', err)
    // render the error page
    res.status(err.status || 500);
    res.send('<p>something blew up</p>');
  })
}

function casInit(app) {
  if (configInfo.NODE_ENV !== 'local') {
    // 读取apollo配置中心

    function reqApolloData(configInfo) {
      const configApolloUrl = `${configInfo.configServerUrl}:${configInfo.apolloPort}/configs/${configInfo.appId}/${configInfo.clusterName}/${configInfo.namespaceName}`
      signale.debug('配置中心拉取', configApolloUrl)
      return new Promise((resolve, reject) => {
        request(configApolloUrl, (error, response, body) => {
          if (!error && response.statusCode == 200) {
            try {
              const result = JSON.parse(body)
              let configFromApollo = result && result.configurations
              console.log('configFromApollo-------------\n', configFromApollo)
              resolve({
                ...config,
                ...configFromApollo,
              });
            } catch (e) {
              console.error(`从 ${configApolloUrl} 获取数据失败`, e)
              signale.fatal('配置中心拉取数据异常')
              resolve(null)
            }
          } else {
            console.error(`从 ${configApolloUrl} 获取数据失败`, error, response.statusCode)
            resolve(null)
          }
        })
      })
    }

    async function casLogin() {
      const apolloConfigDefault = await reqApolloData(configInfo)
      // 如需外网访问，阿波罗需要添加 namespace: outer
      const apolloConfigOuter = await reqApolloData({ ...configInfo, namespaceName: 'outer' })
      const allConfig = { defaultConfig: apolloConfigDefault, outerConfig: apolloConfigOuter }
      signale.debug('最终配置', allConfig)
      initCasClient(allConfig, app)
    }

    casLogin()
  } else {
    initCasClient({ defaultConfig: config }, app)
  }
}

module.exports = casInit;
