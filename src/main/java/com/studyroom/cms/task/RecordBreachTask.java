package com.studyroom.cms.task;

import com.studyroom.cms.entity.StudentOrderMessage;
import com.studyroom.cms.result.customException;
import com.studyroom.cms.service.BreachMessageService;
import com.studyroom.cms.service.StudentOrderMessageService;
import com.studyroom.cms.service.StudentService;
import com.studyroom.cms.utils.EmailSend;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@EnableScheduling
//用于记录违约的定时任务
public class RecordBreachTask {

    @Autowired
    private StudentOrderMessageService studentOrderMessageService;

    @Autowired
    private BreachMessageService breachMessageService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private EmailSend emailSendUtils;

    //学生预约信息表 student_order_message
    //学生违法信息表 breach_message

    //每天0点15分、1点15分...到21:15分运行
    @Scheduled(cron="0 15 0-21 * * ?")
    public void process(){
        //假设当前执行时间为20220512 13:15
        Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String time = sdf.format(date);
        loggerUtils.info("超过预约开始时间15分钟仍未签到的自动取消预约任务开始执行，本次取消的是开始时间为" + time + "的预约");

        try{
            //1.从student_order_message中筛选出已经开始但未签到的预约订单信息,
            //  筛选条件为order_start_time为13:00、is_sign_in为0、is_order_valid为1
            List<StudentOrderMessage> notSignInAndHasStartOrderMessage = studentOrderMessageService.getNotSignInAndHasStartOrderMessage(time);
            if (!notSignInAndHasStartOrderMessage.isEmpty()){
                //2.将1中拿到的预约Id和学生学号写入breach_message
                List<HashMap<String,String>> breachMessage = new ArrayList<>();
                for (int i = 0 ;i < notSignInAndHasStartOrderMessage.size(); ++i){
                    StudentOrderMessage orderMessage = notSignInAndHasStartOrderMessage.get(i);
                    HashMap<String,String>  one = new HashMap<String,String>(){
                        {
                            put("student_number",orderMessage.getStudentNumber());
                            put("breach_time",sdf.format(orderMessage.getOrderStartTime()));
                            put("order_id",String.valueOf(orderMessage.getId()));
                        }
                    };
                    breachMessage.add(one);
                }
                breachMessageService.batchAdd(breachMessage);   //暂不考虑单次写入条数过多的问题  有时间可改成分批写入

                //3.根据1拿到的预约订单信息(预约的自习室及座位、开始自习的时间等)组装邮件内容
                HashMap<String,String> mailContent = new HashMap<>();
                for (int i = 0; i < notSignInAndHasStartOrderMessage.size(); ++i){
                    StudentOrderMessage orderMessage = notSignInAndHasStartOrderMessage.get(i);
                    String  noticeMessage = String.format("你预约的%s教室的%s号座位超过预约开始时间（%s）15分钟仍未签到，系统将自动释放预约的座位，且将本次未签到记一次违约",
                            orderMessage.getRoomNumber(),orderMessage.getSeatNumber(),time);
                    mailContent.put(orderMessage.getStudentNumber(),noticeMessage);
                    loggerUtils.info("学生" + orderMessage.getStudentNumber() + "应收到系统自动释放座位的提醒邮件，内容如下：" + noticeMessage);
                }

                //4.根据学号获取邮箱
                HashMap<String,String>  emailMap = studentService.getEmailByNumber(new ArrayList<>(mailContent.keySet()));
                if (!emailMap.isEmpty()){
                    //5.调用邮件服务发送邮件
                    for(String stuNumber : emailMap.keySet()){
                        if (mailContent.containsKey(stuNumber)){
//                            emailSendUtils.EmailSendLogic_single(emailMap.get(stuNumber), "系统自动释放座位的提醒",mailContent.get(stuNumber));
                            loggerUtils.info("学生" + stuNumber + "的系统自动释放座位的提醒邮件发送成功");
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
