package com.studyroom.cms.controller;


import com.studyroom.cms.result.Const;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.service.OrderRuleService;
import com.studyroom.cms.service.OrderSeatService;
import com.studyroom.cms.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRuleService orderRuleService;

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private ReserveService reserveService;

    @RequestMapping("/updateOR")
    public Result updateOrderRule(HttpServletRequest request, HttpServletResponse response ,HttpSession session){

        try {
            String roomNumber = request.getParameter("roomNumber");
            String opentime = request.getParameter("opentime");
            String closetime = request.getParameter("closetime");
            String singleMaxOrdertime = request.getParameter("singleOrderTime");

            String numberValue = session.getAttribute(Const.SAVE_ADMIN_LOGIN_MESSAGE_COLUMN).toString();
            if(numberValue==null){
                return Result.fail(ResultCodeEnum.NOT_LOGIN);
            }
            if(roomNumber==null){
                return Result.fail(ResultCodeEnum.UPDATE_RULE_ROOM_NUMBER_EMPTY);
            }
            if(opentime==null){
                return Result.fail(ResultCodeEnum.UPDATE_RULE_START_TIME_EMPTY);
            }
            if(closetime==null){
                return Result.fail(ResultCodeEnum.UPDATE_RULE_END_TIME_EMPTY);
            }
            int singleMaxOrderTimeInt = -1;
            if(singleMaxOrdertime!=null){
                singleMaxOrderTimeInt = Integer.parseInt(singleMaxOrdertime);
            }
            return orderRuleService.updateValidRoom(roomNumber,opentime,closetime,singleMaxOrderTimeInt);
        }catch (Exception e){
            return Result.error();
        }
    }

    @RequestMapping("/updateOS")
    public Result updateOrderSeat(HttpServletRequest request, HttpServletResponse response ,HttpSession session){
        try {
            String numberValue = session.getAttribute(Const.SAVE_ADMIN_LOGIN_MESSAGE_COLUMN).toString();
            if(numberValue==null){
                return Result.fail(ResultCodeEnum.NOT_LOGIN);
            }

            String roomNumber = request.getParameter("roomNumber");
            String seatNumber = request.getParameter("seatNumber");
            int orderStatus = Integer.parseInt(request.getParameter("orderStatus"));

            return orderSeatService.updateSingleOrderRule(roomNumber,seatNumber,orderStatus);



        }catch (Exception e){
            return Result.error();
        }
    }



}
