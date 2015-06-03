package com.aristotle.web.ui.template;

import javax.servlet.http.HttpServletRequest;

public interface UiTemplateManager {

    String getTemplate(HttpServletRequest httpServletRequest);

    void refresh();

    Long getDomainLocation(HttpServletRequest httpServletRequest);
}
