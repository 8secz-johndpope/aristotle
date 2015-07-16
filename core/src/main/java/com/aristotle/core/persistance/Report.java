package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "reports")
public class Report extends BaseEntity {

    @Column(name = "report_type")
    private String reportType;
	@Column(name = "content",  columnDefinition="LONGTEXT")
    private String content;// json Content
    @Column(name = "report_date_time_id")
    private String reportDateTimeId;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReportDateTimeId() {
        return reportDateTimeId;
    }

    public void setReportDateTimeId(String reportDateTimeId) {
        this.reportDateTimeId = reportDateTimeId;
    }


}
