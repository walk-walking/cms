package com.studyroom.cms.controller;

import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.service.StudySeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/studyseat")
public class StudySeatController {

    @Autowired
    private StudySeatService studySeatService;

    @RequestMapping("/list")
    public Result list(HttpServletRequest request, HttpServletResponse response){
        try{
            String pageStr = request.getParameter("page");
            String pageSizeStr = request.getParameter("limit");
            String roomNumber = request.getParameter("room_number");
            int page = (pageStr != null &&  pageStr != "") ? Integer.parseInt(pageStr) : 1;
            int pageSize = (pageSizeStr != null &&  pageSizeStr != "") ? Integer.parseInt(pageSizeStr) : 10;
            if (roomNumber == null || roomNumber == ""){
                return Result.fail(ResultCodeEnum.MISSPARAM);
            }

            HashMap<String,Object> ret = studySeatService.getListByRoomNumber(page,pageSize,roomNumber);
            return Result.listSuccess(Integer.valueOf(ret.get("count").toString()),ret.get("list"));
        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/modplug")
    public Result modPlug(HttpServletRequest request, HttpServletResponse response){
        try{
            String roomNumber = request.getParameter("room_number");
            String number = request.getParameter("number");
            String typeStr = request.getParameter("type");
            if (roomNumber == null || roomNumber == "" || number == null || number == ""){
                return Result.fail(ResultCodeEnum.MISSPARAM);
            }
            int type = (typeStr == null || typeStr == "") ? 1 : Integer.valueOf(typeStr);

            int effectId = studySeatService.modHasPlug(roomNumber,number,type);
            if (effectId == 0){
                return Result.fail(ResultCodeEnum.ENTITYNOTEXIST);
            }

            return Result.success(new HashMap<String,Object>(){{put("id",effectId);}});
        }catch (Exception e){
            return Result.error();
        }
    }
}
