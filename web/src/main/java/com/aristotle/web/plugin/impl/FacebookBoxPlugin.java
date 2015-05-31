package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FacebookBoxPlugin extends AbstractDataPlugin {

    public FacebookBoxPlugin(String pluginName) {
        super(pluginName);
    }

    public FacebookBoxPlugin() {
    }

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) {
        String fbData = "<iframe src=\"//www.facebook.com/plugins/likebox.php?href=https://www.facebook.com/swarajabhiyan&amp;width&amp;height=430&amp;colorscheme=light&amp;show_faces=true&amp;header=true&amp;stream=true&amp;show_border=true&amp;appId=607545139264444\" scrolling=\"no\" frameborder=\"0\" style=\"border:none; overflow:hidden; height:430px; width: 100%;\" allowTransparency=\"true\"></iframe>";
        JsonObject context = (JsonObject) mv.getModel().get("context");
        context.addProperty(name, fbData);

    }

}
