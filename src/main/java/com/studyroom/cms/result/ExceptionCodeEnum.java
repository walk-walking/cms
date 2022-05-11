package com.studyroom.cms.result;

public enum ExceptionCodeEnum {

    RESET_ORDER_SEAT_STATUS_FAIL(50001,"重置预约座位状态失败"),
    GET_RULED_ROOMS_FAIL(50002,"获取被设置预约规则的自习室编号List失败"),
    DELETE_UNRULE_SEAT_FAIL(50003,"未设置预约规则的座位删除失败"),
    GET_LATEST_RULED_ROOMS_FAIL(50004,"获取最近被设置预约规则的自习室编号List失败"),
    UPDATE_ORDER_RULE_TO_SEAT_FAIL(50005,"未能将最近修改的预约规则应用到座位上"),
    GET_LATEST_MOD_SEAT_FAIL(50006,"未能获取到最近修改的座位信息"),
    DELETE_OR_ADD_ORDER_SEAT_FAIL(50007,"可预约座位的删除或新增失败");


    private Integer code;
    private String errMsg;

    ExceptionCodeEnum(Integer code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public Integer getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
