package com.studyroom.cms.controller;

import com.studyroom.cms.result.Result;
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
        return Result.success();
    }
}
