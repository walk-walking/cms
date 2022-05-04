package com.studyroom.cms.result;

public enum ResultCodeEnum {
    SUCCESS(0,"成功"),
    SYSTEMERROR(-1,"系统错误"),

    MISSPARAM(10001,"参数缺失"),
    USERNOTEXIST(10002,"用户不存在"),
    LOGINFAIL(10003,"密码错误");

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
