package com.studyroom.cms.task;

import com.studyroom.cms.entity.OrderRule;
import com.studyroom.cms.entity.OrderSeat;
import com.studyroom.cms.result.customException;
import com.studyroom.cms.service.OrderRuleService;
import com.studyroom.cms.service.OrderSeatService;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
//根据预约规则来打开或关闭座位预约的定时任务
public class OpenOrCloseSeatTask {

    @Autowired
    private OrderRuleService orderRuleService;

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private LoggerUtils loggerUtils;

    //可预约座位信息表 order_seat
    //预约规则表 order_rule

    //每天0点、1点....21点和23点运行
    @Scheduled(cron="0 1 0-21,23 * * ?")
    public void process(){
        //假设当前时间为20220331 12:00
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,+1);
        date = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:00");
        String time = sdf.format(date);
        loggerUtils.info("根据已经生效的预约规则来打开或关闭座位预约的定时任务开始执行，本次操作的数据是可开始预约或者不可预约的时间为" + time);

        try{
            //1.从order_rule中获取到已生效规则中13:00要开放的自习室编号列表ListA
            // 已生效的定义:如果该脚本的运行时间为0点、1点...21点，mtime < 前一天的22点
            //            如果该脚本的运行时间为23点，mtime < 今天的22点
            String effectTime = "";
            Date date2 = new Date();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
            calendar2.add(Calendar.DAY_OF_MONTH,-1);
            Date preDate = calendar2.getTime();
            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd 22:00");
            effectTime = time.equals("00:00") ? sdf2.format(date2) : sdf2.format(preDate);

            List<String>  openRoomList = orderRuleService.getEffectRoomsByTime(time, OrderRule.ORDER_TIME_TYPE_OPEN,effectTime);
            loggerUtils.info(time + "要开放的自习室：" + openRoomList.toString());
            if (!openRoomList.isEmpty()){
                //2.order_seat中自习室编号属于ListA的座位状态修改为可预约
                orderSeatService.modSeatStatusByRoom(openRoomList, OrderSeat.ORDER_STATUS_OPEN);
            }

            //3.从order_rule中获取到已生效规则中13:00要关闭的自习室编号集合ListB
            List<String>  closeRoomList = orderRuleService.getEffectRoomsByTime(time, OrderRule.ORDER_TIME_TYPE_CLOSE,effectTime);
            loggerUtils.info(time + "要关闭的自习室：" + closeRoomList.toString());
            if (!closeRoomList.isEmpty()){
                //4.order_seat中自习室编号属于ListB的座位状态修改为未开放
                orderSeatService.modSeatStatusByRoom(closeRoomList, OrderSeat.ORDER_STATUS_NOTOPEN);
            }

        }catch (customException e){
            loggerUtils.error(e.getErrMsg());
        }catch (Exception e){
            loggerUtils.error(e.getMessage());
        }

    }
}
