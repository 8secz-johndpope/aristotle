package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "StaticData")
public class StaticDataPlugin extends DataPlugin {

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "StaticDataPlugin [content=" + content + ", toString()=" + super.toString() + "]";
    }

}
