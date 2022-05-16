package com.studyroom.cms.task;


import com.studyroom.cms.entity.StudentOrderMessage;
import com.studyroom.cms.result.customException;
import com.studyroom.cms.service.StudentOrderMessageService;
import com.studyroom.cms.service.StudentService;
import com.studyroom.cms.utils.EmailSend;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling
//用于未签到提醒的定时任务
public class NotSignNoticeTask {

    @Autowired
    private StudentOrderMessageService studentOrderMessageService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private EmailSend emailSendUtils;

    //学生预约信息表 student_order_message
    //学生信息表 student

    //每天0点10分、1点10分....到21点10分运行
    @Scheduled(cron="0 10 0-21 * * ?")
    public void process(){
        //假设当前时间为20220511 13:10
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String time = sdf.format(date);
        loggerUtils.info("超过预约开始时间10分钟仍未签到的签到提醒任务开始执行，本次提醒的是开始时间为" + time + "的预约");

        try{
            //1.从student_order_message中筛选出已经开始但未签到的预约订单信息,
            //  筛选条件为order_start_time为13:00、is_sign_in为0、is_order_valid为1
            List<StudentOrderMessage> notSignInAndHasStartOrderMessage = studentOrderMessageService.getNotSignInAndHasStartOrderMessage(time);
            if (!notSignInAndHasStartOrderMessage.isEmpty()){
                //2.根据1拿到的预约订单信息(预约的自习室及座位、开始自习的时间等)组装邮件内容
                HashMap<String,String> mailContent = new HashMap<>();
                for (int i = 0; i < notSignInAndHasStartOrderMessage.size(); ++i){
                    StudentOrderMessage orderMessage = notSignInAndHasStartOrderMessage.get(i);
                    String  noticeMessage = String.format("你预约的自习座位已经生效，请在%s前到达%s教室的%s号座位进行签到。",
                            time.replace("00:00","15:00"),orderMessage.getRoomNumber(),orderMessage.getSeatNumber());
                    mailContent.put(orderMessage.getStudentNumber(),noticeMessage);
                    loggerUtils.info("学生" + orderMessage.getStudentNumber() + "应收到签到提醒邮件，内容如下：" + noticeMessage);
                }

                //3.根据学号获取邮箱
                HashMap<String,String>  emailMap = studentService.getEmailByNumber(new ArrayList<>(mailContent.keySet()));
                if (!emailMap.isEmpty()){
                    //4.调用邮件服务发送邮件
                    for(String stuNumber : emailMap.keySet()){
                        if (mailContent.containsKey(stuNumber)){
                            emailSendUtils.EmailSendLogic_single(emailMap.get(stuNumber), "自习室未签到提醒",mailContent.get(stuNumber));
                            loggerUtils.info("学生" + stuNumber + "的自习室未签到提醒邮件发送成功");
                        }
                    }
                }
            }

        }catch (customException e){
            loggerUtils.error(e.getErrMsg());
        }catch (Exception e){
            loggerUtils.error(e.getMessage());
        }
    }
}
