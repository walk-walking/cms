package com.studyroom.cms.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
//用于更新座位状态的定时任务
public class UpdateSeatStatusTask {

    //处理流程
    //假设当前时间为20220331 12:00
    //可预约座位信息表 order_seat
    //1.从open_classroom_20220331_12:00中获取到要开放的自习室编号集合ListA
    //2.order_seat中自习室编号属于ListA的座位状态修改为可预约
    //3.从close_classroom_20220331_12:00中获取到要关闭的自习室编号集合ListB
    //4.order_seat中自习室编号属于ListB的座位状态修改为待预约
    //5.从open_seat_20220331_12:00中获取到要释放的自习室编号_座位编号集合ListC
    //6.order_seat中自习室编号_座位编号属于ListC的座位状态修改为可预约

    //每天0点、1点....21点和23点运行
    @Scheduled(cron="0 1 0-21,23 * * ？")
    public void process(){

    }
}
