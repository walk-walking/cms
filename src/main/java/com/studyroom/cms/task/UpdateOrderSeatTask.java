package com.studyroom.cms.task;

import com.studyroom.cms.result.customException;
import com.studyroom.cms.service.OrderRuleService;
import com.studyroom.cms.service.OrderSeatService;
import com.studyroom.cms.service.StudySeatService;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@EnableScheduling
//每天22:00-23:59不可通过预约使用自习室  0点开放的教室，在前一天的23点以后可进行预约
//用于更新每天可预约座位信息的定时任务
public class UpdateOrderSeatTask {

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private OrderRuleService orderRuleService;

    @Autowired
    private StudySeatService studySeatService;

    //涉及的表
    // - 可预约座位信息表 order_seat
    // - 预约规则表 order_rule
    // - 座位信息表 study_seat

    //每天22点运行
//    @Scheduled(cron="0 0 22 * * ?")   //TODO 删除注释
    public void process(){
        try{
            loggerUtils.info("更新每天可预约座位信息的定时任务开始执行");

            //1.将order_seat中所有可预约、已预约的座位状态都设置为待预约状态 [重置]
            orderSeatService.resetStatus();

            //2.从order_rule中拿到被设置预约规则的自习室编号List(eg:JA101,JA103)
            List<String> ruledRooms = orderRuleService.getRuledRooms();
            loggerUtils.info("已设置预约规则的自习室："+ ruledRooms.toString());
            if (ruledRooms.isEmpty()){
                loggerUtils.info("不存在设置预约规则的自习室");
                return;
            }

            //3.删除order_seat中未设置预约规则的座位(eg:删除JA102下的所有座位)
            orderSeatService.deleteUnruledSeat(ruledRooms);

            //4.从order_rule中筛选出mtime为24小时内([前一天22:00,今天22：00))的自习室编号List
            List<String> latestRulesRooms = orderRuleService.getLatestRuledRooms();
            loggerUtils.info("24小时内预约规则被修改的自习室：" + latestRulesRooms.toString());
            if (!latestRulesRooms.isEmpty()){
                //5.将4筛选出的规则内容更新到order_seat
                orderSeatService.updateSeatOrderRule(latestRulesRooms);
            }

            //6.从study_seat中筛选出mtime为24小时内([前一天22:00,今天22：00))的座位信息
            List<HashMap<String,String>> latestModSeats = studySeatService.getLatestModSeat();
            loggerUtils.info("24小时内被修改的座位："+ latestModSeats.toString());
            if (!latestModSeats.isEmpty()){
                //7.将study_seat中的修改应用到order_seat
                orderSeatService.delOrAddSeat(latestModSeats);
            }

        }catch (customException e){
            //若执行失败  再执行一遍
            loggerUtils.error(e.getErrMsg());
        }catch (Exception e){
            loggerUtils.error(e.getMessage());
        }
    }
}

