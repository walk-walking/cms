package com.studyroom.cms.service;


import com.studyroom.cms.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.HashMap;

@Service
public class StudentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getPassword(String stuNo) throws Exception{
        String password = "";
        String sql = "select `password` from `student` where `stuNo`='" + stuNo + "' and `is_valid`=1";
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

    public int addOne(HashMap<String,String> data) throws Exception{
        int retId = 0;
        String sql = "insert into `student` (`stuNo`,`name`,`campus`,`finish_year`,`password`) values (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    //指定主键
                    PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, data.get("stuNo"));
                    preparedStatement.setString(2, data.get("name"));
                    preparedStatement.setString(3, data.get("campus"));
                    preparedStatement.setInt(4,Integer.parseInt(data.get("finish_year")));
                    preparedStatement.setString(5,data.get("password"));
                    return preparedStatement;
                }
            }, keyHolder);
            retId = keyHolder.getKey().intValue();
        }catch (DuplicateKeyException de){
            //已有相同学号
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return retId;
    }

    public Student getOneByStuNo(String stuNo) throws Exception{
        Student stu = null;
        String sql = "select `stuNo`,`name`,`campus`,`finish_year`,`password` from `student` where `stuNo`='" + stuNo + "' and `is_valid`=1";
        try{
            stu = jdbcTemplate.queryForObject(sql, new RowMapper<Student>() {
                @Override
                public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Student row = new Student();
                    row.setStuNo(rs.getString("stuNo"));
                    row.setName(rs.getString("name"));
                    row.setCampus(rs.getString("campus"));
                    row.setFinish_year(rs.getInt("finish_year"));
                    row.setPassword(rs.getString("password"));
                    return row;
                }
            });
        }catch (EmptyResultDataAccessException e){
            //未找到该学生
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return stu;
    }

    public int modOne(HashMap<String,String> data) throws Exception{
        int effectRow = 0;
        try{
            Student stu = getOneByStuNo(data.get("stuNo"));
            if (stu != null){
                String sql = "update `student` set `name` =?,`campus`=?,`finish_year`=?,`password`=? where `stuNo`=?";
                //如果更新前后数据一致  底层不会有更新操作
                effectRow = jdbcTemplate.update(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        //指定主键
                        PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
                        preparedStatement.setString(1, data.get("name"));
                        preparedStatement.setString(2, data.get("campus"));
                        preparedStatement.setInt(3,Integer.parseInt(data.get("finish_year")));
                        preparedStatement.setString(4,data.get("password"));
                        preparedStatement.setString(5, data.get("stuNo"));
                        return preparedStatement;
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return effectRow;
    }


    public int delOne(String stuNo) throws Exception{
        int effectRow = 0;
        try{
            Student stu = getOneByStuNo(stuNo);
            if (stu != null){
                String sql = "update `student` set `is_valid` = 0 where `stuNo`='" + stuNo + "'";
                effectRow = jdbcTemplate.update(sql);
                System.out.println(effectRow);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return effectRow;
    }
}
