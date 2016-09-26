package com.aristotle.web.ui.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UiTemplateManager {

    String getTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    String getCompiledTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    void refresh();

    Long getDomainLocation(HttpServletRequest httpServletRequest);

    Integer getCacheTime(HttpServletRequest httpServletRequest);
}
