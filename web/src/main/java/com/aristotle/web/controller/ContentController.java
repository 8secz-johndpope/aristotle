package com.aristotle.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/news", "/news/", "/blogs", "/events" })
public class ContentController {

    @RequestMapping
    @ResponseBody
    public String defaultMethod() {
        return "ContentController Reached Default";
    }
}
