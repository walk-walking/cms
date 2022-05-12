package com.studyroom.cms.entity;

public class OrderSeat {

    public static final int ORDER_STATUS_NOTOPEN = -1;  //  未开放
    public static final int ORDER_STATUS_OPEN = 0;  //待预约
    public static final int ORDER_STATUS_ORDERED = 1;  //已预约

    private int id;

    private String seatNumber;

    private String roomNumber;

    private String building;

        //boolean? or not
    private int orderStatus;

    private String orderStartTime;

    private String orderEndTime;

    private int orderMaxTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public String getOrderStartTime() {
        return orderStartTime;
    }

    public String getOrderEndTime() {
        return orderEndTime;
    }

    public int getOrderMaxTime() {
        return orderMaxTime;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setOrderStartTime(String orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public void setOrderEndTime(String orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public void setOrderMaxTime(int orderMaxTime) {
        this.orderMaxTime = orderMaxTime;
    }

}
