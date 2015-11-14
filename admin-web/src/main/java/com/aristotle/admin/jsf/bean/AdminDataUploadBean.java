package com.aristotle.admin.jsf.bean;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.service.UserService;
import com.aristotle.core.service.dto.UserUploadDto;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminDataUploadBean", beanName = "adminDataUploadBean", pattern = "/admin/user-upload", viewId = "/admin/admin_user_upload.xhtml")
@URLBeanName("adminDataUploadBean")
public class AdminDataUploadBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;
    private List<UserUploadDto> userBeingUploaded;

    @Autowired
    private UserService userService;

	public AdminDataUploadBean() {
        super("/admin/user-upload", AppPermission.ADMIN_EVENT);
	}

	// @URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback = false)
	public void init() throws Exception {

		if (!checkUserAccess()) {
			return;
		}

		System.out.println("Init");
		try {

		} catch (Exception ex) {
			sendErrorMessageToJsfScreen(ex);
		}

	}

    public void uploadData() {


	}

    public void cancel() {

    }


    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("Uploading File " + event.getFile().getFileName());

        try {
            Reader in = new InputStreamReader(event.getFile().getInputstream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').parse(in);
            userBeingUploaded = new ArrayList<UserUploadDto>();
            for (CSVRecord record : records) {
                String email = record.get("email");
                String phone = record.get("phone");
                String name = record.get("name");
                if (StringUtils.isEmpty(phone)) {
                    phone = record.get("mobile");
                }

                UserUploadDto userUploadDto = new UserUploadDto();
                userUploadDto.setEmail(email);
                userUploadDto.setPhone(phone);
                userUploadDto.setName(name);
                userBeingUploaded.add(userUploadDto);
            }
            userService.checkUserStatus(userBeingUploaded);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            sendErrorMessageToJsfScreen("Failed", event.getFile().getFileName() + " is failed to uploaded.");
        }
    }

    public List<UserUploadDto> getUserBeingUploaded() {
        return userBeingUploaded;
    }

    public void setUserBeingUploaded(List<UserUploadDto> userBeingUploaded) {
        this.userBeingUploaded = userBeingUploaded;
    }

}
