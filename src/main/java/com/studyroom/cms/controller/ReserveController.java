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
                return Result.fail(ResultCodeEnum.MISS_PARAM);
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
                return Result.fail(ResultCodeEnum.MISS_PARAM);
            }

            return reserveService.cancelLogic(orderNo,studentSessionNo);

        }catch (Exception e){
            return Result.error();
        }
    }
    @RequestMapping("/queryReserveList")
    public Result queryReserveList(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        try{
            Object numberValue = session.getAttribute(Const.SAVE_STUDENT_LOGIN_MESSAGE_COLUMN);
            String studentSessionNo = numberValue.toString().split(":")[1];
            String studentNumber = request.getParameter("studentNumber");
            String pageStr = request.getParameter("page");
            String pageSizeStr = request.getParameter("limit");


            int page = (pageStr != null &&  pageStr != "") ? Integer.parseInt(pageStr) : 1;
            int pageSize = (pageSizeStr != null &&  pageSizeStr != "") ? Integer.parseInt(pageSizeStr) : 10;
            if(page<=0 || pageSize<=0){
                return Result.fail(ResultCodeEnum.PAGE_OR_PAGESIZE_LESS_THAN_ZERO);
            }
            return reserveService.queryReserverListLogic(studentNumber,studentSessionNo,page,pageSize);

        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/queryUnReserveList")
    public Result queryUnReserveList(HttpServletRequest request, HttpServletResponse response){
        try {
            String studyRoomNumber = request.getParameter("studyRoomNumber");
            String pageStr = request.getParameter("page");
            String pageSizeStr = request.getParameter("limit");
            int page = (pageStr != null &&  pageStr != "") ? Integer.parseInt(pageStr) : 1;
            int pageSize = (pageSizeStr != null &&  pageSizeStr != "") ? Integer.parseInt(pageSizeStr) : 10;
            if(page<=0 || pageSize<=0){
                return Result.fail(ResultCodeEnum.PAGE_OR_PAGESIZE_LESS_THAN_ZERO);
            }

            return reserveService.queryUnReserverListLogic(studyRoomNumber,page,pageSize);

        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/getRoomList")
    public Result getRoomListLogic(HttpServletRequest request, HttpServletResponse response,HttpSession session){
        try {

            String pageStr = request.getParameter("page");
            String pageSizeStr = request.getParameter("limit");
            int page = (pageStr != null && pageStr != "") ? Integer.parseInt(pageStr) : 1;
            int pageSize = (pageSizeStr != null && pageSizeStr != "") ? Integer.parseInt(pageSizeStr) : 10;
            if (page <= 0 || pageSize <= 0) {
                return Result.fail(ResultCodeEnum.PAGE_OR_PAGESIZE_LESS_THAN_ZERO);
            }
            Object numberValue = session.getAttribute(Const.SAVE_STUDENT_LOGIN_MESSAGE_COLUMN);
            if(numberValue==null){
                return Result.fail(ResultCodeEnum.NOT_LOGIN);
            }

            return reserveService.getRoomListLogic(page,pageSize);
        }catch (Exception e){
            return Result.error();
        }

    }



}
