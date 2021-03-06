package com.studyroom.cms.service;


import com.studyroom.cms.entity.Student;
import com.studyroom.cms.result.ExceptionCodeEnum;
import com.studyroom.cms.result.customException;
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
        String sql = "insert into `student` (`number`,`name`,`sex`,`campus`,`email`,`finish_year`,`password`) values (?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    //指定主键
                    PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, data.get("number"));
                    preparedStatement.setString(2, data.get("name"));
                    preparedStatement.setString(3, data.get("sex"));
                    preparedStatement.setString(4, data.get("campus"));
                    preparedStatement.setString(5, data.get("email"));
                    preparedStatement.setInt(6,Integer.parseInt(data.get("finish_year")));
                    preparedStatement.setString(7,data.get("password"));
                    return preparedStatement;
                }
            }, keyHolder);
            int retId = keyHolder.getKey().intValue();
            return retId;
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }
    }

    public Student getOneByNumber(String number) throws Exception{
        Student stu = null;
        String sql = "select `id`,`number`,`name`,`sex`,`campus`,`email`,`finish_year`,`password` from `student` where `number`='" + number + "' and `is_valid`=1";
        try{
            stu = jdbcTemplate.queryForObject(sql, new RowMapper<Student>() {
                @Override
                public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Student row = new Student();
                    row.setId(rs.getInt("id"));
                    row.setNumber(rs.getString("number"));
                    row.setName(rs.getString("name"));
                    row.setSex(rs.getString("sex"));
                    row.setCampus(rs.getString("campus"));
                    row.setEmail(rs.getString("email"));
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
                String sql = "update `student` set `name` =?,`sex`=?,`campus`=?,`email`=?,`finish_year`=?,`password`=? where `number`=?";
                //如果更新前后数据一致  底层不会有更新操作  但effectRow仍然是1
                effectRow = jdbcTemplate.update(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        //指定主键
                        PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
                        preparedStatement.setString(1, data.get("name"));
                        preparedStatement.setString(2, data.get("sex"));
                        preparedStatement.setString(3, data.get("campus"));
                        preparedStatement.setString(4, data.get("email"));
                        preparedStatement.setInt(5,Integer.parseInt(data.get("finish_year")));
                        preparedStatement.setString(6,data.get("password"));
                        preparedStatement.setString(7, data.get("number"));
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

    public HashMap<String,Object> getList(int page,int pageSize,HashMap<String,Object> condMap) throws Exception{
        HashMap<String,Object> res = new HashMap<>();
        int offset = (page - 1) * pageSize;
        int rowcnt = pageSize;
        String sql = "select `number`,`name`,`sex`,`campus`,`email`,`finish_year`,`password` from `student` where `is_valid`=1";
        String condition = "";
        List<String> intColumns = Student.getIntColumn();
        for (String key : condMap.keySet()){
            if (!intColumns.contains(key)){
                condition += "`" + key + "`='" + condMap.get(key) + "'";
            }else{
                condition += "`" + key + "`=" + condMap.get(key);
            }
            condition += " and ";
        }
        if (condition != ""){
            condition = condition.substring(0,condition.length()-4);
            sql += " and " + condition + "order by `id` desc limit " + offset + "," + rowcnt;
        }else{
            sql += " order by `id` desc limit "+ offset + "," + rowcnt;
        }

        try{
            List<Map<String,Object>> rs = jdbcTemplate.queryForList(sql);
            int count = getCount(condition);
            res.put("count",count);
            List<HashMap<String,Object>> data = new ArrayList<>();
            for (int i = 0; i < rs.size(); ++i){
                HashMap<String,Object> row = new HashMap<>();
                row.put("number",rs.get(i).get("number"));
                row.put("name",rs.get(i).get("name"));
                row.put("sex",rs.get(i).get("sex"));
                row.put("campus",rs.get(i).get("campus"));
                row.put("email",rs.get(i).get("email"));
                row.put("finish_year",rs.get(i).get("finish_year"));
                row.put("password",rs.get(i).get("password"));
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

    public int getCount(String condition) throws Exception{
        int count = 0;
        String sql = "select count(*) from `student` where `is_valid`=1";
        if (condition != ""){
            sql += " and " + condition;
        }
        try{
            count = jdbcTemplate.queryForObject(sql,Integer.class);
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return count;
    }

    public int modOneColumn(String columnName, Object columnValue ,String number) throws Exception{
        int effecId = 0;
        try{
            Student stu = getOneByNumber(number);
            if (stu != null){
                List<String> intColumnList = Student.getIntColumn();
                String sql = "";
                if (intColumnList.contains(columnName)){
                    sql = "update `student` set `" + columnName + "` = "+ columnValue +" where `number`='" + number + "'";
                }else {
                    sql = "update `student` set `" + columnName + "` = '"+ columnValue +"' where `number`='" + number + "'";
                }

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

    public HashMap<String,String> getEmailByNumber(List<String> numberList) throws customException {
        HashMap<String,String>  ret = new HashMap<>();
        if (numberList.isEmpty()){
            return ret;
        }

        try{
            StringBuffer numberStr = new StringBuffer();
            for (int i = 0; i < numberList.size(); ++i){
                numberStr.append("'");
                numberStr.append(numberList.get(i));
                numberStr.append("'");
                numberStr.append(",");
            }
            numberStr.deleteCharAt(numberStr.length()-1);
            String sql = "select `number`,`email` from `student` where `number` in (" + numberStr.toString() + ") and `is_valid`=1";

            List<Map<String,Object>> sqlRet = jdbcTemplate.queryForList(sql);
            for (int i = 0; i < sqlRet.size(); ++i){
                ret.put(sqlRet.get(i).get("number").toString(),sqlRet.get(i).get("email").toString());
            }
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_EMAIL_BY_NUMBER_FAIL);
        }
        return ret;
    }
}
