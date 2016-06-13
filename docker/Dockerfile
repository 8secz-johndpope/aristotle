FROM ping2ravi/maven-build-docker
MAINTAINER Ravi Sharma 

ADD ./runCustomMavenBuild.sh /
RUN chmod 755 /runCustomMavenBuild.sh

RUN echo $GIT_REPO
ENTRYPOINT ["sh", "/runCustomMavenBuild.sh"]