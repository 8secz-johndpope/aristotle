package com.aristotle.admin.jsf.bean;

import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aristotle.core.enums.AppPermission;
import com.aristotle.core.exception.AppException;
import com.aristotle.core.persistance.Event;
import com.aristotle.core.service.EventService;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "adminEventBean", beanName = "adminEventBean", pattern = "/admin/event", viewId = "/admin/admin_event.xhtml")
@URLBeanName("adminEventBean")
public class AdminEventBean extends BaseMultiPermissionAdminJsfBean {

	private static final long serialVersionUID = 1L;

	private ScheduleModel eventModel;

    private boolean showCalendarView = true;

	private ScheduleEvent event = new ScheduleEvent();

	private MapModel draggableMapModel;
	
    @Autowired
    private EventService eventService;

	Marker marker;

	private static double defaultLattitude = 23.934102635197338;
	private static double defaultLongitude = 78.310546875;
	private static int defaultDepth = 4;

	public AdminEventBean() {
        super("/admin/event", AppPermission.ADMIN_EVENT);
		eventModel = new DefaultScheduleModel();
	}

	// @URLActions(actions = { @URLAction(mappingId = "userProfileBean") })
	@URLAction(onPostback = false)
	public void init() throws Exception {

		if (!checkUserAccess()) {
			return;
		}

		System.out.println("Init");
		try {
			draggableMapModel = new DefaultMapModel();

			LatLng coord1 = new LatLng(defaultLattitude, defaultLongitude);
			// Draggable
			marker = new Marker(coord1, "Event Location");
			marker.setDraggable(true);
			draggableMapModel.addOverlay(marker);

			event.setLattitude(defaultLattitude);
			event.setLongitude(defaultLongitude);
			event.setDepth(defaultDepth);
			refreshEvents();
		} catch (Exception ex) {
			sendErrorMessageToJsfScreen(ex);
		}

	}

	public void saveEvent(ActionEvent actionEvent) {
		try {
            if (StringUtils.isEmpty(event.getTitle())) {
				sendErrorMessageToJsfScreen("Please enter Title");
			}
            if (StringUtils.isEmpty(event.getDescription())) {
				sendErrorMessageToJsfScreen("Please enter Event Description");
			}
			if (event.getStartDate() == null) {
				sendErrorMessageToJsfScreen("Please enter the start date of Event");
			}
			if (event.getEndDate() == null) {
				sendErrorMessageToJsfScreen("Please enter the end date of Event");
			}
			if (isValidInput()) {
				saveEvent(event);
				sendInfoMessageToJsfScreen("Event saved succesfully");
                showCalendarView = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			sendErrorMessageToJsfScreen("Unable to save Post", ex);
		}

	}

    public void cancel() {
        showCalendarView = true;
    }
	private void saveEvent(ScheduleEvent event) throws AppException{
		System.out.println("Valid input " +event);
		// aapService.savePlannedEmail(selectedEmail);
		//refreshEvents();
		// RequestContext.getCurrentInstance().execute("eventDialog.hide()");
        Event eventDto = new Event();
		eventDto.setId(event.getDbId());
		eventDto.setTitle(event.getTitle());
		eventDto.setAddress(event.getAddress());
		eventDto.setContactNumber1(event.getContactNumber1());
		eventDto.setContactNumber2(event.getContactNumber2());
		eventDto.setContactNumber3(event.getContactNumber3());
		eventDto.setContactNumber4(event.getContactNumber4());
        eventDto.setContactEmail(event.getContactEmail());
		eventDto.setDepth(event.getDepth());
		eventDto.setDescription(event.getDescription());
		eventDto.setEndDate(event.getEndDate());
		eventDto.setFbEventId(event.getFbEventId());
		eventDto.setLattitude(marker.getLatlng().getLat());
		eventDto.setLongitude(marker.getLatlng().getLng());
        eventDto.setVer(event.getVer());
		eventDto.setStartDate(event.getStartDate());
		eventDto.setTitle(event.getTitle());

        Long selectedLocationId = null;

        eventDto = eventService.saveEvent(eventDto, menuBean.getSelectedLocation());
		event = new ScheduleEvent(eventDto);
		//eventModel.addEvent(event);
		refreshEvents();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		try {
			event = (ScheduleEvent) selectEvent.getObject();
			marker.setLatlng(new LatLng(((ScheduleEvent) selectEvent.getObject()).getLattitude(), ((ScheduleEvent) selectEvent.getObject())
					.getLongitude()));
            showCalendarView = false;
		} catch (Exception ex) {
			sendErrorMessageToJsfScreen(ex);
		}
	}

	public void onDateSelect(SelectEvent selectEvent) {
		try {
			event = new ScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
			event.setLattitude(defaultLattitude);
			event.setLongitude(defaultLongitude);
			event.setDepth(defaultDepth);
            event.setContactEmail("contact@swarajabhiyan.org");
			marker.setLatlng(new LatLng(defaultLattitude, defaultLongitude));
            showCalendarView = false;
		} catch (Exception ex) {
			sendErrorMessageToJsfScreen(ex);
		}
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		try {
			saveEvent((ScheduleEvent)event.getScheduleEvent());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved" , "Day delta:" + event.getDayDelta() + ", Minute delta:"
					+ event.getMinuteDelta());
			
			addMessage(message);
			
		} catch (Exception ex) {
			sendErrorMessageToJsfScreen(ex);
		}
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		try {
			saveEvent((ScheduleEvent)event.getScheduleEvent());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event timings changed", "Day delta:" + event.getDayDelta() + ", Minute delta:"
					+ event.getMinuteDelta());

			addMessage(message);
			
		} catch (Exception ex) {
			sendErrorMessageToJsfScreen(ex);
		}

	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private void refreshEvents() {
		try {
            List<Event> events = eventService.getLocationEvents(menuBean.getSelectedLocation(), 100);
			eventModel.clear();
            for (Event oneEvent : events) {
				eventModel.addEvent(new ScheduleEvent(oneEvent));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onMarkerDrag(MarkerDragEvent event) {
		marker = event.getMarker();
	}

	public void onStateChange(StateChangeEvent stateChangeEvent) {
		this.event.setDepth(stateChangeEvent.getZoomLevel());
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public MapModel getDraggableMapModel() {
		return draggableMapModel;
	}

	public void setDraggableMapModel(MapModel draggableMapModel) {
		this.draggableMapModel = draggableMapModel;
	}

    public boolean isShowCalendarView() {
        return showCalendarView;
    }

    public void setShowCalendarView(boolean showCalendarView) {
        this.showCalendarView = showCalendarView;
    }

}
