package com.htx.schoolstarter.starter;

import java.util.ArrayList;
import java.util.List;

public class Klass {
    private int id;
    private List<Student> students = new ArrayList<>();

    public Klass(int id) {
        this.id = id;
    }

    public void addStudent(Student student) {
        students.add(student);
        System.out.println(student.toString() + " added to class "+id);
    }

    public String toString() {
        return "Klass: "+id;
    }

}
