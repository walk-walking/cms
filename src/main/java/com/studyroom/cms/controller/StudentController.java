package com.studyroom.cms.controller;

import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.service.AdministratorService;
import com.studyroom.cms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/addormod")
    public Result AddOrMod(HttpServletRequest request, HttpServletResponse response){
        try{
            HashMap<String,String> needParams = new HashMap<>();
            needParams.put("type",request.getParameter("type"));
            needParams.put("stuNo",request.getParameter("stuno"));
            needParams.put("name",request.getParameter("name"));
            needParams.put("campus",request.getParameter("campus"));
            needParams.put("finish_year",request.getParameter("finish_year"));
            needParams.put("password",request.getParameter("password"));

            for (Object value : needParams.values()){
                if (value == null || value == ""){
                    return Result.fail(ResultCodeEnum.MISSPARAM);
                }
            }

            int type = Integer.parseInt(needParams.get("type"));
            needParams.remove("type");

            //type 1-add 2-修改
            if(type == 1){
                int id = studentService.addOne(needParams);
                if (id == 0){
                    return Result.fail(ResultCodeEnum.USERHASEXIST);
                }else{
                    return Result.success(new HashMap<String,Object>(){{put("id",id);}});
                }
            }else{
                int effectRow = studentService.modOne(needParams);
                if (effectRow == 0) {
                    return Result.fail(ResultCodeEnum.USERNOTEXIST);
                }else{
                    return Result.success(new HashMap<String,Object>(){{put("id",needParams.get("stuNo"));}});
                }
            }
        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/delete")
    public Result Delete(HttpServletRequest request, HttpServletResponse response){
        try{
            String stuNo = request.getParameter("stuno");
            if (stuNo == null || stuNo == ""){
                return Result.fail(ResultCodeEnum.MISSPARAM);
            }

            int effectRow = studentService.delOne(stuNo);
            if (effectRow == 0) {
                return Result.fail(ResultCodeEnum.USERNOTEXIST);
            }else{
                return Result.success(new HashMap<String,Object>(){{put("id",stuNo);}});
            }
        }catch (Exception e){
            return Result.error();
        }
    }
}
