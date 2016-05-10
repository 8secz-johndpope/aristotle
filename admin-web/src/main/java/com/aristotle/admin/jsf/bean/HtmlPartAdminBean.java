package com.aristotle.admin.jsf.bean;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.DomainTemplatePart;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "htmlPartAdminBean", beanName = "htmlPartAdminBean", pattern = "/admin/htmlpart", viewId = "/admin/html_part.xhtml")
@URLBeanName("htmlPartAdminBean")
public class HtmlPartAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    private DomainTemplatePart selectedDomainTemplatePart;
    private List<DomainTemplatePart> domainTemplateParts;

	
	private boolean showList = true;
	public HtmlPartAdminBean(){
        super("/admin/html_part", AppPermission.WEB_ADMIN, AppPermission.WEB_ADMIN_DRAFT);
	}
	//@URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback=false)
	public void init() throws Exception {
        System.out.println("Checking USer Access");
		if(!checkUserAccess()){
			return;
		}
        domainTemplateParts = uiTemplateService.getDomainTemplatePartsByDomainTemplateId(1L);// TODO remove Hard code
	}

    public void saveDomainTemplatePart() {
        try {
            selectedDomainTemplatePart = uiTemplateService.saveDomainTemplatePart(selectedDomainTemplatePart);
            domainTemplateParts = uiTemplateService.getDomainTemplatePartsByDomainTemplateId(1L);
        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
	}

	public void cancel(){
		showList = true;
	}
	public boolean isShowList() {
		return showList;
	}
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public List<DomainTemplatePart> getDomainTemplateParts() {
        return domainTemplateParts;
    }

    public void setDomainTemplateParts(List<DomainTemplatePart> domainTemplateParts) {
        this.domainTemplateParts = domainTemplateParts;
    }

    public DomainTemplatePart getSelectedDomainTemplatePart() {
        return selectedDomainTemplatePart;
    }

    public void setSelectedDomainTemplatePart(DomainTemplatePart selectedDomainTemplatePart) {
        this.selectedDomainTemplatePart = selectedDomainTemplatePart;
        showList = false;
    }



}
