package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.aristotle.web.parameters.HttpParameters;
import com.aristotle.web.plugin.DataPlugin;

public abstract class AbstractDataPlugin implements DataPlugin {

    protected Pageable getPageRequest(HttpServletRequest httpServletRequest) {
        int page = getIntPramater(httpServletRequest, HttpParameters.PAGE_NUMBER_PARAM, HttpParameters.PAGE_NUMBER_DEFAULT_VALUE);
        int size = getIntPramater(httpServletRequest, HttpParameters.PAGE_SIZE_PARAM, HttpParameters.PAGE_SIZE_DEFAULT_VALUE);
        Pageable pageable = new PageRequest(page, size);
        return pageable;
    }

    protected int getIntPramater(HttpServletRequest httpServletRequest, String paramName, int defaultValue) {
        try {
            return Integer.parseInt(httpServletRequest.getParameter(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    protected long getLongPramater(HttpServletRequest httpServletRequest, String paramName, int defaultValue) {
        try {
            return Long.parseLong(httpServletRequest.getParameter(paramName));
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
