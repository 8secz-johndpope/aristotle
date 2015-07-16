package com.aristotle.core.service;

import java.util.Date;

import com.aristotle.core.exception.AppException;

public interface TwitterReportService {

    String genrateDailyTwitterReport(Date date) throws AppException;

    String getDailyTwitterReport(Date date) throws AppException;

}
