FROM 192.168.111.26/framework/tomcat:8.0.53
WORKDIR /opt/tomcat/webapps
ADD *.war sso.war
RUN sh -c "mkdir sso && mv sso.war sso/"
RUN sh -c "cd sso && jar -xvf sso.war && rm -f sso.war"
WORKDIR /opt/tomcat
ENTRYPOINT ["/bin/sh","-c","sh ./bin/startup.sh  && tail -f /dev/null"]