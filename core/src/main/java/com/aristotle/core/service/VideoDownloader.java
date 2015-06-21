package com.aristotle.core.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aristotle.core.enums.ContentStatus;
import com.aristotle.core.persistance.Video;
import com.aristotle.core.persistance.repo.VideoRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
@Transactional
public class VideoDownloader{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private VideoRepository videoRepository;
	
    @Value("${google_api_key}")
    private String googleApiKey;

    @Autowired
    private HttpUtil httpUtil;
	

	@PostConstruct
	public void init(){
	}
	// http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
	// http://freshgroundjava.blogspot.in/2012/07/spring-scheduled-tasks-cron-expression.html
	// ss mm hh dd
	public boolean refreshVideoList() {
        logger.info("Downloading Videos");
        String channelIds[] = { "UCYm-AJyEXXAWOrw_qp0XQDw" };
		boolean newVideos = false;
		try{
			
			for(String oneChannel:channelIds){
				logger.info("oneChannel = "+oneChannel);
                newVideos = newVideos || downloadVideosOfChannelNew(oneChannel);
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return newVideos;
	}

    private boolean downloadVideosOfChannelNew(String channelId) throws Exception{
        int maxResult = 50;
        int startIndex = 1;
        String nextPageToken = null;
        JsonParser jsonParser = new JsonParser();
        while(true){

            logger.info("StartIndex = "+startIndex+", MaxResults="+maxResult);
            String feedUrl = "http://gdata.youtube.com/feeds/api/users/"+channelId+"/uploads?start-index="+startIndex+"&max-results="+maxResult;
            feedUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=" + channelId + "&maxResults=" + maxResult + "&key=" + googleApiKey;
            if (nextPageToken != null) {
                feedUrl = feedUrl + "&pageToken=" + nextPageToken;
            }
            System.out.println("feedUrl = " + feedUrl);
            String response = httpUtil.getResponse(feedUrl);
            System.out.println("response = " + response);
            JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
            int totalResults = jsonObject.get("pageInfo").getAsJsonObject().get("totalResults").getAsInt();
            JsonArray itemJsonArray = jsonObject.get("items").getAsJsonArray();
            downloadAndSaveVideoNew(itemJsonArray, channelId);
            if(totalResults < maxResult){
                break;
            }
            nextPageToken = jsonObject.get("nextPageToken").getAsString();
        }
        return true;
    }

    public boolean downloadAndSaveVideoNew(JsonArray allVideos, String channelId) {
        boolean newVideoAvailable = false;
        Video videoItem;
        Video existingVideo;
        for (int i = allVideos.size() - 1; i >= 0; i--) {
            JsonObject videoEntry = allVideos.get(i).getAsJsonObject();
            // printVideoEntry(videoEntry, detailed, count);
            String videoId = videoEntry.get("id").getAsJsonObject().get("videoId").getAsString();
            existingVideo = videoRepository.getVideoByYoutubeVideoId(videoId);
            videoItem = new Video();
            if (existingVideo != null) {
                videoItem.setId(existingVideo.getId());
                continue;
            } else {
                newVideoAvailable = true;
            }
            Date date = new Date(videoEntry.get("snippet").getAsJsonObject().get("publishedAt").getAsString());
            videoItem.setPublishDate(date);
            videoItem.setContentStatus(ContentStatus.Published);
            videoItem.setDescription(videoEntry.get("snippet").getAsJsonObject().get("description").getAsString());
            videoItem.setGlobal(true);
            videoItem.setImageUrl("http://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg");
            videoItem.setYoutubeVideoId(videoId);
            videoItem.setTitle(videoEntry.get("snippet").getAsJsonObject().get("title").getAsString());
            videoItem.setChannelId(channelId);
            videoItem.setWebUrl("http://www.youtube.com/watch?v=" + videoId);
            logger.info("Saving Youtube Video : " + videoItem.getTitle() + ", Video Id = " + videoItem.getYoutubeVideoId());
            videoItem = videoRepository.save(videoItem);
        }
        return newVideoAvailable;
    }

}
