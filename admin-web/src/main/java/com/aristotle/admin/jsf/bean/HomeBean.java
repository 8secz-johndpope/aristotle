package com.aristotle.admin.jsf.bean;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.ocpsoft.pretty.faces.annotation.URLBeanName;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
@URLMapping(id = "homeBean", beanName = "homeBean", pattern = "/admin/home", viewId = "/admin/admin_home.xhtml")
@URLBeanName("homeBean")
public class HomeBean {

    private String name;

    @PostConstruct
    public void init() {
        name = "Ravi";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
