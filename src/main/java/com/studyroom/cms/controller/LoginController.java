package com.studyroom.cms.controller;

import com.studyroom.cms.result.Const;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.service.AdministratorService;
import com.studyroom.cms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AdministratorService adminService;
    @Autowired
    private StudentService studentService;

    @RequestMapping("/login")
    public Result Login(HttpServletRequest request, HttpServletResponse response, HttpSession session){
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

                //set session Attribute
                if(Integer.parseInt(needParams.get("type")) == 1) {
                    session.setAttribute(Const.CURRENT_USER_ADMIN, Const.CURRENT_USER_ADMIN);
                }
                else{
                    session.setAttribute(Const.CURRENT_USER_STUDENT, Const.CURRENT_USER_STUDENT);
                }

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
    //登录测试模块,可删
    @RequestMapping("/check")
    public Result check(HttpServletRequest request, HttpServletResponse response, HttpSession session){

        String admin = (String) session.getAttribute(Const.CURRENT_USER_ADMIN);
        if(admin==null){
            return Result.fail(ResultCodeEnum.NOTLOGIN);
        }
        System.out.println(admin);

        return Result.success();
    }

}
