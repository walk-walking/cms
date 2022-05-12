package com.studyroom.cms.task;

import com.studyroom.cms.result.customException;
import com.studyroom.cms.service.OrderSeatService;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@EnableScheduling
//将即将到期的已预约座位、未签到的已预约座位进行释放
public class ReleaseSeatTask {

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private LoggerUtils loggerUtils;


    //可预约座位信息表 order_seat
    //学生预约信息表 student_order_message

    //每天0点30分、1点分....21点30分运行
//    @Scheduled(cron="0 30 0-21 * * ?")   //TODO 删除注释
    public void process(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,+1);
        date = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String time = sdf.format(date);
        System.out.println(time);

        try{

        }catch (customException e){
            loggerUtils.error(e.getErrMsg());
        }catch (Exception e){
            loggerUtils.error(e.getMessage());
        }

    }
}
