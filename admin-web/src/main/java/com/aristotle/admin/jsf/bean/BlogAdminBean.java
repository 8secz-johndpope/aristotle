package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.enums.ContentStatus;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Blog;
import com.aristotle.core.persistance.ContentTweet;
import com.aristotle.core.service.BlogService;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "blogAdminBean", beanName = "blogAdminBean", pattern = "/admin/blog", viewId = "/admin/admin_blog.xhtml")
@URLBeanName("blogAdminBean")
public class BlogAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    @Autowired
    private BlogService blogService;

    private Blog selectedBlog;;
	
	private boolean showList = true;
    private List<ContentTweet> tweetList;
    private ContentTweet selectedTweet;
	private boolean newTweet = false;
	private boolean showTweetList = true;
	private String otherReason;
	private boolean showOtherReasonTextBox;
    private String htmlBlogContent;
	private boolean showEditor;
	
    private List<Blog> blogList;
	public BlogAdminBean(){
        super("/admin/blog", AppPermission.CREATE_NEWS, AppPermission.UPDATE_NEWS, AppPermission.DELETE_NEWS, AppPermission.APPROVE_NEWS);
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
        refreshBlogList();
	}

    private void refreshBlogList() {
        try {
            if (menuBean.isGlobalSelected()) {
                blogList = blogService.getAllGlobalBlog();
            } else {
                Set<Long> locationIds = new HashSet<Long>();
                locationIds.add(menuBean.getSelectedLocation().getId());
                blogList = blogService.getAllLocationPublishedBlog(locationIds, 0, 100);
            }

        } catch (Exception ex) {
            blogList = new ArrayList<Blog>();
            sendErrorMessageToJsfScreen(ex);
        }
		tweetList = new ArrayList<>();
	}

	public void cancelTweet(){
		showTweetList = true;
	}

    public void createBlogTweet() {
        selectedTweet = new ContentTweet();
		newTweet = true;
		showTweetList = false;
	}

    public void addBlogTweet() {
		if(newTweet){
			tweetList.add(selectedTweet);
		}
		showTweetList = true;
	}

    public Blog getSelectedBlog() {
        return selectedBlog;
	}

    public void setSelectedBlog(Blog selectedBlog) {
        this.selectedBlog = selectedBlog;
		showList = false;
        try {
            tweetList = blogService.getBlogContentTweets(selectedBlog.getId());
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
	}
	public boolean isSaveDraft(){
        return menuBean.isEditBlogAllowed();
	}
	public boolean isSaveAndPublish(){
        if (selectedBlog.getContentStatus() == ContentStatus.Rejected) {
			return false;
		}
        return menuBean.isPublishBlogAllowed();
	}
	public boolean isPublish(){
        if (selectedBlog.getContentStatus() == ContentStatus.Rejected) {
			return false;
		}
        return menuBean.isPublishBlogAllowed();
	}
	public boolean isReject(){
        if (selectedBlog == null || selectedBlog.getId() == null || selectedBlog.getId() <= 0 || selectedBlog.getContentStatus() == ContentStatus.Rejected) {
			return false;
		}
        return menuBean.isPublishBlogAllowed();
	}
	public boolean isEditAllowed(){
        return menuBean.isEditBlogAllowed();
	}
	public void editRawHtml() {
        htmlBlogContent = selectedBlog.getContent();
		showEditor = false;
	}
	public void editText() {
        selectedBlog.setContent(htmlBlogContent);
		showEditor = true;
	}
	public void saveAndPublishPost() {
		savePost();
		publishPost();
	}
	public void onRejectionReasonChange(){
        if (selectedBlog == null) {
			showOtherReasonTextBox = false;
		}else{
            if (selectedBlog.getRejectionReason() != null && "Others".equalsIgnoreCase(selectedBlog.getRejectionReason())) {
				showOtherReasonTextBox = true;
			}else{
				showOtherReasonTextBox = false;
			}
		}
	}
	
	public void publishPost(){
		try{
            if (selectedBlog == null) {
                sendErrorMessageToJsfScreen("No blog selected to publish");
			}
            if (selectedBlog.getId() == null || selectedBlog.getId() <= 0) {
                sendErrorMessageToJsfScreen("Please save the Blog first");
			}
			if(!isPublish()){
                sendErrorMessageToJsfScreen("You do not have permission to publish a blog");
			}
			if(isValidInput()){
                selectedBlog = blogService.publishBlog(selectedBlog.getId());
                sendInfoMessageToJsfScreen("Blog Published Succesfully");
                refreshBlogList();
			}
			
		}catch(Exception ex){
			sendErrorMessageToJsfScreen("Unable to save Post",ex);
		}
	}
	public void rejectPost(){
		try{
            if (selectedBlog == null) {
                sendErrorMessageToJsfScreen("No blog selected to Reject");
			}
            if (selectedBlog.getId() == null || selectedBlog.getId() <= 0) {
                sendErrorMessageToJsfScreen("Please save the Blog first");
			}
			if(!isPublish()){
                sendErrorMessageToJsfScreen("You do not have permission to reject a blog");
			}
			if(isValidInput()){
                String rejectionReason = selectedBlog.getRejectionReason();
				if("others".equalsIgnoreCase(rejectionReason)){
					rejectionReason = rejectionReason +" - " + otherReason;
				}
                selectedBlog = blogService.rejectBlog(selectedBlog.getId(), rejectionReason);
                sendInfoMessageToJsfScreen("Blog Rejected Succesfully");
                refreshBlogList();
			}
			
		}catch(Exception ex){
			sendErrorMessageToJsfScreen("Unable to save Post",ex);
		}
	}
	public void savePost(){
		try{
            if (StringUtils.isEmpty(selectedBlog.getContent())) {
                sendErrorMessageToJsfScreen("Please enter Blog Content");
			}
            if (StringUtils.isEmpty(selectedBlog.getTitle())) {
                sendErrorMessageToJsfScreen("Please enter Blog Title");
			}

			if(isValidInput()){
                selectedBlog.setGlobal(menuBean.isGlobalSelected());
                Long locationId = null;
                if (menuBean.getSelectedLocation() != null) {
                    locationId = menuBean.getSelectedLocation().getId();
                }
                selectedBlog = blogService.saveBlog(selectedBlog, tweetList, locationId);
                sendInfoMessageToJsfScreen("Blog saved succesfully");
                refreshBlogList();
				showList = true;
			}
				
		}catch(Exception ex){
			sendErrorMessageToJsfScreen("Unable to save Post",ex);
		}
		
	}

    public void createBlog() {
        selectedBlog = new Blog();
        selectedBlog.setContentStatus(ContentStatus.Pending);
		showList = false;
		tweetList = new ArrayList<>();
	}
	public void cancel(){
        createBlog();
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

    public List<Blog> getBlogList() {
        return blogList;
	}

    public void setBlogList(List<Blog> blogList) {
        this.blogList = blogList;
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

    public String getHtmlBlogContent() {
        return htmlBlogContent;
	}

    public void setHtmlBlogContent(String htmlBlogContent) {
        this.htmlBlogContent = htmlBlogContent;
	}
	public boolean isShowEditor() {
		return showEditor;
	}
	public void setShowEditor(boolean showEditor) {
		this.showEditor = showEditor;
	}


}
