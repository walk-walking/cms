package com.studyroom.cms.service;

import com.studyroom.cms.entity.StudyRoom;
import org.junit.*;
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
public class StudyRoomServiceTest {

    private String existStuRoomNumber = "JA101";
    private String notExistStuRoomNumber = "JA999";
    private String condCampus = "江湾";

    @Autowired
    private StudyRoomService studyRoomService;

    @Test
    @Transactional
    @Rollback
    public void addOne() {
        try{
            HashMap<String,String> data = new HashMap<String,String>(){
                {
                    put("number","JA103");
                    put("campus","江湾");
                    put("building","A教学楼");
                    put("seat_count","3");
                }
            };

            int retId1 = studyRoomService.addOne(data);
            Assert.assertNotEquals(0,retId1);

            int retId2 = studyRoomService.addOne(data);
            Assert.assertEquals(0,retId2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getOneByNumber() {
        try{
            StudyRoom stuRoom1 = studyRoomService.getOneByNumber(existStuRoomNumber);
            Assert.assertEquals(existStuRoomNumber,stuRoom1.getNumber());
            StudyRoom stuRoom2 = studyRoomService.getOneByNumber(notExistStuRoomNumber);
            Assert.assertNull(stuRoom2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getList() {
        try{
            HashMap<String,Object> condArr = new HashMap<String,Object>(){
                {put("campus",condCampus);}
            };
            HashMap<String,Object> stuList = studyRoomService.getList(1,3,condArr);
            Assert.assertNotNull(stuList.get("count"));
            Assert.assertNotNull(stuList.get("list"));
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getCount() {
        try{
            String condition = "campus='" + condCampus + "'";
            int count = studyRoomService.getCount(condition);
            Assert.assertNotEquals(0,count);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void delOne() {
        try{
            int retId1 = studyRoomService.delOne(existStuRoomNumber);
            Assert.assertNotEquals(0,retId1);

            int retId2 = studyRoomService.delOne(notExistStuRoomNumber);
            Assert.assertEquals(0,retId2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }
}