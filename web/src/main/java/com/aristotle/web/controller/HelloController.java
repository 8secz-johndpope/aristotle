package com.aristotle.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.aristotle.core.persistance.repo.CustomerRepository;

public class HelloController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BeanFactory beanFactory;

    @PostConstruct
    public void init() {
        String urlMapping1 = "/test/{param1}";

        //
    }

    private static class CustomUrlMapping {
        String originalMapping;
        Pattern pattern;
        List<String> parameters;

        public CustomUrlMapping(String urlMapping) {
            originalMapping = urlMapping;
            parameters = new ArrayList<String>();
            // Build parameters and pattern
            char[] charArray = urlMapping.toCharArray();
            StringBuilder sb = new StringBuilder();
            boolean paramStarted = false;
            StringBuilder paramNameBuilder = new StringBuilder();
            for (char oneChar : charArray) {
                if (oneChar == '{') {
                    paramStarted = true;
                    paramNameBuilder = new StringBuilder();
                    continue;
                }
                if (oneChar == '}') {
                    paramStarted = false;
                    sb.append("(.*)");
                    parameters.add(paramNameBuilder.toString());
                    continue;
                }
                if (paramStarted) {
                    paramNameBuilder.append(oneChar);
                    continue;
                }
                sb.append(oneChar);
            }
            if (!parameters.isEmpty()) {
                System.out.println("sb = " + sb.toString());
                pattern = Pattern.compile(sb.toString());
            }
        }


        @Override
        public String toString() {
            return "CustomUrlMapping [originalMapping=" + originalMapping + ", pattern=" + pattern + ", parameters=" + parameters + "]";
        }

    }

    public static void maisn(String[] args) {
        CustomUrlMapping customUrlMapping = new CustomUrlMapping("/test2/{Param1}/{Param2}/do");
        System.out.println(customUrlMapping);
        String url = "/test2/first/second/do";

        // url = "This order was placed for QT3000! OK?";
        // pattern = "(.*)(\\d+)(.*)";

        Pattern r = customUrlMapping.pattern;
        Matcher m = r.matcher(url);
        if (m.find()) {
            int count = 1;
            for (String oneParam : customUrlMapping.parameters) {
                System.out.println("Found " + oneParam + ": " + m.group(count));
                count++;
            }

        } else {
            System.out.println("NO MATCH");
        }

    }

    public String defaultMethod(ModelAndView mv, HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        System.out.println("restOfTheUrl=" + restOfTheUrl);
        System.out.println("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables=" + request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
        System.out.println("org.springframework.web.servlet.HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE=" + request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE));
        for (Entry<String, Object> oneEntry : mv.getModel().entrySet()) {
            System.out.println(oneEntry.getKey() + " : " + oneEntry.getValue());
        }
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = beanFactory.getBean("customeSimpleUrlHandlerMapping", SimpleUrlHandlerMapping.class);
        ((Map<String, Object>) simpleUrlHandlerMapping.getUrlMap()).put("test3/{param1}/{param9}", this);
        // test3/123/345
        return "Reached Default";
    }

    @RequestMapping("/test/{param1}/{param2}")
    @ResponseBody
    public Map<String, Object> test(ModelAndView mv, HttpServletRequest request, @PathVariable String param1) {
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        System.out.println("restOfTheUrl=" + restOfTheUrl);
        System.out.println("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables=" + request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
        System.out.println("org.springframework.web.servlet.HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE=" + request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE));
        for (Entry<String, Object> oneEntry : mv.getModel().entrySet()) {
            System.out.println(oneEntry.getKey() + " : " + oneEntry.getValue());
        }
        mv.getModel().put("Status", "Working");
        return mv.getModel();
    }

}