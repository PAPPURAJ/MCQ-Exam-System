package com.duetbd.cse18.myapplication;

public class MyUserData {
    private String name, email, phone, institute;
    private boolean admin;

    public MyUserData() {
    }

    public MyUserData(String name, String email, String phone, String institute,boolean admin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.institute = institute;
        this.admin=admin;
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

    public boolean isAdmin() {
        return admin;
    }
}
