package com.studyroom.cms.result;

public class customException extends RuntimeException{

    private int code;

    private String errMsg;

    public  customException(ExceptionCodeEnum resultEnum){
        this.code = resultEnum.getCode();
        this.errMsg = resultEnum.getErrMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
