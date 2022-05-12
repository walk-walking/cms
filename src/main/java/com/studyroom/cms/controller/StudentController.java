package com.studyroom.cms.controller;

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
            HashMap<String,Object> ret = studentService.getList(page,pageSize);
            return Result.listSuccess(Integer.valueOf(ret.get("count").toString()),ret.get("list"));
        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/modpwd")
    public Result modPwd(HttpServletRequest request, HttpServletResponse respons){
        try{
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username == null || username == "" || password == null || password == ""){
                return Result.fail(ResultCodeEnum.MISS_PARAM);
            }

            int effectId = studentService.modOneColumn("password",password,username);
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
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            if (username == null || username == "" || email == null || email == ""){
                return Result.fail(ResultCodeEnum.MISS_PARAM);
            }

            int effectId = studentService.modOneColumn("email",email,username);
            if (effectId == 0) {
                return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
            }else{
                return Result.success(new HashMap<String,Object>(){{put("id",effectId);}});
            }

        }catch (Exception e){
            return Result.error();
        }
    }
}
