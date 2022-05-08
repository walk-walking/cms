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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getPassword(String number) throws Exception{
        String password = "";
        String sql = "select `password` from `student` where `number`='" + number + "' and `is_valid`=1";
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
        String sql = "insert into `student` (`number`,`name`,`campus`,`finish_year`,`password`) values (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    //指定主键
                    PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, data.get("number"));
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

    public Student getOneByNumber(String number) throws Exception{
        Student stu = null;
        String sql = "select `id`,`number`,`name`,`campus`,`finish_year`,`password` from `student` where `number`='" + number + "' and `is_valid`=1";
        try{
            stu = jdbcTemplate.queryForObject(sql, new RowMapper<Student>() {
                @Override
                public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Student row = new Student();
                    row.setId(rs.getInt("id"));
                    row.setNumber(rs.getString("number"));
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
            Student stu = getOneByNumber(data.get("number"));
            if (stu != null){
                String sql = "update `student` set `name` =?,`campus`=?,`finish_year`=?,`password`=? where `number`=?";
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
                        preparedStatement.setString(5, data.get("number"));
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

    public int delOne(String number) throws Exception{
        int effecId = 0;
        try{
            Student stu = getOneByNumber(number);
            if (stu != null){
                String sql = "update `student` set `is_valid` = 0 where `number`='" + number + "'";
                if (jdbcTemplate.update(sql) > 0){
                    effecId = stu.getId();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return effecId;
    }

    public HashMap<String,Object> getList(int page,int pageSize) throws Exception{
        HashMap<String,Object> res = new HashMap<>();
        int offset = (page - 1) * pageSize;
        int rowcnt = pageSize;
        String sql = "select `number`,`name`,`campus`,`finish_year` from `student` where `is_valid`=1 order by `id` desc limit " + offset + "," + rowcnt;
        try{
            List<Map<String,Object>> rs = jdbcTemplate.queryForList(sql);
            int count = getCount();
            res.put("count",count);
            List<HashMap<String,Object>> data = new ArrayList<>();
            for (int i = 0; i < rs.size(); ++i){
                HashMap<String,Object> row = new HashMap<>();
                row.put("number",rs.get(i).get("number"));
                row.put("name",rs.get(i).get("name"));
                row.put("campus",rs.get(i).get("campus"));
                row.put("finish_year",rs.get(i).get("finish_year"));
                data.add(row);
            }
            res.put("list",data);
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }
        return res;
    }

    public int getCount() throws Exception{
        int count = 0;
        String sql = "select count(*) from `student` where `is_valid`=1";
        try{
            count = jdbcTemplate.queryForObject(sql,Integer.class);
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return count;
    }
}
