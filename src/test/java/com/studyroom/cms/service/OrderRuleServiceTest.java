package com.studyroom.cms.service;

import com.studyroom.cms.result.ExceptionCodeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderRuleServiceTest {

    @Autowired
    private OrderRuleService orderRuleService;

    @Test
    public void getRuledRooms() {
        try{
            List<String> ruledRoom = orderRuleService.getRuledRooms();
            Assert.assertNotNull(ruledRoom);
        }catch (Exception e){
            Assert.assertEquals(ExceptionCodeEnum.GET_RULED_ROOMS_FAIL.getErrMsg(),e.getMessage());
        }
    }

    @Test
    public void getLatestRuledRooms() {
        orderRuleService.getLatestRuledRooms();
    }
}