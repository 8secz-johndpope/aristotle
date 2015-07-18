package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "email_template")
public class EmailTemplate extends BaseEntity {

    @Column(name = "name")
    private String name; // template name
    @Column(name = "subject")
    private String subject; // Title of the email
	@Column(name = "content",  columnDefinition="LONGTEXT")
    private String content;// content of email which can be html or plain text

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
