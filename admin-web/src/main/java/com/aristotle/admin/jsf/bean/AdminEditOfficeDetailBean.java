package com.aristotle.admin.jsf.bean;

import java.util.List;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Office;
import com.aristotle.core.service.AppService;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminEditOfficeDetailBean", beanName = "adminEditOfficeDetailBean", pattern = "/admin/office", viewId = "/admin/admin_office.xhtml")
@URLBeanName("adminEditOfficeDetailBean")
public class AdminEditOfficeDetailBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

    @Autowired
    private AppService appService;

    private Office selectedOffice;

	private boolean showList = true;
	
	private static double defaultLattitude = 23.2411685061471;
	private static double defaultLongitude = 77.4254605000001;
	
	private MapModel draggableMapModel;
	
	Marker marker;

    private List<Office> officeList;

	public AdminEditOfficeDetailBean() {
		super("/admin/office", AppPermission.EDIT_OFFICE_ADDRESS);
	}

	// @URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback = false)
	public void init() throws Exception {
		if (!checkUserAccess()) {
			return;
		}
		refreshNewsList();
		draggableMapModel = new DefaultMapModel();
		
		LatLng coord1 = new LatLng(selectedOffice.getLattitude(), selectedOffice.getLongitude()); 
        //Draggable  
		marker = new Marker(coord1, "Office Location");
		marker.setDraggable(true);
		draggableMapModel.addOverlay(marker);  
	}
	
	public void onMarkerDrag(MarkerDragEvent event) {  
        marker = event.getMarker();  
    }  
	public void onStateChange(StateChangeEvent event) {
		selectedOffice.setDepth(event.getZoomLevel());
    }  

    private void refreshNewsList() throws AppException {
        if (menuBean.getSelectedLocation() == null) {
            officeList = appService.getLocationOffices(null);
        } else {
            officeList = appService.getLocationOffices(menuBean.getSelectedLocation().getId());
        }

		if (officeList == null || officeList.size() <= 1) {
			if (officeList.size() == 1) {
				selectedOffice = officeList.get(0);
			} else {
                selectedOffice = new Office();
				selectedOffice.setLattitude(defaultLattitude);
				selectedOffice.setLongitude(defaultLongitude);
				selectedOffice.setDepth(10);
			}
			showList = false;
		}
	}

	public void saveOfficeDetail() {
		selectedOffice.setLattitude(marker.getLatlng().getLat());
		selectedOffice.setLongitude(marker.getLatlng().getLng());
		try {
		    Long locationId = null;
		    if (menuBean.getSelectedLocation() != null) {
	            locationId = menuBean.getSelectedLocation().getId();
	        }

			if (isValidInput()) {
                selectedOffice = appService.saveOffice(selectedOffice, locationId);
				sendInfoMessageToJsfScreen("Office Details saved Succesfully");
				refreshNewsList();
			}
		} catch (Exception ex) {
			sendErrorMessageToJsfScreen("Unable to save Office Detail", ex);
		}
	}

	public boolean isShowList() {
		return showList;
	}

	public void setShowList(boolean showList) {
		this.showList = showList;
	}

    public Office getSelectedOffice() {
		return selectedOffice;
	}

    public void setSelectedoffice(Office selectedoffice) {
		this.selectedOffice = selectedoffice;
		showList = false;
	}

    public List<Office> getOfficeList() {
		return officeList;
	}

    public void setOfficeList(List<Office> officeList) {
		this.officeList = officeList;
	}

	public MapModel getDraggableMapModel() {
		return draggableMapModel;
	}

	public void setDraggableMapModel(MapModel draggableMapModel) {
		this.draggableMapModel = draggableMapModel;
	}

}
