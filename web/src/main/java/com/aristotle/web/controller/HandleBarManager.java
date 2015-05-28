package com.aristotle.web.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

@Component
public class HandleBarManager {

    private final Handlebars handlebars;

    public HandleBarManager() {
        handlebars = new Handlebars();
    }

    public Handlebars getHandlebars() {
        return handlebars;
    }

    @PostConstruct
    public void init() {
        registerTrimStringFunction();
    }

    private void registerTrimStringFunction() {
        handlebars.registerHelper("trimString", new Helper<String>() {
            @Override
            public CharSequence apply(String context, Options options) throws IOException {
                System.out.println("Apply trimString : " + context.length());
                if (context.length() > 300) {
                    return context.subSequence(0, 300);
                }
                return context;
            }

        });
    }

}
