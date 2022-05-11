package com.studyroom.cms.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
//用于记录违约的定时任务
public class RecordBreachTask {

    //处理流程
    //学生预约信息表 student_order_message
    //学生违法信息表 breach_message
    //假设当前执行时间为13点15分
    //1.从student_order_message中筛选出order_start_time为13:00、is_sign_in为0、is_order_valid为1的预约信息
    //2.将1中拿到的预约Id和学生学号写入breach_message
    //3.根据1拿到的预约信息(预约的自习室及座位、开始自习的时间等)组装邮件内容，同时根据学号获取邮箱
    //4.调用邮件服务发送邮件

    @Scheduled(cron="0 15 0-21 * * ?")    //每天0点15分、1点15分...到21:15分运行
    public void process(){

    }
}
