package com.studyroom.cms.entity;

public class StudySeat {

    private int id;
    private String number;
    private String room_number;
    private int has_plug;

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

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public int getHas_plug() {
        return has_plug;
    }

    public void setHas_plug(int has_plug) {
        this.has_plug = has_plug;
    }
}
