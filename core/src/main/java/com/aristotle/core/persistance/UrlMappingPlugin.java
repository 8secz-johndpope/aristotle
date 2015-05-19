package com.aristotle.core.persistance;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "url_mapping_plugin")
public class UrlMappingPlugin extends BaseEntity {

	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name = "url_mapping_id")
    private UrlMapping urlMapping;
    @Column(name = "url_mapping_id", insertable = false, updatable = false)
    private Long urlMappingId;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "data_plugin_id")
    private DataPlugin dataPlugin;
    @Column(name = "data_plugin_id", insertable = false, updatable = false)
    private Long dataPluginId;
	
    @Column(name = "setting", columnDefinition = "LONGTEXT")
    private String setting;

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
