package com.studyroom.cms.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
//每天22:00-23:59不可通过预约使用自习室  0点开放的教室，在前一天的23点以后可进行预约
//用于更新每天可预约座位信息的定时任务
public class UpdateOrderSeatTask {

    //处理流程
    //可预约座位信息表 order_seat
    //预约规则表 order_rule
    //座位信息表 study_seat
    //1.将order_seat中所有可预约、已预约的座位状态都设置为待预约状态 [重置]
    //2.从order_rule中拿到被设置预约规则的自习室编号List(eg:JA101,JA103)
    //3.删除order_seat中未设置预约规则的座位(eg:删除JA102下的所有座位)
    //4.从order_rule中筛选出mtime为24小时内的规则
    //5.将4筛选出的规则内容更新到order_seat
    //6.从study_seat中筛选出mtime为24小时内且不可用的座位信息
    //7.删除order_seat中对应6得到的座位信息的座位

    //每天22点运行
    @Scheduled(cron="0 0 22 * * ？")
    public void process(){

    }
}

