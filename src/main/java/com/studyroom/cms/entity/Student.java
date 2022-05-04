package com.studyroom.cms.entity;

public class Student {

    private String number;
    private String name;
    private String campus;
    private int finish_year;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public int getFinish_year() {
        return finish_year;
    }

    public void setFinish_year(int finish_year) {
        this.finish_year = finish_year;
    }
}
