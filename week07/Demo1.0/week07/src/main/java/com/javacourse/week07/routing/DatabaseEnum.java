package com.javacourse.week07.routing;

public enum DatabaseEnum {

    DATASOURCE_MASTER("master"),
    DATASOURCE_SLAVE("slave");

    public String getValue() {
        return value;
    }

    private String value;

    DatabaseEnum(String value) {
        this.value = value;
    }


}
