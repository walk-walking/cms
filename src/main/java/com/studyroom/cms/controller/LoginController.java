package com.studyroom.cms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {
    @RequestMapping("/login")
    public Map<String,Object> Login(HttpServletRequest request, HttpServletResponse response){
        try{
            String username=request.getParameter("username");
            String password= request.getParameter("password");
            String type=request.getParameter("selected");
            System.out.println(username);
            System.out.println(password);
            System.out.println(type);
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("code",0);
            map.put("state","后台获取数据成功");
            return map;
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("code",1);
            map.put("state","后台获取数据失败");
            return map;
        }


    }
}
