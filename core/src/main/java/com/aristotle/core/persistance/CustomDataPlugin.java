package com.aristotle.core.persistance;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CustomData")
public class CustomDataPlugin extends DataPlugin {

    @Column(name = "full_class_name")
    private String fullClassName;

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    @Override
    public String toString() {
        return "CustomDataPlugin [fullClassName=" + fullClassName + ", toString()=" + super.toString() + "]";
    }

}
