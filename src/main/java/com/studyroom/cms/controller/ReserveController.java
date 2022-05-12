package com.studyroom.cms.controller;

import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;



    @RequestMapping("/check")
    public Result check(HttpServletRequest request, HttpServletResponse response){
        return Result.success();
    }

    @RequestMapping("/order")
    public Result order(HttpServletRequest request, HttpServletResponse response){
        try {
            //根据座位编号,自习室开始时间,自习室结束时间进行预约


            String studentNumber = request.getParameter("studentNumber");
            String roomNumber = request.getParameter("room_number");
            String seatNumber = request.getParameter("seat_number");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");

            if(studentNumber==null || roomNumber==null || seatNumber==null || startTime==null || endTime==null){
                return Result.fail(ResultCodeEnum.MISSPARAM);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date st = sdf.parse(startTime);
            Date et = sdf.parse(endTime);

            return reserveService.OrderLogic(studentNumber,roomNumber,seatNumber,st,et);


        } catch (Exception e){
            return Result.error();
        }

    }



}
