package com.htx.schoolstarter.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@PropertySource("application.properties")
@ConfigurationProperties(prefix = "school")
public class MyProperty {

    private List<Integer> studentIds;

    public List<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.studentIds = studentIds;
    }


    public List<Integer> getClassIds() {
        return ClassIds;
    }

    public void setClassIds(List<Integer> myClassIds) {
        this.ClassIds = myClassIds;
    }

    public List<Map<String, Integer>> getStudentOfClass() {
        return studentOfClass;
    }

    public void setStudentOfClass(List<Map<String, Integer>> studentOfClass) {
        this.studentOfClass = studentOfClass;
    }

    private List<Integer> ClassIds;
    private List<Map<String, Integer>> studentOfClass;

}
