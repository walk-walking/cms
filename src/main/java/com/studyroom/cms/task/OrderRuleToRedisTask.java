package com.studyroom.cms.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
//将预约规则写入到redis中，后续通过ReleaseSeatTask任务来开放座位的预约
public class OrderRuleToRedisTask {

    //处理流程
    //预约规则表 order_rule
    //当前时间为20220330
    //存放规则的redis key:  数据结构为集合
    // - open_classroom_20220331_06:00 [20220331 07:00时间需要执行开放的自习室]  注意！！！ 小时-1 日期+1  0点就要开放的情况需要注意
    // - close_classroom_20220331_21:00 [20220331 22:00时间需要执行关闭的自习室] 注意！！！ 小时-1 日期+1
    //存放规则的redis value: 自习室编号集合
    //
    //1.从order_rule获取每个自习室的开放关闭时间
    //2.按照redis key的格式生成每个自习室对应的key
    //3.将自习室编号添加到对应的key的集合中去

    //每天22点运行
    @Scheduled(cron="0 0 22 * * ？")
    public void process(){

    }
}
