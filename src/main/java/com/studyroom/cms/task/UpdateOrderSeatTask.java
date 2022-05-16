package com.studyroom.cms.task;

import com.studyroom.cms.result.customException;
import com.studyroom.cms.service.OrderRuleService;
import com.studyroom.cms.service.OrderSeatService;
import com.studyroom.cms.service.StudyRoomService;
import com.studyroom.cms.service.StudySeatService;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Autowired
    private StudyRoomService studyRoomService;

    //涉及的表
    // - 可预约座位信息表 order_seat
    // - 预约规则表 order_rule
    // - 座位信息表 study_seat
    // - 自习室信息表 study_room

    //每天22点运行
//    @Scheduled(cron="0 0 22 * * ?")   //TODO 删除注释
    public void process(){
        try{
            loggerUtils.info("更新每天可预约座位信息的定时任务开始执行");

            //1.将order_seat中所有可预约、已预约的座位状态都设置为待预约状态 [重置]
            orderSeatService.resetStatus();

            //2.从order_rule中拿到被设置预约规则的自习室编号ListA(eg:JA101,JA103)
            List<String> ruledRooms = orderRuleService.getRuledRooms();
            loggerUtils.info("已设置预约规则的自习室："+ ruledRooms.toString());
            if (ruledRooms.isEmpty()){
                loggerUtils.info("不存在设置预约规则的自习室");
                return;
            }

            //3.从study_room中拿到可以使用的自习室编号ListB(eg:JA101,JA103)
            //这部操作的原因：删除自习室成功，但该自习室的座位未全部删除成功，此时若该自习室的预约规则存在，那么这些座位仍然会被预约到
            //             因此对自习室也加一层校验
            List<String> validRooms = studyRoomService.getValidStudyRooms();
            loggerUtils.info("可以使用的自习室："+ validRooms.toString());
            if (validRooms.isEmpty()){
                loggerUtils.info("不存在可以使用的自习室");
                return;
            }

            //4.对1和2获取到的自习室编号List做取交集操作
            List<String>  ruledAndValidRoom = new ArrayList<>(ruledRooms);
            ruledAndValidRoom.retainAll(validRooms);
            loggerUtils.info("可以使用且已设置预约规则的自习室：" + ruledAndValidRoom.toString());
            if (ruledAndValidRoom.isEmpty()){
                loggerUtils.info("不存在可以使用且已设置预约规则的自习室");
                return;
            }

            //5.删除order_seat中未设置预约规则或者不可以使用的座位(eg:删除JA102下的所有座位)
            orderSeatService.deleteUnruledOrInvalidSeat(ruledAndValidRoom);

            //6.从order_rule中筛选出mtime为24小时内([前一天22:00,今天22：00))的自习室编号List
            List<String> latestRulesRooms = orderRuleService.getLatestRuledRooms();
            loggerUtils.info("24小时内预约规则被修改的自习室：" + latestRulesRooms.toString());
            if (!latestRulesRooms.isEmpty()){
                //7.将4筛选出的规则内容更新到order_seat
                orderSeatService.updateSeatOrderRule(latestRulesRooms);
            }

            //8.从study_seat中筛选出mtime为24小时内([前一天22:00,今天22：00))的座位信息
            List<HashMap<String,String>> latestModSeats = studySeatService.getLatestModSeat();
            loggerUtils.info("24小时内被修改的座位："+ latestModSeats.toString());
            if (!latestModSeats.isEmpty()){
                //9.将study_seat中的修改应用到order_seat
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

