CAS Overlay Template
============================

Generic CAS WAR overlay to exercise the latest versions of CAS. This overlay could be freely used as a starting template for local CAS war overlays. The CAS services management overlay is available [here](https://github.com/apereo/cas-services-management-overlay).

# Versions

```xml
<cas.version>5.3.x</cas.version>
```

# Requirements

* JDK 1.8+

# Configuration

The `etc` directory contains the configuration files and directories that need to be copied to `/etc/cas/config`.

# Build

To see what commands are available to the build script, run:

```bash
./build.sh help
```

To package the final web application, run:

```bash
./build.sh package
```

To update `SNAPSHOT` versions run:

```bash
./build.sh package -U
```

# Deployment

- Create a keystore file `thekeystore` under `/etc/cas`. Use the password `changeit` for both the keystore and the key/certificate entries.
- Ensure the keystore is loaded up with keys and certificates of the server.

On a successful deployment via the following methods, CAS will be available at:

* `http://cas.server.name:8080/cas`
* `https://cas.server.name:8443/cas`

## Executable WAR

Run the CAS web application as an executable WAR.

```bash
./build.sh run
```

## Spring Boot

Run the CAS web application as an executable WAR via Spring Boot. This is most useful during development and testing.

```bash
./build.sh bootrun
```

### Warning!

Be careful with this method of deployment. `bootRun` is not designed to work with already executable WAR artifacts such that CAS server web application. YMMV. Today, uses of this mode ONLY work when there is **NO OTHER** dependency added to the build script and the `cas-server-webapp` is the only present module. See [this issue](https://github.com/spring-projects/spring-boot/issues/8320) for more info.


## Spring Boot App Server Selection

There is an app.server property in the `pom.xml` that can be used to select a spring boot application server.
It defaults to `-tomcat` but `-jetty` and `-undertow` are supported.

It can also be set to an empty value (nothing) if you want to deploy CAS to an external application server of your choice.

```xml
<app.server>-tomcat<app.server>
```

## Windows Build

If you are building on windows, try `build.cmd` instead of `build.sh`. Arguments are similar but for usage, run:

```
build.cmd help
```

## External

Deploy resultant `target/cas.war`  to a servlet container of choice.


## Command Line Shell

Invokes the CAS Command Line Shell. For a list of commands either use no arguments or use `-h`. To enter the interactive shell use `-sh`.

```bash
./build.sh cli
```


## https 安装脚本
keytool -genkey -alias cas -keyalg RSA -validity 3650 -keystore caskeystore

keytool -export -alias cas -file cas.cer -keystore caskeystore -validity 3650

keytool -import -keystore "C:/Program Files/Java/jdk1.8.0_181/jre/lib/security/cacerts" -file D:/keys/cas.cer -alias cas -storepass changeit


#Rest 认证
步骤1:
POST  https://localhost:8443/cas/v1/tickets
headers:
        Content-Type:application/x-www-form-urlencoded
body:
    username:XXX
    password:XXX
    
步骤2：
据返回的连接换取ST
POST https://localhost:8443/cas/v1/tickets/TGT-2-ADSvy3N5CVprIw3RU7oMoIPF-CRbRZo8M0MDCDYX6C2bmFFMmD9RvrPCA65adIOj-fQUnisInsight-PC
headers:
        Content-Type:application/x-www-form-urlencoded
body:
    service:http://localhost:8081/client/（注意要带客服端的全路径不然请求的ST不能做登录）
    
步骤3：
    据返回的ST访问客户端地址，即可免登录
    http://localhost:8081/client/?ticket=ST-5-ZmD9nzvGOnv9KImb6KO7-2KrzNsUnisInsight-PC