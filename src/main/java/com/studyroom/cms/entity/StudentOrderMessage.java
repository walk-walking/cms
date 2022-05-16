package com.studyroom.cms.entity;

import java.util.Date;

public class StudentOrderMessage {
    private int id;

    private String seatNumber;

    private String roomNumber;

    private String studentNumber;

    private Date orderStartTime;

    private Date orderEndTime;

    private String orderStartTimeSTR;

    private String orderEndTimeSTR;

    private int isSignIn;

    private int isOrderValid;

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

    public String getStudentNumber() {
        return studentNumber;
    }

    public Date getOrderStartTime() {
        return orderStartTime;
    }

    public Date getOrderEndTime() {
        return orderEndTime;
    }

    public String getOrderEndTimeSTR() {
        return orderEndTimeSTR;
    }

    public String getOrderStartTimeSTR() {
        return orderStartTimeSTR;
    }

    public int getIsSignIn() {
        return isSignIn;
    }

    public int getIsOrderValid() {
        return isOrderValid;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setOrderStartTime(Date orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public void setOrderEndTime(Date orderEndTime) {
        this.orderEndTime = orderEndTime;
    }

    public void setIsSignIn(int isSignIn) {
        this.isSignIn = isSignIn;
    }

    public void setIsOrderValid(int isOrderValid) {
        this.isOrderValid = isOrderValid;
    }

    public void setOrderEndTimeSTR(String orderEndTimeSTR) {
        this.orderEndTimeSTR = orderEndTimeSTR;
    }

    public void setOrderStartTimeSTR(String orderStartTimeSTR) {
        this.orderStartTimeSTR = orderStartTimeSTR;
    }
}
