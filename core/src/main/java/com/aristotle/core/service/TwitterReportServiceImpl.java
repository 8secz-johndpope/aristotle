package com.aristotle.core.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.EmailTemplate;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.Report;
import com.aristotle.core.persistance.Tweet;
import com.aristotle.core.persistance.repo.EmailTemplateRepository;
import com.aristotle.core.persistance.repo.PlannedTweetRepository;
import com.aristotle.core.persistance.repo.ReportRepository;
import com.aristotle.core.persistance.repo.TweetRepository;
import com.aristotle.core.persistance.repo.TwitterAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class TwitterReportServiceImpl implements TwitterReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private TwitterAccountRepository twitterAccountRepository;
    @Autowired
    private PlannedTweetRepository plannedTweetRepository;
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    private static final String DAILY_TWITTER_REPORT = "DailyTwitterReport";

    private ThreadLocal<DateFormat> dailyDateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };
    @Override
    public String genrateDailyTwitterReport(Date date) throws AppException {
        // TODO Auto-generated method stub
        JsonObject reportJsonObject = new JsonObject();
        reportJsonObject.addProperty("ReportCreationTime", new Date().toString());
        reportJsonObject.addProperty("ReportDate", date.toString());
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(date);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);

        addPlannedTweetCount(reportJsonObject, start.getTime(), end.getTime());
        addEachPlannedTweetDetail(reportJsonObject, start.getTime(), end.getTime());

        saveDailyReport(date, reportJsonObject.toString());

        return reportJsonObject.toString();
    }


    @Override
    public String getDailyTwitterReport(Date date) throws AppException {

        return null;
    }

    private void saveDailyReport(Date date, String reportContent){
        String reportDateTimeId = dailyDateFormat.get().format(date);
        Report report = reportRepository.getReportByReportDateTimeIdAndReportType(reportDateTimeId, DAILY_TWITTER_REPORT);
        if (report == null) {
            report = new Report();
            report.setReportDateTimeId(reportDateTimeId);
            report.setReportType(DAILY_TWITTER_REPORT);
        }
        report.setContent(reportContent);
        report = reportRepository.save(report);

    }
    private void addPlannedTweetCount(JsonObject reportJsonObject, Date startDateTime, Date endDateTime) {
        int totalPlannedTweets = plannedTweetRepository.getPlannedTweetCount(startDateTime, endDateTime);
        reportJsonObject.addProperty("totalPlannedTweets", totalPlannedTweets);
    }

    private void addEachPlannedTweetDetail(JsonObject reportJsonObject, Date startDateTime, Date endDateTime) {
        List<PlannedTweet> plannedTweets = plannedTweetRepository.getPlannedTweets(startDateTime, endDateTime);
        JsonArray plannedTweetJsonArray = new JsonArray();
        for (PlannedTweet onePlannedTweet : plannedTweets) {
            JsonObject onePlannedJsonObject = new JsonObject();
            onePlannedJsonObject.addProperty("tweetId", onePlannedTweet.getTweetId());
            onePlannedJsonObject.addProperty("totalRequired", onePlannedTweet.getTotalRequired());
            onePlannedJsonObject.addProperty("message", onePlannedTweet.getMessage());
            onePlannedJsonObject.addProperty("screenName", onePlannedTweet.getTwitterAccount().getScreenName());
            onePlannedJsonObject.addProperty("imageUrl", onePlannedTweet.getTwitterAccount().getImageUrl());
            onePlannedJsonObject.addProperty("twitterUserId", onePlannedTweet.getTwitterAccount().getTwitterId());
            onePlannedJsonObject.addProperty("tweetType", onePlannedTweet.getTweetType());
            onePlannedJsonObject.addProperty("tweetPostingTime", onePlannedTweet.getPostingTime().toString());
            onePlannedJsonObject.addProperty("status", onePlannedTweet.getStatus().toString());
            if (onePlannedTweet.getTotalRetweets() == null) {
                onePlannedJsonObject.addProperty("totalRetweets", 0);
            } else {
                onePlannedJsonObject.addProperty("totalRetweets", onePlannedTweet.getTotalRetweets());
            }

            List<Tweet> tweets = tweetRepository.getTweetsByPlannedTweetId(onePlannedTweet.getId());
            onePlannedJsonObject.addProperty("totalScheduled", tweets.size());
            int totalSuccess = 0;
            int totalFail = 0;
            int totalReach = 0;
            int totalAuto = 0;
            int totalManual = 0;
            int totalAutoReach = 0;
            int totalManualReach = 0;
            for(Tweet oneTweet : tweets){
                if(oneTweet.getStatus().equalsIgnoreCase("Done")){
                    totalSuccess++;
                } else {
                    totalFail++;
                }
                if (oneTweet.isAutoRetweeted()) {
                    totalAuto++;
                    totalAutoReach = totalAutoReach + oneTweet.getTwitterAccount().getFollowerCount();
                } else {
                    totalManual++;
                    totalManualReach = totalManualReach + oneTweet.getTwitterAccount().getFollowerCount();
                }
                if (oneTweet.getTwitterAccount().getFollowerCount() != null) {
                    totalReach = totalReach + oneTweet.getTwitterAccount().getFollowerCount();
                }
            }
            onePlannedJsonObject.addProperty("totalSuccess", totalSuccess);
            onePlannedJsonObject.addProperty("totalFail", totalFail);
            onePlannedJsonObject.addProperty("totalReach", totalReach);
            onePlannedJsonObject.addProperty("totalAuto", totalAuto);
            onePlannedJsonObject.addProperty("totalManual", totalManual);
            onePlannedJsonObject.addProperty("totalAutoReach", totalAutoReach);
            onePlannedJsonObject.addProperty("totalManualReach", totalManualReach);

            plannedTweetJsonArray.add(onePlannedJsonObject);
        }
        reportJsonObject.add("plannedTweets", plannedTweetJsonArray);
    }

    @Override
    public String genrateDailyTwitterReportEmail(Date date) throws AppException {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String reportDateTimeId = dailyDateFormat.get().format(calendar.getTime());
            Report currentReport = reportRepository.getReportByReportDateTimeIdAndReportType(reportDateTimeId, DAILY_TWITTER_REPORT);
            calendar.add(Calendar.DATE, -1);
            String reportPreviousDateTimeId = dailyDateFormat.get().format(calendar.getTime());
            Report previousDayReport = reportRepository.getReportByReportDateTimeIdAndReportType(reportPreviousDateTimeId, DAILY_TWITTER_REPORT);

            JsonObject jsonContext = new JsonObject();
            JsonParser jsonParser = new JsonParser();
            if (currentReport == null) {
                currentReport = previousDayReport;
            }
            if(currentReport != null){
            	jsonContext.add("current", jsonParser.parse(currentReport.getContent()).getAsJsonObject());
            }
            if(previousDayReport != null){
            	jsonContext.add("previous", jsonParser.parse(previousDayReport.getContent()).getAsJsonObject());
            }
            
            EmailTemplate emailTemplate = emailTemplateRepository.getEmailTemplateByName("TWITTER_REPORT_EMAIL");

            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(emailTemplate.getContent());

            JsonNode rootNode = convertDataToJackSon(jsonContext);
            Context context = Context.newBuilder(rootNode).resolver(JsonNodeValueResolver.INSTANCE).build();

            String result = template.apply(context);
            return result;
        } catch (Exception ex) {
            throw new AppException(ex);
        }
        


    }

    private ObjectMapper mapper = new ObjectMapper();

    private JsonNode convertDataToJackSon(JsonObject jsonObject) throws JsonProcessingException, IOException {
        JsonNode rootNode = mapper.readTree(jsonObject.toString());
        return rootNode;
    }

}
