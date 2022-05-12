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
                    return Result.fail(ResultCodeEnum.MISS_PARAM);
                }
            }

            int type = Integer.parseInt(needParams.get("type"));
            String pw = (type == 1) ? adminService.getPassword(needParams.get("username")) : studentService.getPassword(needParams.get("username"));
            if (needParams.get("password").equals(pw)){

                //set session Attribute
                if (type == 1){
                    session.setAttribute(Const.SAVE_ADMIN_LOGIN_MESSAGE_COLUMN,Const.CURRENT_ADMIN_NUMBER_PREFIX + needParams.get("username"));
                }else{
                    session.setAttribute(Const.SAVE_STUDENT_LOGIN_MESSAGE_COLUMN,Const.CURRENT_STUDENT_NUMBER_PREFIX + needParams.get("username"));
                }

                return Result.success();
            }else if(pw == ""){
                return Result.fail(ResultCodeEnum.USER_NOT_EXIST);
            }else{
                //密码不相等
                return Result.fail(ResultCodeEnum.LOGIN_FAIL);
            }
        }catch (Exception e){
            //数据库执行异常
            return Result.error();
        }
    }

    @RequestMapping("/logout")
    public Result Logout(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        String typeStr = request.getParameter("type");
        if (typeStr == null || typeStr == ""){
            return Result.fail(ResultCodeEnum.MISS_PARAM);
        }

        int type = Integer.parseInt(typeStr);
        String column = (type == 1) ? Const.SAVE_ADMIN_LOGIN_MESSAGE_COLUMN : Const.SAVE_STUDENT_LOGIN_MESSAGE_COLUMN;
        session.removeAttribute(column);
        Object numberValue =  session.getAttribute(column);
        if (numberValue == null){
            return Result.success();
        }else{
            return Result.fail(ResultCodeEnum.LOGOUT_FAIL);
        }
    }

}
