package com.studyroom.cms.entity;

public class OrderRule {

    public static final String ORDER_TIME_TYPE_OPEN = "open";  //预约时间类型--开始时间
    public static final String ORDER_TIME_TYPE_CLOSE = "close";  //预约时间类型--结束时间

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
