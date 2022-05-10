package com.studyroom.cms.entity;

import java.util.Date;

public class BreachMessage {

    private String studentNumber;

    private Date breachTime;

    private int breachOrderId;

    public String getStudentNumber() {
        return studentNumber;
    }

    public Date getBreachTime() {
        return breachTime;
    }

    public int getBreachOrderId() {
        return breachOrderId;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setBreachTime(Date breachTime) {
        this.breachTime = breachTime;
    }

    public void setBreachOrderId(int breachOrderId) {
        this.breachOrderId = breachOrderId;
    }

}
