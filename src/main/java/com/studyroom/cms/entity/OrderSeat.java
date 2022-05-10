package com.studyroom.cms.entity;

public class OrderSeat {

    private String seatNumber;

    private String roomNumber;

    private String building;

        //boolean? or not
    private int orderStatus;

    private String orderStartTime;

    private String orderEndTime;

    private int orderMaxTime;

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
