package com.aristotle.web.plugin.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.aristotle.web.plugin.PluginManager;

@Service
@Transactional
public class PluginManagerImpl implements PluginManager {

    @Override
    public void applyAllPluginsForUrl(HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
        // TODO Auto-generated method stub

    }

}
