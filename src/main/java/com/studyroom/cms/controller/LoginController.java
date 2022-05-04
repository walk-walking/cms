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
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AdministratorService adminService;
    @Autowired
    private StudentService studentService;

    @RequestMapping("/login")
    public Result Login(HttpServletRequest request, HttpServletResponse response){
        try{
            HashMap<String,String> needParams = new HashMap<>();
            needParams.put("type",request.getParameter("type"));
            needParams.put("username",request.getParameter("username"));
            needParams.put("password",request.getParameter("password"));

            for (Object value : needParams.values()){
                if (value == null || value == ""){
                    return Result.fail(ResultCodeEnum.MISSPARAM);
                }
            }

            String pw = Integer.parseInt(needParams.get("type")) == 1 ? adminService.getPassword(needParams.get("username")) : studentService.getPassword(needParams.get("username"));
            if (needParams.get("password").equals(pw)){
                return Result.success();
            }else if(pw == ""){
                return Result.fail(ResultCodeEnum.USERNOTEXIST);
            }else{
                //密码不相等
                return Result.fail(ResultCodeEnum.LOGINFAIL);
            }
        }catch (Exception e){
            //数据库执行异常
            return Result.error();
        }
    }
}
