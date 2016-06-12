package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.core.service.HttpUtil;
import com.aristotle.core.service.LocationService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * /district/{districtId}
 * @author Ravi
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DistrictMemberListPlugin extends AbstractDataPlugin {

    @Autowired
    private LocationService locationService;

    @Value("${user_search_end_point}")
	private String searchEndPoint;
    
    @Autowired
    private HttpUtil httpUtil;
    
    private JsonParser jsonParser = new JsonParser();
    
    public DistrictMemberListPlugin() {
    }

    public DistrictMemberListPlugin(String pluginName) {
        super(pluginName);
    }
    
    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        logger.info("Applying {} plugin", name);
        try {
            JsonObject context = (JsonObject) mv.getModel().get("context");
            String url = httpServletRequest.getRequestURI();
            Long districtId = getLongParameterFromPathOrParams(httpServletRequest, "districtId");
            JsonObject members = getMembers(districtId);
            context.add(name, members);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JsonObject getMembers(Long selectedLocationId) throws Exception{
		String result = null;
		String query = "q="+selectedLocationId+"&q.parser=simple&q.options=%7BdefaultOperator:%22and%22,fields:[%22livingdistrictid%22,%22votingdistrictid%22]%7D&return=_all_fields&size=9999&start=0&sort=name%20asc";

		String url = "http://" + searchEndPoint + "/2013-01-01/search?" + query;
		result = httpUtil.getResponse(url);

		JsonObject resultObject = jsonParser.parse(result).getAsJsonObject();
		JsonObject members = resultObject.get("hits").getAsJsonObject();

		return members;
	}


}
