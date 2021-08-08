package com.javacourse.week07.routing;

import org.springframework.util.Assert;

public class ClientDatabaseContextHolder {
    private static ThreadLocal<String> CONTEXT
            = new ThreadLocal<>();

    public static void set(String databaseType) {
        Assert.notNull(databaseType, "databaseEnum cannot be null");
        CONTEXT.set(databaseType);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }


}
