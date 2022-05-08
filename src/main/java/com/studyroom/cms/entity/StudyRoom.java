package com.studyroom.cms.entity;

import java.util.ArrayList;
import java.util.List;

public class StudyRoom {

    private int id;
    private String number;
    private String campus;
    private String building;
    private int set_count;
    private int is_valid;

    public static List<String> getIntColumn(){
        List<String> ret = new ArrayList<>();
        ret.add("seat_count");
        ret.add("is_valid");
        return ret;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public int getSet_count() {
        return set_count;
    }

    public void setSet_count(int set_count) {
        this.set_count = set_count;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(int is_valid) {
        this.is_valid = is_valid;
    }
}
