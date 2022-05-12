package com.studyroom.cms.service;

import com.studyroom.cms.result.ExceptionCodeEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
public class OrderSeatServiceTest {

    @Autowired
    private OrderSeatService orderSeatService;

    @Test
    @Transactional
    @Rollback
    public void resetStatus() {
        try {
            orderSeatService.resetStatus();
        }catch (Exception e){
            Assert.assertEquals(ExceptionCodeEnum.RESET_ORDER_SEAT_STATUS_FAIL.getErrMsg(),e.getMessage());
        }
    }
}