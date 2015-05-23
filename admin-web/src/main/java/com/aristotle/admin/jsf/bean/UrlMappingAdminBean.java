package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.DataPlugin;
import com.aristotle.core.persistance.UrlMapping;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "urlMappingAdminBean", beanName = "urlMappingAdminBean", pattern = "/admin/urls", viewId = "/admin/admin_urls.xhtml")
@URLBeanName("urlMappingAdminBean")
public class UrlMappingAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    private UrlMapping selectedUrlMapping;
    private List<UrlMapping> urlMappings;
    private List<String> selectedDataPluginNames;
    private List<DataPlugin> dataPlugins;

	
	private boolean showList = true;
	public UrlMappingAdminBean(){
        super("/admin/urls", AppPermission.WEB_ADMIN, AppPermission.WEB_ADMIN_DRAFT);
	}
	//@URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback=false)
	public void init() throws Exception {
        System.out.println("Checking USer Access");
		if(!checkUserAccess()){
			return;
		}
        refreshPluginList();
        dataPlugins = dataPluginService.getAllDataPlugins();
	}

    private void refreshPluginList() {
        try {
            urlMappings = dataPluginService.getAllUrlMappings();

        } catch (Exception ex) {
            urlMappings = new ArrayList<UrlMapping>();
            sendErrorMessageToJsfScreen(ex);
        }
	}

    public void saveUrlMapping() {
        try {
            dataPluginService.saveUrlMapping(selectedUrlMapping);
            refreshPluginList();
            showList = true;
            System.out.println("selectedDataPluginNames = " + selectedDataPluginNames);
            if (selectedDataPluginNames != null && selectedDataPluginNames.size() > 0) {
                List<DataPlugin> selectedDataPlugins = new ArrayList<DataPlugin>(selectedDataPluginNames.size());
                for(String onePlugiName:selectedDataPluginNames){
                    for(DataPlugin oneDataPlugin : dataPlugins){
                        if(oneDataPlugin.getPluginName().equalsIgnoreCase(onePlugiName)){
                            selectedDataPlugins.add(oneDataPlugin);
                            break;
                        }
                    }
                }
                dataPluginService.addDataPluginForUrlMapping(selectedUrlMapping.getId(), selectedDataPlugins);
                
            }

        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
	}

    public void createUrlMapping() {
        selectedUrlMapping = new UrlMapping();
		showList = false;
        // selectedDataPlugins = new DataPlugin[];
	}
	public void cancel(){
        createUrlMapping();
		showList = true;
	}
	public boolean isShowList() {
		return showList;
	}
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public UrlMapping getSelectedUrlMapping() {
        return selectedUrlMapping;
    }

    public void setSelectedUrlMapping(UrlMapping selectedUrlMapping) {
        try {
            this.selectedUrlMapping = selectedUrlMapping;
            showList = false;
            List<DataPlugin> dbDataPlugins = dataPluginService.getDataPluginsByUrlMappingId(selectedUrlMapping.getId());
            if (dbDataPlugins != null) {
                selectedDataPluginNames = new ArrayList<String>();
                System.out.println("dbDataPlugins=" + dbDataPlugins);
                for (DataPlugin oneDataPlugin : dbDataPlugins) {
                    selectedDataPluginNames.add(oneDataPlugin.getPluginName());
                }
            }

        } catch (AppException e) {
            sendErrorMessageToJsfScreen(e);
        }
    }

    public List<UrlMapping> getUrlMappings() {
        return urlMappings;
    }

    public void setUrlMappings(List<UrlMapping> urlMappings) {
        this.urlMappings = urlMappings;
    }

    public List<DataPlugin> getDataPlugins() {
        return dataPlugins;
    }

    public void setDataPlugins(List<DataPlugin> dataPlugins) {
        this.dataPlugins = dataPlugins;
    }

    public List<String> getSelectedDataPluginNames() {
        return selectedDataPluginNames;
    }

    public void setSelectedDataPluginNames(List<String> selectedDataPluginNames) {
        this.selectedDataPluginNames = selectedDataPluginNames;
    }



}
