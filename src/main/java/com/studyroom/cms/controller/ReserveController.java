package com.studyroom.cms.controller;

import com.studyroom.cms.result.Const;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/reserve")
public class ReserveController {



    @RequestMapping("/check")
    public Result check(HttpServletRequest request, HttpServletResponse response, HttpSession session){

        String student = (String) session.getAttribute(Const.CURRENT_USER_STUDENT);
        if(student==null){
            return Result.fail(ResultCodeEnum.NOTLOGIN);
        }
        System.out.println(student);

        return Result.success();
    }
}
