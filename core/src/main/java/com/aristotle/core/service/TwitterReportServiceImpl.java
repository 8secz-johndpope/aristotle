package com.aristotle.core.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.PlannedTweet;
import com.aristotle.core.persistance.Report;
import com.aristotle.core.persistance.Tweet;
import com.aristotle.core.persistance.repo.PlannedTweetRepository;
import com.aristotle.core.persistance.repo.ReportRepository;
import com.aristotle.core.persistance.repo.TweetRepository;
import com.aristotle.core.persistance.repo.TwitterAccountRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

            List<Tweet> tweets = tweetRepository.getTweetsByPlannedTweetId(onePlannedTweet.getId());
            onePlannedJsonObject.addProperty("totalScheduled", tweets.size());
            int totalSuccess = 0;
            int totalFail = 0;
            int totalReach = 0;
            for(Tweet oneTweet : tweets){
                if(oneTweet.getStatus().equalsIgnoreCase("Done")){
                    totalSuccess++;
                } else {
                    totalFail++;
                }
                if (oneTweet.getTwitterAccount().getFollowerCount() != null) {
                    totalReach = totalReach + oneTweet.getTwitterAccount().getFollowerCount();
                }
            }
            onePlannedJsonObject.addProperty("totalSuccess", totalSuccess);
            onePlannedJsonObject.addProperty("totalFail", totalFail);
            onePlannedJsonObject.addProperty("totalReach", totalReach);
            plannedTweetJsonArray.add(onePlannedJsonObject);
        }
        reportJsonObject.add("plannedTweets", plannedTweetJsonArray);
    }

}
