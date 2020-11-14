package com.example.jobtask;

public class User {

    private String Name;
    private String Age;

    public void setName(String name) {
        Name = name;
    }

    public User(String name, String age) {
        Name = name;
        Age = age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getName() {
        return Name;
    }

    public String getAge() {
        return Age;
    }
}
