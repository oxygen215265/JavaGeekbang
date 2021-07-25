package com.htx.schoolstarter.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(MyProperty.class)
public class SchoolAutoConfiguration {

    @Autowired MyProperty myProperty;


    @Bean
    public School school() {
        School school = new School(1);
        List<Integer> students = myProperty.getStudentIds();
        List<Integer> klasses = myProperty.getClassIds();
        List<Map<String, Integer>> studentOfClass = myProperty.getStudentOfClass();

        HashMap<Integer,Student> studentMap = new HashMap<>();
        for(int i=0;i<students.size();i++) {
            studentMap.put(students.indexOf(i),new Student(students.indexOf(i)));
        }

        HashMap<Integer,Klass> klassMap = new HashMap<>();
        for(int i=0;i<klasses.size();i++) {
            klassMap.put(klasses.indexOf(i),new Klass(klasses.indexOf(i)));
        }

        for(Map<String,Integer> studentKlassMap:studentOfClass) {

            klassMap.get(studentKlassMap.get("klassId")).addStudent(studentMap.get(studentKlassMap.get("studentId")));
        }

        for(Klass klass:klassMap.values()){
            school.addKlass(klass);
        }

        return school;


    }


}
