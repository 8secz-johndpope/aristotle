package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.List;

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
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.CallCampaign;
import com.aristotle.core.persistance.UploadedFile;
import com.aristotle.core.service.CallCampaignService;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "callCampaignAdminBean", beanName = "callCampaignAdminBean", pattern = "/admin/call", viewId = "/admin/admin_call.xhtml")
@URLBeanName("callCampaignAdminBean")
public class CallCampaignAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    @Autowired
    private CallCampaignService callCampaignService;

    private CallCampaign selectedCallCampaign;
	
	private boolean showList = true;
    private List<UploadedFile> callCampaignFilesList;

    @Value("${static_data_env:dev}")
    private String staticDataEnv;

    @Value("${aws_access_key}")
    private String awsKey;
    @Value("${aws_access_secret}")
    private String awsSecret;

    private List<CallCampaign> callCampaignList;

	public CallCampaignAdminBean(){
        super("/admin/call", AppPermission.CALL_CAMPAIGN_ADMIN);
	}
	//@URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback=false)
	public void init() throws Exception {
        System.out.println("Checking USer Access");
		if(!checkUserAccess()){
			return;
		}
		refreshCallCampaignList();
	}
	private void refreshCallCampaignList(){
        try {
            callCampaignList = callCampaignService.getCallCampigns();
        } catch (Exception ex) {
            callCampaignList = new ArrayList<CallCampaign>();
            sendErrorMessageToJsfScreen(ex);
        }
	}

    public void updateUrl() {
        if(selectedCallCampaign.getId() == null && selectedCallCampaign.getUrl() == null && !StringUtils.isEmpty(selectedCallCampaign.getTitle())){
            char[] chars = selectedCallCampaign.getTitle().toLowerCase().toCharArray();
            StringBuilder sb = new StringBuilder();
            for(char oneChar : chars){
                if ((oneChar >= 'a' && oneChar <= 'z') || (oneChar >= '0' && oneChar <= '9')) {
                    sb.append(oneChar);
                }
                if (oneChar == ' ') {
                    sb.append('-');
                }
            }
            selectedCallCampaign.setUrl(sb.toString());
        }

    }
    public CallCampaign getSelectedCallCampaign() {
        return selectedCallCampaign;
    }

    public void setSelectedCallCampaign(CallCampaign selectedCallCampaign) {
        this.selectedCallCampaign = selectedCallCampaign;
        showList = false;
        try {
            callCampaignFilesList = callCampaignService.getCallCampaignUploadedFiles(selectedCallCampaign.getId());
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Uploading File");
        String remoteFileName = "content/callcampaign/" + staticDataEnv + "/" + selectedCallCampaign.getId() + "/" + event.getFile().getFileName();
        String bucketName = "static.swarajabhiyan.org";
        System.out.println("remoteFileName = " + remoteFileName);
        try {
            awsFileManager.uploadFileToS3(awsKey, awsSecret, bucketName, remoteFileName, event.getFile().getInputstream());
            System.out.println("File uploaded = " + remoteFileName);
            callCampaignService.saveCallCampaignUploadedFile(selectedCallCampaign.getId(), remoteFileName, event.getFile().getSize(), getFileType(event.getFile().getFileName()));
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            callCampaignFilesList = callCampaignService.getCallCampaignUploadedFiles(selectedCallCampaign.getId());
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void saveCallCampaign() {
		try{
            if (StringUtils.isEmpty(selectedCallCampaign.getTitle())) {
                sendErrorMessageToJsfScreen("Please enter Campaign Title");
			}
			if(isValidInput()){
                selectedCallCampaign = callCampaignService.saveCallCampign(selectedCallCampaign);
                sendInfoMessageToJsfScreen("Call Campaign saved succesfully");
				refreshCallCampaignList();
				showList = true;
			}
				
		}catch(Exception ex){
            sendErrorMessageToJsfScreen("Unable to save Call Campaign", ex);
		}
		
	}

    public void createCallCampaign() {
        selectedCallCampaign = new CallCampaign();
        selectedCallCampaign.setEnabled(true);
		showList = false;
        callCampaignFilesList = new ArrayList<>();
	}
	public void cancel(){
        selectedCallCampaign = new CallCampaign();
		showList = true;
	}
	public boolean isShowList() {
		return showList;
	}
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public boolean isShowFileUpload() {
        if (selectedCallCampaign == null || selectedCallCampaign.getId() == null || selectedCallCampaign.getId() <= 0) {
            return false;
        }
        return true;
    }

    public List<UploadedFile> getCallCampaignFilesList() {
        return callCampaignFilesList;
    }

    public void setCallCampaignFilesList(List<UploadedFile> callCampaignFilesList) {
        this.callCampaignFilesList = callCampaignFilesList;
    }

    public List<CallCampaign> getCallCampaignList() {
        return callCampaignList;
    }

    public void setCallCampaignList(List<CallCampaign> callCampaignList) {
        this.callCampaignList = callCampaignList;
    }

}
