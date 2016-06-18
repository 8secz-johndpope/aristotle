cwd=$(pwd)

BUILD_OUTPUT_DIRECTORY=/var/build-output

if [ -z "${HOST_JENKINS_HOME+xxx}" ];
then
HOST_JENKINS_HOME=${cwd}
fi

MVN_COMMAND=$1
if [ -z "${MVN_COMMAND+xxx}" ];
then
MVN_COMMAND=clean install -Dmaven.test.skip=true 
fi

echo "Build Output Directory : " $HOST_JENKINS_HOME/workspace/${JOB_NAME}/build-output
chmod -R 777 ~/.m2
docker run -e GIT_REPO=https://github.com/ModernAristotle/aristotle.git -e MVN_COMMAND='clean install -Dmaven.test.skip=true -f pom-member.xml' -e BUILD_OUTPUT=$BUILD_OUTPUT_DIRECTORY -v $HOST_JENKINS_HOME/workspace/${JOB_NAME}/build-output:$BUILD_OUTPUT_DIRECTORY -v ~/.m2:/root/.m2/ -v /var/run/docker.sock:/var/run/docker.sock -it ping2ravi/maven-selenium-build-docker
chmod -R 777 ~/.m2

cd $HOST_JENKINS_HOME/workspace/${JOB_NAME}/build-output/member/member/

mvn docker:build

export MYSQL_ROOT_PASSWORD=password
export MYSQL_DATABASE=ss_db_central
export MYSQL_PORT=3305
export MYSQL_DBSERVER=jdbc:mysql://localhost:${MYSQL_PORT}/${MYSQL_DATABASE}?characterEncoding=UTF-8

#start MySql
docker stop AristotleMysql
docker rm AristotleMysql
docker run --name=AristotleMysql -v ~/work/data/mysql/ss_db_central_temp:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} -e MYSQL_DATABASE=${MYSQL_DATABASE} -p ${MYSQL_PORT}:3306  -d mysql:5.5


#start Member Docker image
docker stop AristotleMemberBuild
docker rm AristotleMemberBuild

docker run --name=AristotleMemberBuild --net="host" -e user_search_end_point=${user_search_end_point} -e user_document_end_point=${user_document_end_point} -e dbuser=root -e dbpassword=${MYSQL_ROOT_PASSWORD} -e dbserver=${MYSQL_DBSERVER} -e user_cloud_search_domain_name=${user_cloud_search_domain_name} -e aws_region=${aws_region} -e aws_access_key=${aws_access_key} -e aws_access_secret=${aws_access_secret} -e planned_tweet_queue=${planned_tweet_queue} -e donation_queue_name=${donation_queue_name} -e poll_vote_queue_name=${poll_vote_queue_name} -e donation_queue_region=${donation_queue_region} -e instamojoApiKey=${instamojoApiKey} -e instamojoApiAuthToken=${instamojoApiAuthToken} -e donation_cloud_search_domain_name=${donation_cloud_search_domain_name} -e refresh_donation_queue=${refresh_donation_queue} -e refresh_user_queue=${refresh_user_queue} -e registration_mail_id=${registration_mail_id} -e google_api_key=${google_api_key} -e smsTransactionalUrlTemplate=${smsTransactionalUrlTemplate} -e smsPromotionalUrlTemplate=${smsPromotionalUrlTemplate} -e captcha_site_key=${captcha_site_key} -e captcha_site_secret=${captcha_site_secret} -d ping2ravi/member


#Run tests in Docker Image
docker stop AristotleMemberTestBuild
docker rm AristotleMemberTestBuild
docker run --name=AristotleMemberTestBuild --net="host" -e GIT_REPO=https://github.com/ModernAristotle/aristotle.git -e MVN_COMMAND='test -f pom-ui-test.xml' -e BUILD_OUTPUT=$BUILD_OUTPUT_DIRECTORY -e user_search_end_point=${user_search_end_point} -e user_document_end_point=${user_document_end_point} -e dbuser=root -e dbpassword=${MYSQL_ROOT_PASSWORD} -e dbserver=${MYSQL_DBSERVER} -e user_cloud_search_domain_name=${user_cloud_search_domain_name} -e aws_region=${aws_region} -e aws_access_key=${aws_access_key} -e aws_access_secret=${aws_access_secret} -e planned_tweet_queue=${planned_tweet_queue} -e donation_queue_name=${donation_queue_name} -e poll_vote_queue_name=${poll_vote_queue_name} -e donation_queue_region=${donation_queue_region} -e instamojoApiKey=${instamojoApiKey} -e instamojoApiAuthToken=${instamojoApiAuthToken} -e donation_cloud_search_domain_name=${donation_cloud_search_domain_name} -e refresh_donation_queue=${refresh_donation_queue} -e refresh_user_queue=${refresh_user_queue} -e registration_mail_id=${registration_mail_id} -e google_api_key=${google_api_key} -e smsTransactionalUrlTemplate=${smsTransactionalUrlTemplate} -e smsPromotionalUrlTemplate=${smsPromotionalUrlTemplate} -e captcha_site_key=${captcha_site_key} -e captcha_site_secret=${captcha_site_secret} -v $HOST_JENKINS_HOME/workspace/${JOB_NAME}/build-output:$BUILD_OUTPUT_DIRECTORY -v ~/.m2:/root/.m2/ -v /var/run/docker.sock:/var/run/docker.sock -it ping2ravi/maven-selenium-build-docker

docker stop AristotleMemberTestBuild
docker rm AristotleMemberTestBuild

docker stop AristotleMemberBuild
docker rm AristotleMemberBuild

docker stop AristotleMysql
docker rm AristotleMysql

