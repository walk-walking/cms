package com.studyroom.cms.entity;

public class OrderRule {
        //not know (tinyint) is a int or a boolean

    private String roomNumber;

    private String openTime;

    private String closeTime;

    private int singleOrderTime;

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public int getSingleOrderTime() {
        return singleOrderTime;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public void setSingleOrderTime(int singleOrderTime) {
        this.singleOrderTime = singleOrderTime;
    }

}
