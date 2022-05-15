package com.studyroom.cms.result;

public enum ResultCodeEnum {
    SUCCESS(0,"成功"),
    SYSTEM_ERROR(-1,"系统错误"),

    MISS_PARAM(10001,"参数缺失"),
    USER_NOT_EXIST(10002,"用户不存在"),
    LOGIN_FAIL(10003,"密码错误"),

    USER_HAS_EXIST(10004,"用户已存在"),
    ENTITY_HAS_EXIST(10005,"实体已存在"),
    ENTITY_NOT_EXIST(10005,"实体不存在"),

    USER_ORIGIN_PASSWORD_IS_WRONG(10006,"用户提供的原有密码错误"),

    WRONG_PARAM_VALUE(10007,"参数值错误"),

    NOT_LOGIN(20001,"没有登录"),
    LOGOUT_FAIL(20002,"登出失败"),
    TIME_NOT_ENOUGH(15001,"自习时间不足"),
    STUDENT_NUMBER_NOT_VALID(15002,"学生NUMBER无效"),
    STUDY_SEAT_NOT_VALID(15003,"对应的座位不存在或无效"),
    STUDENT_ID_NOT_MATCHING(15004,"用于登录的学生ID与想操作的学生ID不一样"),
    ORDER_MESSAGE_NOT_EXIST(15005,"没有对应的预约信息"),
    SIGNIN_TIME_OUT(15005,"已经过了签到时间"),
    SIGNIN_TIME_TOO_EARLY(15006,"签到时间过早"),
    IS_SIGNINED(15007,"您已经签过到"),
    ORDER_NUMBER_NOT_EXIST(15008,"没有对应的订单"),
    CANCEL_TIME_OUT(15009,"已经过了可以取消订单的时间"),
    IS_CANCEL(15009,"已经取消过预约"),
    REVERSE_MESSAGE_QUERY_FAIL(15010,"查询对应学生NUMBER的有效预约信息失败"),
    PAGE_OR_PAGESIZE_LESS_THAN_ZERO(15011,"页码和页长小于等于0"),
    RESERVE_TIME_ERROR(15012,"预约时间错误");



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
