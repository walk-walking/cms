package com.studyroom.cms.result;

public enum ResultCodeEnum {
    SUCCESS(0,"成功"),
    SYSTEMERROR(-1,"系统错误"),

    MISSPARAM(10001,"参数缺失"),
    USERNOTEXIST(10002,"用户不存在"),
    LOGINFAIL(10003,"密码错误"),

    USERHASEXIST(10004,"用户已存在"),
    ENTITYHASEXIST(10005,"实体已存在"),
    ENTITYNOTEXIST(10005,"实体不存在"),
    NOTLOGIN(20001,"没有登录"),
    LOGOUTFAIL(20002,"登出失败"),
    TIME_NOT_ENOUGH(15001,"自习时间不足"),
    STUDENT_NUMBER_NOT_VALID(15002,"学生NUMBER无效"),
    STUDY_SEAT_NOT_VALID(15003,"对于的座位不存在");


    private Integer code;
    private String desc;

    ResultCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
