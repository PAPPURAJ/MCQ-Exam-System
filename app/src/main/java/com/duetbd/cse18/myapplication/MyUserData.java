package com.duetbd.cse18.myapplication;

public class MyUserData {
    private String name, email, phone, institute;

    public MyUserData() {
    }

    public MyUserData(String name, String email, String phone, String institute) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.institute = institute;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getInstitute() {
        return institute;
    }
}
