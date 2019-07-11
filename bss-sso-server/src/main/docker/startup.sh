#!/bin/sh
basePath=/opt/framework-sso/webapps/sso/WEB-INF/classes
if -d /opt/config/service;
    then
        cp -f /opt/config/service/** ${basePath}/service/**
fi
if  -f /opt/config/config.js;
    then
        cp -f /opt/config/config.js ${basePath}/static/themes/unisinsight/js/config.js
fi
if -f /opt/config/application.properties;
    then
        cp -f /opt/config/application.properties ${basePath}/application.properties
fi
if -d /opt/config/image; then
    cp -f /opt/config/images/* ${basePath}/static/themes/unisinsight/images/
fi
sh ./bin/startup.sh