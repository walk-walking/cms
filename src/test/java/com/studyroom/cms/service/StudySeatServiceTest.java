package com.studyroom.cms.service;

import com.studyroom.cms.entity.StudySeat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application-test.properties")
public class StudySeatServiceTest {

    private String existStuRoomNumber = "JA101";
    private String existStuSeatNumber = "1";

    @Autowired
    private StudySeatService studySeatService;

    @Test
    @Transactional
    @Rollback
    public void batchAdd() {
        String rooNumber = "JA999";
        int seatCount = 2;
        boolean ret = studySeatService.batchAdd(rooNumber,seatCount);
        Assert.assertNotNull(ret);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteByRoomNumber() {
        try{
            int effectRow = studySeatService.deleteByRoomNumber(existStuRoomNumber);
            Assert.assertNotEquals(0,effectRow);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getListByRoomNumber() {
        try{
            HashMap<String,Object> stuList = studySeatService.getListByRoomNumber(1,3,existStuRoomNumber);
            Assert.assertNotNull(stuList.get("count"));
            Assert.assertNotNull(stuList.get("list"));
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getCountByRoomNumber() {
        try{
            int count = studySeatService.getCountByRoomNumber(existStuRoomNumber);
            Assert.assertNotEquals(0,count);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getSeatByMixNumber() {
        try{
            StudySeat stuSeat = studySeatService.getSeatByMixNumber(existStuRoomNumber,existStuSeatNumber);
            Assert.assertNotNull(stuSeat);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void modHasPlug() {
        try{
            int effectId = studySeatService.modHasPlug(existStuRoomNumber,existStuSeatNumber,1);
            Assert.assertNotEquals(0,effectId);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }
}