package com.studyroom.cms.controller;

import com.studyroom.cms.result.Const;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.service.ReserveService;
import com.studyroom.cms.utils.EmailSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.mail.MessagingException;
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
    public Result check(HttpServletRequest request, HttpServletResponse response) throws MessagingException {
        EmailSend emailSend = new EmailSend();
        emailSend.EmailSendLogic_single("742906522@qq.com","自习室签到提醒","距离JA201的自习室签到还有15分钟");
        return Result.success();
    }

    @RequestMapping("/order")
    public Result order(HttpServletRequest request, HttpServletResponse response,HttpSession session){
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
            Object numberValue = session.getAttribute(Const.SAVE_STUDENT_LOGIN_MESSAGE_COLUMN);
            String studentSessionNo = numberValue.toString().split(":")[1];

            return reserveService.OrderLogic(studentNumber,roomNumber,seatNumber,st,et,studentSessionNo);


        } catch (Exception e){
            return Result.error();
        }

    }

    @RequestMapping("/signIn")
    public Result signIn(HttpServletRequest request, HttpServletResponse response,HttpSession session){

        try {
            Object numberValue = session.getAttribute(Const.SAVE_STUDENT_LOGIN_MESSAGE_COLUMN);
            String studentSessionNo = numberValue.toString().split(":")[1];
            String studentNumber = request.getParameter("studentNumber");
            String roomNumber = request.getParameter("room_number");
            String seatNumber = request.getParameter("seat_number");



            return reserveService.signInLogic(studentNumber,roomNumber,seatNumber,studentSessionNo);

        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/cancel")
    public Result cancel(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        try {
            Object numberValue = session.getAttribute(Const.SAVE_STUDENT_LOGIN_MESSAGE_COLUMN);
            String studentSessionNo = numberValue.toString().split(":")[1];
            String orderNo = request.getParameter("orderNo");
            if(orderNo==null){
                return Result.fail(ResultCodeEnum.MISSPARAM);
            }

            return reserveService.cancelLogic(orderNo,studentSessionNo);

        }catch (Exception e){
            return Result.error();
        }
    }

}
