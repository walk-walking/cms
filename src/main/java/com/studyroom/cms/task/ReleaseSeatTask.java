package com.studyroom.cms.task;

import com.studyroom.cms.result.customException;
import com.studyroom.cms.service.OrderSeatService;
import com.studyroom.cms.service.StudentOrderMessageService;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@EnableScheduling
//将即将到期的已预约座位进行释放
public class ReleaseSeatTask {

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private StudentOrderMessageService studentOrderMessageService;

    @Autowired
    private LoggerUtils loggerUtils;

    //可预约座位信息表 order_seat
    //学生预约信息表 student_order_message

    //每天0点30分、1点分....21点30分运行
//    @Scheduled(cron="0 30 0-21 * * ?")   //TODO 删除注释
    public void process(){
        //假设当前时间为20220511 14:30
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,+1);
        date = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String time = sdf.format(date);
//        System.out.println(time);

        try{
            //1.从student_order_message中筛选出预约结束时间为15:00、预约有效、且已进行签到的预约记录，
            //  再从预约记录中获取到即将被释放的座位信息(自习室编号和座位编号)ListA
            List<HashMap<String ,String>> expiringSeats = studentOrderMessageService.getExpiringSeat(time);
//            System.out.println(expiringSeats);
            if (!expiringSeats.isEmpty()){
                //2.order_seat中座位信息属于ListA的座位状态修改为可预约
                orderSeatService.releaseExpiringSeat(expiringSeats);
            }
        }catch (customException e){
            loggerUtils.error(e.getErrMsg());
        }catch (Exception e){
            loggerUtils.error(e.getMessage());
        }

    }
}
