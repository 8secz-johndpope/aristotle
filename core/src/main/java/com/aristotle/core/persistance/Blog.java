package com.aristotle.core.persistance;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.aristotle.core.enums.ContentStatus;


@Entity
@Table(name = "blogs")
public class Blog extends BaseEntity {

	@Column(name = "title")
	private String title; // Title of the news/post item
	@Column(name = "content",  columnDefinition="LONGTEXT")
	private String content;//content of news which can be html or plain text
	@Column(name = "image_url")
	private String imageUrl;// image preview url for this news item
	@Column(name = "web_url", length=128)
	private String webUrl;// Web url link for this news, which will be shared by share intent
	@Column(name = "original_url", length=1024)
	private String originalUrl;// Web url link for this news, which will be shared by share intent
	@Column(name = "source")
	private String source;// source of the new like, AamAadmiParty.org or IndianExponent.com or aapkiawaz.com etc
	@Column(name = "author")
	private String author;// nullable, name of the person who wrote this article
	@Column(name = "publish_date")
	private Date publishDate;//Publish date of this item
	@Column(name = "global_allowed")
	private boolean global;//Whether this News is available global or not
	@Column(name = "rejection_reason")
	private String rejectionReason;
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "blog_tweets",
	joinColumns = {
	@JoinColumn(name="blog_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="content_tweet_id")
	})
	private List<ContentTweet> tweets;//all one liners attached to this news which can be tweeted
	
	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "blog_location",
    joinColumns = {
    @JoinColumn(name="blog_id") 
    },
    inverseJoinColumns = {
    @JoinColumn(name = "location_id")
    })
    private Set<Location> locations;
	
	@Column(name = "content_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ContentStatus contentStatus;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public List<ContentTweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<ContentTweet> tweets) {
        this.tweets = tweets;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
    }

    @Override
    public String toString() {
        return "Blog [title=" + title + ", content=" + content + ", imageUrl=" + imageUrl + ", webUrl=" + webUrl + ", originalUrl=" + originalUrl + ", source=" + source + ", author=" + author
                + ", publishDate=" + publishDate + ", global=" + global + ", rejectionReason=" + rejectionReason + ", contentStatus=" + contentStatus + "]";
    }
	
	

}
