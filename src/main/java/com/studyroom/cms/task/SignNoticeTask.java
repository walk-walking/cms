package com.studyroom.cms.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
//用于签到提醒的定时任务
public class SignNoticeTask {

    //处理流程
    //学生预约信息表 student_order_message
    //假设当前执行时间为13点45分
    //1.从student_order_message中筛选出order_start_time为14:00、is_sign_in为0、is_order_valid为1的预约信息
    //2.根据1拿到的预约信息(预约的自习室及座位、开始自习的时间等)组装邮件内容，同时根据学号获取邮箱
    //3.调用邮件服务发送邮件

    //每天0点45分、1点45分....到20点45分及23点45分运行
    @Scheduled(cron="0 45 0-20,23 * * ?")
    public void process(){

    }
}
