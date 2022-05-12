package com.studyroom.cms.controller;

import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.service.StudyRoomService;
import com.studyroom.cms.service.StudySeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/studyroom")
public class StudyRoomController {

    @Autowired
    private StudyRoomService studyRoomService;

    @Autowired
    private StudySeatService studySeatService;

    @RequestMapping("/add")
    public Result add(HttpServletRequest request, HttpServletResponse response){
        try{
            HashMap<String,String> needParams = new HashMap<>();
            needParams.put("number",request.getParameter("number"));
            needParams.put("campus",request.getParameter("campus"));
            needParams.put("building",request.getParameter("building"));
            needParams.put("seat_count",request.getParameter("seat_count"));

            for (Object value : needParams.values()){
                if (value == null || value == ""){
                    return Result.fail(ResultCodeEnum.MISS_PARAM);
                }
            }

            int id = studyRoomService.addOne(needParams);
            if (id == 0){
                return Result.fail(ResultCodeEnum.ENTITY_HAS_EXIST);
            }else{
                //添加对应的座位
                int seatCount = Integer.valueOf(needParams.get("seat_count"));
                if(seatCount > 0){
                    //若添加失败 不在接口中展示  后续可手动添加
                    studySeatService.batchAdd(needParams.get("number"),needParams.get("building"),seatCount);
                }
                return Result.success(new HashMap<String,Object>(){{put("id",id);}});
            }
        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/list")
    public Result List(HttpServletRequest request, HttpServletResponse response){
        try{
            String pageStr = request.getParameter("page");
            String pageSizeStr = request.getParameter("limit");
            HashMap<String,String> condStr = new HashMap<>();
            condStr.put("campus",request.getParameter("campus"));
            condStr.put("building",request.getParameter("building"));
            condStr.put("number",request.getParameter("number"));

            int page = (pageStr != null &&  pageStr != "") ? Integer.parseInt(pageStr) : 1;
            int pageSize = (pageSizeStr != null &&  pageSizeStr != "") ? Integer.parseInt(pageSizeStr) : 10;
            HashMap<String,Object> condition = new HashMap<>();
            for (String key : condStr.keySet()){
                String value = condStr.get(key);
                if (value == null || value == ""){
                    continue;
                }

                condition.put(key,value);
            }

            HashMap<String,Object> ret = studyRoomService.getList(page,pageSize,condition);
            return Result.listSuccess(Integer.valueOf(ret.get("count").toString()),ret.get("list"));
        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/delete")
    public Result Delete(HttpServletRequest request, HttpServletResponse response){
        try{
            String number = request.getParameter("number");
            if (number == null || number == ""){
                return Result.fail(ResultCodeEnum.MISS_PARAM);
            }

            int effectId = studyRoomService.delOne(number);
            if (effectId == 0) {
                return Result.fail(ResultCodeEnum.ENTITY_NOT_EXIST);
            }else{
                studySeatService.deleteByRoomNumber(number);
                return Result.success(new HashMap<String,Object>(){{put("id",effectId);}});
            }
        }catch (Exception e){
            return Result.error();
        }
    }

}
