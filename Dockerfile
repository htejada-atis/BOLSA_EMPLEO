FROM tomcat:9-jdk11-openjdk-slim
LABEL Author="gestion@ujaen.es"
RUN rm -fr /usr/local/tomcat/webapps/ROOT
ADD Documentos/docker/wait-for-it.sh /
COPY builds/desarrollo/uv-desarrollo.war /usr/local/tomcat/webapps/ROOT.war