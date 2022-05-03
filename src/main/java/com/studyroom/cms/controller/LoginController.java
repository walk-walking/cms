package com.studyroom.cms.controller;

import com.studyroom.cms.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private AdministratorService adminService;

    @RequestMapping("/login")
    public Map<String,Object> Login(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> ret=new HashMap<>();
        try{
            String username=request.getParameter("username");
            String password= request.getParameter("password");
            String type=request.getParameter("selected");
            if (type == null){
                type = "2"; //默认是学生登录
            }

            String state = "登录失败";
            if (Integer.parseInt(type) == 1){
                String pw = adminService.getPassword(username);
                if (password.equals(pw)){
                    state = "登录成功";
                }
            }else{
                //TODO  学生登录
            }

            ret.put("code",0);
            ret.put("state",state);
            return ret;
        }catch (Exception e){
            e.printStackTrace();
            ret.put("code",1);
            ret.put("state","后台获取数据失败");
            return ret;
        }

    }
}
