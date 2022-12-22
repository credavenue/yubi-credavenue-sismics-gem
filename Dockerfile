FROM jetty:9.4.50-jdk11-amazoncorretto
# TODO: It should not be root. added only for dev
USER root
ADD docs.xml /var/lib/jetty/webapps/docs.xml
ADD docs-web/target/docs-web-*.war /var/lib/jetty/webapps/docs.war
