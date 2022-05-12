package com.studyroom.cms.result;

public enum ExceptionCodeEnum {

    RESET_ORDER_SEAT_STATUS_FAIL(50001,"重置预约座位状态失败"),
    GET_RULED_ROOMS_FAIL(50002,"获取被设置预约规则的自习室编号List失败"),
    DELETE_UNRULE_SEAT_FAIL(50003,"未设置预约规则的座位删除失败"),
    GET_LATEST_RULED_ROOMS_FAIL(50004,"获取最近被设置预约规则的自习室编号List失败"),
    UPDATE_ORDER_RULE_TO_SEAT_FAIL(50005,"未能将最近修改的预约规则应用到座位上"),
    GET_LATEST_MOD_SEAT_FAIL(50006,"未能获取到最近修改的座位信息"),
    DELETE_OR_ADD_ORDER_SEAT_FAIL(50007,"可预约座位的删除或新增失败"),
    MOD_SEAT_STATUS_BY_ROOM_FAIL(50008,"根据自习室编号修改座位状态失败"),
    GET_ROOM_BY_TIME_FAIL(50009,"根据预约开始/结束时间获取自习室失败"),
    GET_EXPIRING_SEAT_FAIL(50010,"未能获取预约即将到期的座位信息"),
    RELEASE_EXPIRING_SEAT_FAIL(50011,"未能成功释放预约即将到期的座位"),
    GET_NOT_SIGN_IN_AND_NOT_START_ORDER_MESSAGE_FAIL(50012,"未能成功获取到即将开始但未签到的预约订单信息"),
    GET_NOT_SIGN_IN_AND_HAS_START_ORDER_MESSAGE_FAIL(50013,"未能成功获取到已经开始但未签到的预约订单信息"),
    GET_ORDER_MESSAGE_BY_CONDITION_FAIL(50014,"未能根据筛选条件返回预约订单对象"),
    GET_EMAIL_BY_NUMBER_FAIL(50015,"未能根据学号成功获取邮箱"),
    BATCH_ADD_BREACH_MESSAGE_FAIL(50016,"批量插入学生违规信息失败");

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
