package com.aristotle.web.controller;

import java.io.IOException;

public class HelperSource {

    public String trimString(String content, Integer trimLength) throws IOException {
        if (trimLength == null) {
            trimLength = 150;
        }
        if (content.length() > trimLength) {
            return content.substring(0, trimLength);
        }
        return content;
    }

}
