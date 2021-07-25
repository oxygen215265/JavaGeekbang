package com.htx.schoolstarter.starter;

import java.util.ArrayList;
import java.util.List;

public class School {
    private int id;
    private List<Klass> klassList = new ArrayList<>();
    public School(int id) {
        this.id = id;
    }

    public void addKlass(Klass klass) {
        klassList.add(klass);
        System.out.println(klass.toString() + " added to school "+id);
    }

}
