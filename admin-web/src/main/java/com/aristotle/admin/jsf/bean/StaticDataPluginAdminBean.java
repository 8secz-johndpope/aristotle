package com.aristotle.admin.jsf.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.StaticDataPlugin;
import com.google.gson.JsonParser;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "staticDataPluginAdminBean", beanName = "staticDataPluginAdminBean", pattern = "/admin/sdplugin", viewId = "/admin/admin_sdplugin.xhtml")
@URLBeanName("staticDataPluginAdminBean")
public class StaticDataPluginAdminBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;


    private StaticDataPlugin selectedStaticDataPlugin;
    private List<StaticDataPlugin> staticDataPlugins;
	
	private boolean showList = true;
	
	public StaticDataPluginAdminBean(){
        super("/admin/sdplugin", AppPermission.WEB_ADMIN, AppPermission.WEB_ADMIN_DRAFT);
	}
	//@URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback=false)
	public void init() throws Exception {
        System.out.println("Checking USer Access");
		if(!checkUserAccess()){
			return;
		}
        refreshPluginList();
	}

    private void refreshPluginList() {
        try {
            staticDataPlugins = adminService.getAllStaticDataPlugin();
        } catch (Exception ex) {
            staticDataPlugins = new ArrayList<StaticDataPlugin>();
            sendErrorMessageToJsfScreen(ex);
        }
	}

    public void saveStaticDataPlugin() {
        try {
            checkForValidJson(selectedStaticDataPlugin.getContent());
            adminService.saveStaticDataPlugin(selectedStaticDataPlugin);
            refreshPluginList();
            showList = true;
        } catch (Exception e) {
            sendErrorMessageToJsfScreen(e);
        }
	}

    private void checkForValidJson(String message) throws AppException {
        try {
            JsonParser jsonParser = new JsonParser();
            jsonParser.parse(message);
        } catch (Exception ex) {
            throw new AppException("Not a valid Json Message");
        }
    }

    public void createStaticDataPlugin() {
        selectedStaticDataPlugin = new StaticDataPlugin();
		showList = false;
	}
	public void cancel(){
        createStaticDataPlugin();
		showList = true;
	}
	public boolean isShowList() {
		return showList;
	}
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public StaticDataPlugin getSelectedStaticDataPlugin() {
        return selectedStaticDataPlugin;
    }

    public void setSelectedStaticDataPlugin(StaticDataPlugin selectedStaticDataPlugin) {
        this.selectedStaticDataPlugin = selectedStaticDataPlugin;
        showList = false;
    }

    public List<StaticDataPlugin> getStaticDataPlugins() {
        return staticDataPlugins;
    }

    public void setStaticDataPlugins(List<StaticDataPlugin> staticDataPlugins) {
        this.staticDataPlugins = staticDataPlugins;
    }

}
