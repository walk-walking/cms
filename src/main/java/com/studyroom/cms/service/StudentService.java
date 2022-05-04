package com.studyroom.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getPassword(String stuNo) throws Exception{
        String password = "";
        String sql = "select password from student where stuNo='" + stuNo + "' and is_valid=1";
        try{
            password = jdbcTemplate.queryForObject(sql,String.class);
        }catch (EmptyResultDataAccessException e){
            //未找到该学生
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return password;
    }
}
