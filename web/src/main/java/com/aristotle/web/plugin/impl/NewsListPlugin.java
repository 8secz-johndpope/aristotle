package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.ModelAndView;

public class NewsListPlugin extends AbstractDataPlugin {

    private int totalNews = 10;

    @Override
    public void applyPlugin(HttpServletRequest httpServletRequest, ModelAndView mv) {
        Pageable pageRequest = getPageRequest(httpServletRequest);
    }


}
