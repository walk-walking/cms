package com.studyroom.cms.controller;

import com.studyroom.cms.entity.Student;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
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
            needParams.put("number",request.getParameter("number"));
            needParams.put("sex",request.getParameter("sex"));
            needParams.put("name",request.getParameter("name"));
            needParams.put("campus",request.getParameter("campus"));
            needParams.put("email",request.getParameter("email"));
            needParams.put("finish_year",request.getParameter("finish_year"));
            needParams.put("password",request.getParameter("password"));

            for (Object value : needParams.values()){
                if (value == null || value == ""){
                    return Result.fail(ResultCodeEnum.MISS_PARAM);
                }
            }

            int type = Integer.parseInt(needParams.get("type"));
            needParams.remove("type");

            //type 1-add 2-修改
            if(type == 1){
                int id = studentService.addOne(needParams);
                if (id == 0){
                    return Result.fail(ResultCodeEnum.USER_HAS_EXIST);
                }else{
                    return Result.success(new HashMap<String,Object>(){{put("id",id);}});
                }
            }else{
                int effectRow = studentService.modOne(needParams);
                if (effectRow == 0) {
                    return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
                }else{
                    return Result.success(new HashMap<String,Object>(){{put("id",needParams.get("number"));}});
                }
            }
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

            int effectId = studentService.delOne(number);
            if (effectId == 0) {
                return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
            }else{
                return Result.success(new HashMap<String,Object>(){{put("id",effectId);}});
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
            int page = (pageStr != null &&  pageStr != "") ? Integer.parseInt(pageStr) : 1;
            int pageSize = (pageSizeStr != null &&  pageSizeStr != "") ? Integer.parseInt(pageSizeStr) : 10;
            if (page <= 0 || pageSize <= 0){
                return Result.fail(ResultCodeEnum.WRONG_PARAM_VALUE);
            }

            HashMap<String,Object> ret = studentService.getList(page,pageSize);
            return Result.listSuccess(Integer.valueOf(ret.get("count").toString()),ret.get("list"));
        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/modpwd")
    public Result modPwd(HttpServletRequest request, HttpServletResponse respons){
        try{
            HashMap<String,String> needParams = new HashMap<>();
            needParams.put("number",request.getParameter("number"));
            needParams.put("password",request.getParameter("password"));
            needParams.put("origin_password",request.getParameter("origin_password"));

            for (String value : needParams.values()){
                if (value == null || value == ""){
                    return Result.fail(ResultCodeEnum.MISS_PARAM);
                }
            }

            Student stu = studentService.getOneByNumber(needParams.get("number"));
            if (stu == null){
                return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
            }

            if(!stu.getPassword().equals(needParams.get("origin_password"))){
                return Result.fail(ResultCodeEnum.USER_ORIGIN_PASSWORD_IS_WRONG);
            }

            int effectId = studentService.modOneColumn("password",needParams.get("password"),needParams.get("number"));
            if (effectId == 0) {
                return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
            }else{
                return Result.success(new HashMap<String,Object>(){{put("id",effectId);}});
            }

        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/modemail")
    public Result modEmail(HttpServletRequest request, HttpServletResponse respons){
        try{
            String number = request.getParameter("number");
            String email = request.getParameter("email");
            if (number == null || number == "" || email == null || email == ""){
                return Result.fail(ResultCodeEnum.MISS_PARAM);
            }

            int effectId = studentService.modOneColumn("email",email,number);
            if (effectId == 0) {
                return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
            }else{
                return Result.success(new HashMap<String,Object>(){{put("id",effectId);}});
            }

        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/info")
    public Result Info(HttpServletRequest request, HttpServletResponse respons){
        try{
            String number = request.getParameter("number");
            if (number == null || number == ""){
                return Result.fail(ResultCodeEnum.MISS_PARAM);
            }

            Student stu = studentService.getOneByNumber(number);
            if (stu == null){
                return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
            }

            HashMap<String,Object> row = new HashMap<>();
            row.put("number",stu.getNumber());
            row.put("name",stu.getName());
            row.put("sex",stu.getSex());
            row.put("campus",stu.getCampus());
            row.put("email",stu.getEmail());
            row.put("finish_year",stu.getFinish_year());

            return Result.success(row);
        }catch (Exception e){
            return Result.error();
        }
    }
}
