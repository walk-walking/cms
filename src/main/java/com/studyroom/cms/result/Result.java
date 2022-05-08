package com.studyroom.cms.result;

import java.util.HashMap;

public class Result extends HashMap<String,Object> {

    public Result(){
        put("code",ResultCodeEnum.SUCCESS.getCode());
        put("msg",ResultCodeEnum.SUCCESS.getDesc());
        put("data",new HashMap<>());
    }

    public static Result success(){
        return new Result();
    }

    public static Result success(HashMap<String,Object> data){
        Result ret = new Result();
        ret.put("data",data);
        return ret;
    }

    public static Result error(){
        Result ret = new Result();
        ret.put("code",ResultCodeEnum.SYSTEMERROR.getCode());
        ret.put("msg",ResultCodeEnum.SYSTEMERROR.getDesc());
        return ret;
    }

    public static Result fail(ResultCodeEnum codeEnum){
        Result ret = new Result();
        ret.put("code",codeEnum.getCode());
        ret.put("msg",codeEnum.getDesc());
        return ret;
    }

    public static Result listSuccess(int count, Object data){
        Result ret = new Result();
        ret.put("data",data);
        ret.put("count",count);
        return ret;
    }

}
