package com.aristotle.core.persistance;

import java.util.Date;
import java.util.List;

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
@Table(name="news")
public class News extends BaseEntity {

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
	@Column(name = "content_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ContentStatus contentStatus;
	@Column(name = "rejection_reason")
	private String rejectionReason;// 

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_tweets",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="content_tweet_id")
	})
	private List<ContentTweet> tweets;//all one liners attached to this news which can be tweeted
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_ac",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="ac_id")
	})
	private List<AssemblyConstituency> assemblyConstituencies;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_pc",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="pc_id")
	})
	private List<ParliamentConstituency> parliamentConstituencies;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_district",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="district_id")
	})
	private List<District> districts;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_state",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="state_id")
	})
	private List<State> states;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_country",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="country_id")
	})
	private List<Country> countries;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_country_region",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="country_region_id")
	})
	private List<CountryRegion> countryRegions;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "news_country_region_area",
	joinColumns = {
	@JoinColumn(name="news_id") 
	},
	inverseJoinColumns = {
	@JoinColumn(name="country_region_area_id")
	})
	private List<CountryRegionArea> countryRegionsAreas;
	

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
	public List<ContentTweet> getTweets() {
		return tweets;
	}
	public void setTweets(List<ContentTweet> tweets) {
		this.tweets = tweets;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public List<AssemblyConstituency> getAssemblyConstituencies() {
		return assemblyConstituencies;
	}
	public void setAssemblyConstituencies(
			List<AssemblyConstituency> assemblyConstituencies) {
		this.assemblyConstituencies = assemblyConstituencies;
	}
	public List<ParliamentConstituency> getParliamentConstituencies() {
		return parliamentConstituencies;
	}
	public void setParliamentConstituencies(
			List<ParliamentConstituency> parliamentConstituencies) {
		this.parliamentConstituencies = parliamentConstituencies;
	}
	public List<District> getDistricts() {
		return districts;
	}
	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}
	public List<State> getStates() {
		return states;
	}
	public void setStates(List<State> states) {
		this.states = states;
	}
	public boolean isGlobal() {
		return global;
	}
	public void setGlobal(boolean global) {
		this.global = global;
	}
	public ContentStatus getContentStatus() {
		return contentStatus;
	}
	public void setContentStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus;
	}
	public List<Country> getCountries() {
		return countries;
	}
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}
	public List<CountryRegion> getCountryRegions() {
		return countryRegions;
	}
	public void setCountryRegions(List<CountryRegion> countryRegions) {
		this.countryRegions = countryRegions;
	}
	public List<CountryRegionArea> getCountryRegionsAreas() {
		return countryRegionsAreas;
	}
	public void setCountryRegionsAreas(List<CountryRegionArea> countryRegionsAreas) {
		this.countryRegionsAreas = countryRegionsAreas;
	}
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
}
