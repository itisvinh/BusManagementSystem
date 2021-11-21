package com.busmanagementsystem.Interface;

public class Emp {
    public String name;
    public int age;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public Emp(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
