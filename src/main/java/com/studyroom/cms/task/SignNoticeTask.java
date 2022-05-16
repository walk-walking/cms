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
//用于签到提醒的定时任务
public class SignNoticeTask {
    @Autowired
    private StudentOrderMessageService studentOrderMessageService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private EmailSend emailSendUtils;

    //学生预约订单信息表 student_order_message
    //学生信息表 student

    //每天0点45分、1点45分....到20点45分及23点45分运行
    @Scheduled(cron="0 45 0-20,23 * * ?")
    public void process(){
        //假设当前时间为20220511 13:45
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,+1);
        date = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String time = sdf.format(date);
        loggerUtils.info("提前15分钟的签到提醒任务开始执行，本次提醒的是开始时间为" + time + "的预约");

        try{
            //1.从student_order_message中筛选出即将开始但未签到的预约订单信息,
            //  筛选条件为order_start_time为14:00、is_sign_in为0、is_order_valid为1
            List<StudentOrderMessage> notSignInAndNotStartOrderMessage = studentOrderMessageService.getNotSignInAndNotStartOrderMessage(time);
            if (!notSignInAndNotStartOrderMessage.isEmpty()){
                //2.根据1拿到的预约订单信息(预约的自习室及座位、开始自习的时间等)组装邮件内容
                HashMap<String,String>  mailContent = new HashMap<>();
                SimpleDateFormat sdf2 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < notSignInAndNotStartOrderMessage.size(); ++i){
                    StudentOrderMessage orderMessage = notSignInAndNotStartOrderMessage.get(i);
                    String  noticeMessage = String.format("你已预约%s教室的%s号座位，请到达该位置进行签到。预约生效时间为%s，结束时间为%s",
                            orderMessage.getRoomNumber(),orderMessage.getSeatNumber(),
                            sdf2.format(orderMessage.getOrderStartTime()),sdf2.format(orderMessage.getOrderEndTime()));
                    mailContent.put(orderMessage.getStudentNumber(),noticeMessage);
                    loggerUtils.info("学生" + orderMessage.getStudentNumber() + "应收到签到提醒邮件，内容如下：" + noticeMessage);
                }

                //3.根据学号获取邮箱
                HashMap<String,String>  emailMap = studentService.getEmailByNumber(new ArrayList<>(mailContent.keySet()));
                if (!emailMap.isEmpty()){
                    //4.调用邮件服务发送邮件
                    for(String stuNumber : emailMap.keySet()){
                        if (mailContent.containsKey(stuNumber)){
//                            emailSendUtils.EmailSendLogic_single(emailMap.get(stuNumber), "自习室签到提醒",mailContent.get(stuNumber));
                            loggerUtils.info("学生" + stuNumber + "的自习室签到提醒邮件发送成功");
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
