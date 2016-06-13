#if [ -z "${GIT_REPO+xxx}" ]; then echo "GIT_REPO varibale is not set while running the docker container"; exit ; fi
GIT_REPO=https://github.com/ModernAristotle/aristotle.git
rm -fR repo

git clone $GIT_REPO repo

cd repo


#First Just Compile the source

if [ -z "${MVN_COMMAND+xxx}" ];
then
mvn clean install
else
mvn $MVN_COMMAND
fi

#Now start Member Server
export dbserver=jdbc:mysql://localhost:3305/ss_db_central?characterEncoding=UTF-8
export captcha_site_key=6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI
export captcha_site_secret=vFI1TnRWxMZNFuojJ4WifJWe
cd member/member

mvn clean install docker:build
 
docker run --name=AristotleBuild --net="host" -e user_search_end_point=search-sa-users-vog5arqjslh4wkqhjcnncwia44.us-west-2.cloudsearch.amazonaws.com -e user_document_end_point=doc-sa-users-vog5arqjslh4wkqhjcnncwia44.us-west-2.cloudsearch.amazonaws.com -e dbuser=root -e dbpassword=password -e dbserver=jdbc:mysql://localhost:3305/ss_db_central?characterEncoding=UTF-8 -e user_cloud_search_domain_name=sa-users -e aws_region=US_WEST_2 -e aws_access_key=AKIAIXBOMVIQJQ47RDCQ -e aws_access_secret=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 -e planned_tweet_queue=DEV_PLANNED_TWEET_QUEUE -e donation_queue_name=https://incorrect-sqs.us-west-2.amazonaws.com/954166957740/PROD-CentraAppQueue -e donation_queue_name=https://incorrect-sqs.us-east-1.amazonaws.com/365163509554/MyAap-DonationUpdateQueue -e poll_vote_queue_name=https://incorrect-sqs.us-west-2.amazonaws.com/973898665362/PollVotingQueue -e donation_queue_region=US_WEST_1 -e instamojoApiKey=fbb6d3464473f3b7fe07c11cfb1be64a -e instamojoApiAuthToken=78d0bf1368ea167716fe42d9931cb490 -e donation_cloud_search_domain_name=donation -e refresh_donation_queue=PROD_REFRESH_DONATION_QUEUE -e refresh_user_queue=PROD_REFRESH_USER_QUEUE -e registration_mail_id=registration@swarajabhiyan.org -e google_api_key=AIzaSyD5op2kuyCJmXNGWB7rjrh94zShmC6gVw0 -e smsTransactionalUrlTemplate='http://cloud.smsindiahub.in/vendorsms/pushsms.aspx?user=SWARAJ&password=swaraj@abhiyan123&msisdn={mobileNumber}&sid=SWARAJ&msg={message}&fl=0&gwid=2' -e smsPromotionalUrlTemplate='http://cloud.smsindiahub.in/vendorsms/pushsms.aspx?user=SWARAJ&password=swaraj@abhiyan123&msisdn={mobileNumber}&sid=WEBSMS&msg=test%20message&fl=0' -d ping2ravi/member
echo 'Sleeping for 30 seconds to wait for server to start'
sleep 30
echo 'woke up after 30 seconds, will be running tests now'
cd ../../

if [ -z "${MVN_COMMAND+xxx}" ];
then
mvn clean install
else
mvn $MVN_COMMAND
fi

export BUILD_OUTPUT=/tmp
echo 'Copy Target folder * $BUILD_OUTPUT/.'

cp -fR * $BUILD_OUTPUT/.
