package com.busmanagementsystem.Database.Services;

public class Utilities {
    private Utilities() {
    }

    public static String concatAll(String... args) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String i : args)
            stringBuilder.append(i);

        return  stringBuilder.toString();
    }
    public static String sqlString(Object object) {
        return concatAll("'", object.toString().trim(), "'");
    }
}
