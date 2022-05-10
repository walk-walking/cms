package com.studyroom.cms.entity;

import java.util.Date;

public class StudentOrderMessage {

    private String seatNumber;

    private String roomNumber;

    private String studentNumber;

    private Date orderStartTime;

    private Date orderEndTime;

    private int isSignIn;

    private int isOrderValid;

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
}
