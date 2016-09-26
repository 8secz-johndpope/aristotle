package com.aristotle.web.ui.template;

import com.github.jknack.handlebars.Template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UiTemplateManager {

    String getTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    Template getCompiledTemplate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    void refresh();

    Long getDomainLocation(HttpServletRequest httpServletRequest);

    Integer getCacheTime(HttpServletRequest httpServletRequest);
}
