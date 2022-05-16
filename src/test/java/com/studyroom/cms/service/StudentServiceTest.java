package com.studyroom.cms.service;

import com.studyroom.cms.entity.Student;
import org.junit.Assert;
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
@TestPropertySource("classpath:application-test.properties")    //测试环境使用不同数据库配置
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Test
    public void getPassword(){
        try{
            String pw1 = studentService.getPassword("21210240999");
            Assert.assertEquals("2022-fdu",pw1);
            String pw2 = studentService.getPassword("21210240987");
            Assert.assertEquals("",pw2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    @Transactional      //写数据库 测试完成回滚
    @Rollback
    public void addOne() {
        try{
            HashMap<String,String> data = new HashMap<String,String>(){
                {
                    put("number","21210240456");
                    put("name","周小小");
                    put("sex","女");
                    put("campus","邯郸");
                    put("email","21210240456@m.fudan.edu.cn");
                    put("finish_year","2025");
                    put("password","2022-fdu");
                }
            };

            int retId1 = studentService.addOne(data);
            Assert.assertNotEquals(0,retId1);

            int retId2 = studentService.addOne(data);
            Assert.assertEquals(0,retId2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getOneByNumber() {
        try{
            String number = "21210240999";
            Student stu1 = studentService.getOneByNumber(number);
            Assert.assertEquals(number,stu1.getNumber());
            Student stu2 = studentService.getOneByNumber("21210240987");
            Assert.assertNull(stu2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void modOne() {
        try{
            HashMap<String,String> data = new HashMap<String,String>(){
                {
                    put("number","21210240999");
                    put("name","王小五");
                    put("sex","男");
                    put("campus","江湾");
                    put("email","21210240999@m.fudan.edu.cn");
                    put("finish_year","2024");
                    put("password","2022-fdu");
                }
            };

            int retId1 = studentService.modOne(data);
            Assert.assertEquals(1,retId1);

            data.put("number","21210240456");
            int retId2 = studentService.modOne(data);
            Assert.assertEquals(0,retId2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void delOne() {
        try{
            String number = "21210240999";
            int retId1 = studentService.delOne(number);
            Assert.assertNotEquals(0,retId1);

            int retId2 = studentService.delOne("21210240456");
            Assert.assertEquals(0,retId2);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getList() {
        try{
            HashMap<String,Object> stuList = studentService.getList(1,3);
            Assert.assertNotNull(stuList.get("count"));
            Assert.assertNotNull(stuList.get("list"));
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }

    @Test
    public void getCount() {
        try{
           int count = studentService.getCount();
           Assert.assertNotEquals(0,count);
        }catch (Exception e){
            Assert.assertEquals("mysql execute error",e.getMessage());
        }
    }
}