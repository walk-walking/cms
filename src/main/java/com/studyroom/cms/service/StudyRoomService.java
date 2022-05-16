package com.studyroom.cms.service;

import com.studyroom.cms.entity.Student;
import com.studyroom.cms.entity.StudyRoom;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudyRoomService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addOne(HashMap<String,String> data) throws Exception{
        int retId = 0;
        String sql = "insert into `study_room` (`number`,`campus`,`building`,`seat_count`) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    //指定主键
                    PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql, new String[]{"id"});
                    preparedStatement.setString(1, data.get("number"));
                    preparedStatement.setString(2, data.get("campus"));
                    preparedStatement.setString(3, data.get("building"));
                    preparedStatement.setInt(4,Integer.parseInt(data.get("seat_count")));
                    return preparedStatement;
                }
            }, keyHolder);
            retId = keyHolder.getKey().intValue();
        }catch (DuplicateKeyException de){
            //已有相同自习室号
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return retId;
    }

    public StudyRoom getOneByNumber(String number) throws Exception{
        StudyRoom stuRoom = null;
        String sql = "select `id`,`number`,`campus`,`building`,`seat_count`,`is_valid` from `study_room` where `number`='" + number + "'";
        try{
            stuRoom = jdbcTemplate.queryForObject(sql, new RowMapper<StudyRoom>() {
                @Override
                public StudyRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
                    StudyRoom row = new StudyRoom();
                    row.setId(rs.getInt("id"));
                    row.setNumber(rs.getString("number"));
                    row.setCampus(rs.getString("campus"));
                    row.setBuilding(rs.getString("building"));
                    row.setSet_count(rs.getInt("seat_count"));
                    row.setIs_valid(rs.getInt("is_valid"));
                    return row;
                }
            });
        }catch (EmptyResultDataAccessException e){
            //未找到该自习室
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return stuRoom;
    }

    public HashMap<String,Object> getList(int page,int pageSize,HashMap<String,Object> condMap) throws Exception{
        HashMap<String,Object> res = new HashMap<>();
        int offset = (page - 1) * pageSize;
        int rowcnt = pageSize;
        String sql = "select `number`,`campus`,`building`,`seat_count`,`is_valid` from `study_room` ";
        String condition = "";
        List<String> intColumns = StudyRoom.getIntColumn();
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
            sql += "where " + condition + "order by `id` desc limit " + offset + "," + rowcnt;
        }else{
            sql += "order by `id` desc limit "+ offset + "," + rowcnt;
        }

        try{
            List<Map<String,Object>> rs = jdbcTemplate.queryForList(sql);
            int count = getCount(condition);
            res.put("count",count);
            List<HashMap<String,Object>> data = new ArrayList<>();
            for (int i = 0; i < rs.size(); ++i){
                HashMap<String,Object> row = new HashMap<>();
                row.put("number",rs.get(i).get("number"));
                row.put("campus",rs.get(i).get("campus"));
                row.put("building",rs.get(i).get("building"));
                row.put("seat_count",rs.get(i).get("seat_count"));
                row.put("is_valid",rs.get(i).get("is_valid"));
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
        String sql = "select count(*) from `study_room`";
        if (condition != ""){
            sql += "where " + condition;
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

    public int delOne(String number) throws Exception{
        int effectId = 0;
        try{
            StudyRoom stuRoom = getOneByNumber(number);
            if (stuRoom != null){
                String sql = "delete from `study_room` where `number`='" + number + "'";
                if(jdbcTemplate.update(sql) > 0){
                    effectId = stuRoom.getId();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return effectId;
    }

    public List<String> getValidStudyRooms() throws customException {
        List<String> ret = new ArrayList<>();
        String sql = "select `number` from `study_room` where `is_valid`=1";

        try{
            List<Map<String,Object>> sqlRet = jdbcTemplate.queryForList(sql);
            for (int i = 0; i< sqlRet.size(); ++i){
                ret.add(sqlRet.get(i).get("number").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_VALID_STUDY_ROOM_FAIL);
        }
        return ret;
    }
}
