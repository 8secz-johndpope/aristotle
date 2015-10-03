package com.aristotle.admin.jsf.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.persistance.DomainPageTemplate;
import com.aristotle.core.persistance.DomainTemplate;
import com.aristotle.core.persistance.UrlMapping;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "templateAdminBean", beanName = "templateAdminBean", pattern = "/admin/templates", viewId = "/admin/admin_template.xhtml")
@URLBeanName("templateAdminBean")
public class TemplateAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    private List<DomainTemplate> templates;
    private DomainTemplate selectedTemplate;
	
    private boolean showTemplateList = true;
    private DomainPageTemplate selectedTemplateUrl;
    private String selectedUrl;
    private Map<String, String> urls;
    private Map<String, UrlMapping> urlToUrlMapping;
    private String draftUrl;
    
    @Value("${aws_access_key}")
    private String awsKey;
    @Value("${aws_access_secret}")
    private String awsSecret;

    @Value("${static_data_env:dev}")
    private String staticDataEnv;
	
	public TemplateAdminBean(){
        super("/admin/templates", AppPermission.WEB_ADMIN, AppPermission.WEB_ADMIN_DRAFT);
	}
	//@URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback=false)
	public void init() throws Exception {
        System.out.println("Init");
		if(!checkUserAccess()){
			return;
		}
		refreshTemplateList();
        draftUrl = null;
        urls = new HashMap<String, String>();
        urlToUrlMapping = new HashMap<String, UrlMapping>();
        try {
            List<UrlMapping> urlMappings = dataPluginService.getAllUrlMappings();
            for (UrlMapping oneUrlMapping : urlMappings) {
                System.out.println("oneUrlMapping.getUrlPattern()=" + oneUrlMapping.getUrlPattern());
                if (oneUrlMapping.getUrlPattern().startsWith("/api")) {
                    continue;
                }
                urls.put(oneUrlMapping.getUrlPattern(), oneUrlMapping.getUrlPattern());
                urlToUrlMapping.put(oneUrlMapping.getUrlPattern(), oneUrlMapping);
            }
        } catch (Exception ex) {
            sendErrorMessageToJsfScreen(ex);
        }

	}
	private void refreshTemplateList(){
        try {
            if(menuBean.isGlobalSelected()){
                System.out.println("Get Global Templates");
                templates = dataPluginService.getAllDomainTemplates(null);
            } else {
                templates = dataPluginService.getAllDomainTemplates(menuBean.getSelectedLocation().getId());
            }
            System.out.println("templates=" + templates);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    public void handleUrlSelection() {
        if (selectedUrl == null || selectedUrl.equals("")) {
            draftUrl = null;
        }
        selectedTemplateUrl = null;
        if (selectedTemplate.getDomainPageTemplates() != null) {
            for (DomainPageTemplate oneTemplateUrlDto : selectedTemplate.getDomainPageTemplates()) {
                logger.info("selectedUrl={}", selectedUrl);
                if (oneTemplateUrlDto.getUrlMapping().getUrlPattern().equals(selectedUrl)) {
                    selectedTemplateUrl = oneTemplateUrlDto;
                    break;
                }
            }
        }
        if (selectedTemplateUrl == null) {
            logger.info("No Existing Page Template Found ={}", selectedUrl);
            selectedTemplateUrl = new DomainPageTemplate();
            selectedTemplateUrl.setUrlMapping(urlToUrlMapping.get(selectedUrl));
        }
        draftUrl = "http://www.swarajabhiyan.org/index.html?draft=1";
    }

    private String getSubDirectory(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".css")) {
            return "css";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif")) {
            return "images";
        }
        if (fileName.endsWith(".js")) {
            return "js";
        }
        if (fileName.endsWith(".eot") || fileName.endsWith(".woff2") || fileName.endsWith(".otf") || fileName.endsWith(".woff") || fileName.endsWith(".ttf") || fileName.endsWith(".svg")) {
            return "fonts";
        }

        return "others";

    }
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Uploading File");
        String subdDirectory = getSubDirectory(event.getFile().getFileName());
        System.out.println("subdDirectory = " + subdDirectory);
        String remoteFileName = "templates/" + staticDataEnv + "/" + selectedTemplate.getId() + "/" + subdDirectory + "/" + event.getFile().getFileName();
        String bucketName = "static.swarajabhiyan.org";
        System.out.println("remoteFileName = " + remoteFileName);
        try {
            awsFileManager.uploadFileToS3(awsKey, awsSecret, bucketName, remoteFileName, event.getFile().getInputstream());
            System.out.println("File uploaded = " + remoteFileName);
            dataPluginService.saveDomainTemplateFile(selectedTemplate.getId(), remoteFileName, event.getFile().getSize());
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    public void createTemplate() {
        selectedTemplate = new DomainTemplate();
        showTemplateList = false;
        selectedTemplateUrl = new DomainPageTemplate();
        selectedUrl = null;
    }

    public void cancel() {
        showTemplateList = true;
    }

    public void saveTemplate() {
        if (StringUtils.isEmpty(selectedTemplate.getName())) {
            sendErrorMessageToJsfScreen("Please enter Name for this template");
        }
        if (isValidInput()) {
            try {
                selectedTemplate = dataPluginService.saveDomainTemplate(selectedTemplate);
                if (selectedTemplateUrl != null) {
                    selectedTemplateUrl.setDomainTemplate(selectedTemplate);
                    selectedTemplateUrl.setDomainTemplateId(selectedTemplate.getId());
                    selectedTemplateUrl = dataPluginService.saveDomainPageTemplate(selectedTemplateUrl);
                }
                // templateCache.refreshCache();
                refreshTemplateList();
                // cancel();
            } catch (Exception e) {
                sendErrorMessageToJsfScreen(e);
            }
        }
    }

    public void makeItLive() {
        if (StringUtils.isEmpty(selectedTemplate.getName())) {
            sendErrorMessageToJsfScreen("Please enter Name for this template");
        }
        if (isValidInput()) {
            try {
                selectedTemplate = dataPluginService.saveDomainTemplate(selectedTemplate);
                if (selectedTemplateUrl != null) {
                    selectedTemplateUrl.setDomainTemplate(selectedTemplate);
                    selectedTemplateUrl.setDomainTemplateId(selectedTemplate.getId());
                    selectedTemplateUrl.setHtmlContent(selectedTemplateUrl.getHtmlContentDraft());
                    selectedTemplateUrl = dataPluginService.saveDomainPageTemplate(selectedTemplateUrl);
                }
                // templateCache.refreshCache();
                refreshTemplateList();
                // cancel();
            } catch (Exception e) {
                sendErrorMessageToJsfScreen(e);
            }
        }
    }


    public String getSelectedUrl() {
        return selectedUrl;
    }

    public void setSelectedUrl(String selectedUrl) {
        this.selectedUrl = selectedUrl;
    }

    public Map<String, String> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

    public String getDraftUrl() {
        return draftUrl;
    }

    public void setDraftUrl(String draftUrl) {
        this.draftUrl = draftUrl;
    }

    public List<DomainTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<DomainTemplate> templates) {
        this.templates = templates;
    }

    public DomainTemplate getSelectedTemplate() {
        return selectedTemplate;
    }

    public void setSelectedTemplate(DomainTemplate selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
        showTemplateList = false;
    }

    public boolean isShowTemplateList() {
        return showTemplateList;
    }

    public void setShowTemplateList(boolean showTemplateList) {
        this.showTemplateList = showTemplateList;
    }

    public DomainPageTemplate getSelectedTemplateUrl() {
        return selectedTemplateUrl;
    }

    public void setSelectedTemplateUrl(DomainPageTemplate selectedTemplateUrl) {
        this.selectedTemplateUrl = selectedTemplateUrl;
    }

    public String getStaticDataEnv() {
        return staticDataEnv;
    }

    public void setStaticDataEnv(String staticDataEnv) {
        this.staticDataEnv = staticDataEnv;
    }

    public boolean isCopyToProdAvailable() {
        if (selectedTemplateUrl == null) {
            return false;
        }
        if (selectedTemplateUrl.getHtmlContent() == null && selectedTemplateUrl.getHtmlContentDraft() == null) {
            return false;
        }
        return ((selectedTemplateUrl.getHtmlContent() == null && selectedTemplateUrl.getHtmlContentDraft() != null) || !selectedTemplateUrl.getHtmlContent().equals(
                selectedTemplateUrl.getHtmlContentDraft()));
    }


}
