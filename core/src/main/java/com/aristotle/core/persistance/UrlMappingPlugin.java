package com.aristotle.core.persistance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "url_mapping_plugin")
public class UrlMappingPlugin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	@Column(name = "ver")
	private int ver;
	@Column(name = "date_created")
	private Date dateCreated;
	@Column(name = "date_modified")
	private Date dateModified;
	@Column(name = "creator_id")
	private Long creatorId;
	@Column(name = "modifier_id")
	private Long modifierId;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "url_mapping_id")
    private UrlMapping urlMapping;
    @Column(name = "url_mapping_id", insertable = false, updatable = false)
    private Long urlMappingId;

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "data_plugin_id")
    private DataPlugin dataPlugin;
    @Column(name = "data_plugin_id", insertable = false, updatable = false)
    private Long dataPluginId;
	
    @Column(name = "setting", columnDefinition = "LONGTEXT")
    private String setting;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }

    public UrlMapping getUrlMapping() {
        return urlMapping;
    }

    public void setUrlMapping(UrlMapping urlMapping) {
        this.urlMapping = urlMapping;
    }

    public Long getUrlMappingId() {
        return urlMappingId;
    }

    public void setUrlMappingId(Long urlMappingId) {
        this.urlMappingId = urlMappingId;
    }

    public DataPlugin getDataPlugin() {
        return dataPlugin;
    }

    public void setDataPlugin(DataPlugin dataPlugin) {
        this.dataPlugin = dataPlugin;
    }

    public Long getDataPluginId() {
        return dataPluginId;
    }

    public void setDataPluginId(Long dataPluginId) {
        this.dataPluginId = dataPluginId;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }
	
}
