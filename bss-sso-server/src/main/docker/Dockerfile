FROM 192.168.111.26/tomcat/tomcat:8.5.3
WORKDIR /opt/tomcat/webapps
ADD cas sso
WORKDIR /opt/tomcat
ADD startup.sh .
RUN  chmod +x startup.sh
ENTRYPOINT ["/bin/sh","-c","sh startup.sh  && tail -f /dev/null"]