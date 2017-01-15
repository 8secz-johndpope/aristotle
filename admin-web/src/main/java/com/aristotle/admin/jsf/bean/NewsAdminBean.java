package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.enums.ContentStatus;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.ContentTweet;
import com.aristotle.core.persistance.News;
import com.aristotle.core.persistance.UploadedFile;
import com.aristotle.core.service.NewsService;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "newsAdminBean", beanName = "newsAdminBean", pattern = "/admin/news", viewId = "/admin/admin_news.xhtml")
@URLBeanName("newsAdminBean")
public class NewsAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    @Autowired
    private NewsService newsService;

    private News selectedNews;;
	
	private boolean showList = true;
    private List<ContentTweet> tweetList;
    private List<UploadedFile> newsFilesList;
    private ContentTweet selectedTweet;
	private boolean newTweet = false;
	private boolean showTweetList = true;
	private String otherReason;
	private boolean showOtherReasonTextBox;
	private String htmlNewsContent;
	private boolean showEditor;

    @Value("${static_data_env:dev}")
    private String staticDataEnv;

    @Value("${aws_access_key}")
    private String awsKey;
    @Value("${aws_access_secret}")
    private String awsSecret;

    private List<News> newsList;
	public NewsAdminBean(){
		super("/admin/news", AppPermission.CREATE_NEWS,AppPermission.UPDATE_NEWS, AppPermission.DELETE_NEWS, AppPermission.APPROVE_NEWS);
		showEditor = true;
        selectedTweet = new ContentTweet();
	}
	//@URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback=false)
	public void init() throws Exception {
        System.out.println("Checking USer Access");
		if(!checkUserAccess()){
			return;
		}
		refreshNewsList();
	}
	private void refreshNewsList(){
        try {
            if (menuBean.isGlobalSelected()) {
                newsList = newsService.getAllGlobalNews();
            } else {
                Set<Long> locationIds = new HashSet<Long>();
                locationIds.add(menuBean.getSelectedLocation().getId());
                newsList = newsService.getAllLocationPublishedNews(locationIds, 0, 100);
            }

        } catch (Exception ex) {
            newsList = new ArrayList<News>();
            sendErrorMessageToJsfScreen(ex);
        }
		tweetList = new ArrayList<>();
	}

	public void cancelTweet(){
		showTweetList = true;
	}
	public void createNewsTweet(){
        selectedTweet = new ContentTweet();
		newTweet = true;
		showTweetList = false;
	}
	public void addNewsTweet(){
		if(newTweet){
			tweetList.add(selectedTweet);
		}
		showTweetList = true;
	}

    public News getSelectedNews() {
		return selectedNews;
	}

    public void setSelectedNews(News selectedNews) {
		this.selectedNews = selectedNews;
		showList = false;
        try {
            tweetList = newsService.getNewsContentTweets(selectedNews.getId());
            newsFilesList = newsService.getNewsUploadedFiles(selectedNews.getId());
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
	}

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    public void handleFileUpload(FileUploadEvent event) {
        logger.info("Uploading File");
        String remoteFileName = "content/news/" + staticDataEnv + "/" + selectedNews.getId() + "/" + event.getFile().getFileName();
        String bucketName = "static.swarajabhiyan.org";
        logger.info("remoteFileName = " + remoteFileName);
        try {
            awsFileManager.uploadFileToS3(awsKey, awsSecret, bucketName, remoteFileName, event.getFile().getInputstream());
            logger.info("File uploaded = " + remoteFileName);
            newsService.saveNewsUploadedFile(selectedNews.getId(), remoteFileName, event.getFile().getSize(), getFileType(event.getFile().getFileName()));
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            newsFilesList = newsService.getNewsUploadedFiles(selectedNews.getId());
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
	public boolean isSaveDraft(){
        return menuBean.isEditNewsAllowed();
	}
	public boolean isSaveAndPublish(){
		if(selectedNews.getContentStatus() == ContentStatus.Rejected){
			return false;
		}
        return menuBean.isPublishNewsAllowed();
	}
	public boolean isPublish(){
		if(selectedNews.getContentStatus() == ContentStatus.Rejected){
			return false;
		}
        return menuBean.isPublishNewsAllowed();
	}
	public boolean isReject(){
		if(selectedNews == null || selectedNews.getId() == null || selectedNews.getId() <= 0 || selectedNews.getContentStatus() == ContentStatus.Rejected){
			return false;
		}
        return menuBean.isPublishNewsAllowed();
	}
	public boolean isEditAllowed(){
        return menuBean.isEditNewsAllowed();
	}
	public void editRawHtml() {
		htmlNewsContent = selectedNews.getContent();
		showEditor = false;
	}
	public void editText() {
		selectedNews.setContent(htmlNewsContent);
		showEditor = true;
	}
	public void saveAndPublishPost() {
		savePost();
		publishPost();
	}
	public void onRejectionReasonChange(){
		if(selectedNews == null){
			showOtherReasonTextBox = false;
		}else{
			if(selectedNews.getRejectionReason() != null && "Others".equalsIgnoreCase(selectedNews.getRejectionReason())){
				showOtherReasonTextBox = true;
			}else{
				showOtherReasonTextBox = false;
			}
		}
	}
	
	public void publishPost(){
		try{
			if(selectedNews == null){
				sendErrorMessageToJsfScreen("No news selected to publish");
			}
			if(selectedNews.getId() == null || selectedNews.getId() <= 0){
				sendErrorMessageToJsfScreen("Please save the News first");
			}
			if(!isPublish()){
				sendErrorMessageToJsfScreen("You do not have permission to publish a news");
			}
			if(isValidInput()){
                selectedNews = newsService.publishNews(selectedNews.getId());
				sendInfoMessageToJsfScreen("News Published Succesfully");
				refreshNewsList();
			}
			
		}catch(Exception ex){
			sendErrorMessageToJsfScreen("Unable to save Post",ex);
		}
	}
	public void rejectPost(){
		try{
			if(selectedNews == null){
				sendErrorMessageToJsfScreen("No news selected to Reject");
			}
			if(selectedNews.getId() == null || selectedNews.getId() <= 0){
				sendErrorMessageToJsfScreen("Please save the News first");
			}
			if(!isPublish()){
				sendErrorMessageToJsfScreen("You do not have permission to reject a news");
			}
			if(isValidInput()){
				String rejectionReason = selectedNews.getRejectionReason();
				if("others".equalsIgnoreCase(rejectionReason)){
					rejectionReason = rejectionReason +" - " + otherReason;
				}
                selectedNews = newsService.rejectNews(selectedNews.getId(), rejectionReason);
				sendInfoMessageToJsfScreen("News Rejected Succesfully");
				refreshNewsList();
			}
			
		}catch(Exception ex){
			sendErrorMessageToJsfScreen("Unable to save Post",ex);
		}
	}
	public void savePost(){
		try{
            if (StringUtils.isEmpty(selectedNews.getContent())) {
				sendErrorMessageToJsfScreen("Please enter News Content");
			}
            if (StringUtils.isEmpty(selectedNews.getTitle())) {
				sendErrorMessageToJsfScreen("Please enter News Title");
			}

			if(isValidInput()){
                selectedNews.setGlobal(menuBean.isGlobalSelected());
                Long locationId = null;
                if (menuBean.getSelectedLocation() != null) {
                    locationId = menuBean.getSelectedLocation().getId();
                }
                selectedNews = newsService.saveNews(selectedNews, tweetList, locationId);
				sendInfoMessageToJsfScreen("News saved succesfully");
				refreshNewsList();
				showList = true;
			}
				
		}catch(Exception ex){
			sendErrorMessageToJsfScreen("Unable to save Post",ex);
		}
		
	}
	public void createNews(){
        selectedNews = new News();
        selectedNews.setContentStatus(ContentStatus.Pending);
		showList = false;
		tweetList = new ArrayList<>();
	}
	public void cancel(){
		createNews();
		showList = true;
	}
	public void deleteTweet(){
		tweetList.remove(selectedTweet);
	}
	
	public boolean isShowList() {
		return showList;
	}
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public List<News> getNewsList() {
		return newsList;
	}

    public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}

    public List<ContentTweet> getTweetList() {
		return tweetList;
	}

    public void setTweetList(List<ContentTweet> tweetList) {
		this.tweetList = tweetList;
	}

    public ContentTweet getSelectedTweet() {
		return selectedTweet;
	}

    public void setSelectedTweet(ContentTweet selectedTweet) {
		this.selectedTweet = selectedTweet;
		newTweet = false;
		showTweetList = false;
	}
	public boolean isNewTweet() {
		return newTweet;
	}
	public void setNewTweet(boolean newTweet) {
		this.newTweet = newTweet;
	}
	public boolean isShowTweetList() {
		return showTweetList;
	}
	public void setShowTweetList(boolean showTweetList) {
		this.showTweetList = showTweetList;
	}
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	public boolean isShowOtherReasonTextBox() {
		return showOtherReasonTextBox;
	}
	public void setShowOtherReasonTextBox(boolean showOtherReasonTextBox) {
		this.showOtherReasonTextBox = showOtherReasonTextBox;
	}
	public String getHtmlNewsContent() {
		return htmlNewsContent;
	}
	public void setHtmlNewsContent(String htmlNewsContent) {
		this.htmlNewsContent = htmlNewsContent;
	}
	public boolean isShowEditor() {
		return showEditor;
	}
	public void setShowEditor(boolean showEditor) {
		this.showEditor = showEditor;
	}

    public boolean isShowFileUpload() {
        if (selectedNews == null || selectedNews.getId() == null || selectedNews.getId() <= 0) {
            return false;
        }
        return true;
    }

    public List<UploadedFile> getNewsFilesList() {
        return newsFilesList;
    }

    public void setNewsFilesList(List<UploadedFile> newsFilesList) {
        this.newsFilesList = newsFilesList;
    }


}
