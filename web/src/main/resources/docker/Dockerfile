FROM ping2ravi/tomcat:8.5.0_oracle_jdk8.92.14_ubuntu16.04
MAINTAINER Ravi Sharma 

ADD ./ROOT.war ROOT.war
RUN rm -fR $TOMCAT_HOME/webapps/*; cp ROOT.war $TOMCAT_HOME/webapps/.
