package com.studyroom.cms.service;

import com.studyroom.cms.entity.StudyRoom;
import com.studyroom.cms.entity.StudySeat;
import com.studyroom.cms.result.ExceptionCodeEnum;
import com.studyroom.cms.result.customException;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudySeatService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean batchAdd(String roomNumber,String building,int count){
        String sql = "insert into `study_seat`(`number`,`room_number`,`building`) values (?,?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 1; i <= count; ++i){
            batchArgs.add(new Object[]{i,roomNumber,building});
        }
        try{
            jdbcTemplate.batchUpdate(sql,batchArgs);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int deleteByRoomNumber(String roomNumber) throws Exception{
        int effectRow = 0;
        String  sql = "delete from `study_seat` where `room_number`='" + roomNumber + "'";
        try{
            effectRow = jdbcTemplate.update(sql);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }
        return effectRow;
    }

    public HashMap<String,Object> getListByRoomNumber(int page, int pageSize, String roomNumber) throws Exception{
        HashMap<String,Object> res = new HashMap<>();
        int offset = (page - 1) * pageSize;
        int rowcnt = pageSize;
        String sql = "select `number`,`is_valid`,`has_plug` from `study_seat` where `room_number`='"+ roomNumber +"' order by `id` desc limit " + offset + "," + rowcnt;
        try{
            List<Map<String,Object>> rs = jdbcTemplate.queryForList(sql);
            int count = getCountByRoomNumber(roomNumber);
            res.put("count",count);
            List<HashMap<String,Object>> data = new ArrayList<>();
            for (int i = 0; i < rs.size(); ++i){
                HashMap<String,Object> row = new HashMap<>();
                row.put("number",rs.get(i).get("number"));
                row.put("is_valid",rs.get(i).get("is_valid"));
                row.put("has_plug",rs.get(i).get("has_plug"));
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

    public int getCountByRoomNumber(String roomNumber) throws Exception{
        int count = 0;
        String sql = "select count(*) from `study_seat` where `room_number`='" + roomNumber + "'";
        try{
            count = jdbcTemplate.queryForObject(sql,Integer.class);
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return count;
    }

    public StudySeat getSeatByMixNumber(String roomNumber, String number) throws Exception{
        StudySeat stuSeat = null;
        String sql = "select `id`,`number`,`room_number`,`building`,`has_plug`,`is_valid` from study_seat where ";
        sql += "`room_number`='" +roomNumber +"' and `number`='" + number +"'";
        try{
            stuSeat = jdbcTemplate.queryForObject(sql, new RowMapper<StudySeat>() {
                @Override
                public StudySeat mapRow(ResultSet rs, int rowNum) throws SQLException {
                    StudySeat row = new StudySeat();
                    row.setId(rs.getInt("id"));
                    row.setNumber(rs.getString("number"));
                    row.setRoom_number(rs.getString("room_number"));
                    row.setBuilding(rs.getString("building"));
                    row.setHas_plug(rs.getInt("has_plug"));
                    row.setIs_valid(rs.getInt("is_valid"));
                    return row;
                }
            });
        }catch (EmptyResultDataAccessException e){
            //未找到该自习座位
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return stuSeat;
    }

    public int modHasPlug(String roomNumber, String number, int hasFlag) throws Exception{
        int effectId = 0;
        try{
            StudySeat studySeat = getSeatByMixNumber(roomNumber,number);
            if (studySeat != null){
                String sql = "update `study_seat` set `has_plug`= "+ hasFlag + " where `id`=" + studySeat.getId();
                if(jdbcTemplate.update(sql) > 0){
                    effectId = studySeat.getId();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return effectId;
    }

    public List<HashMap<String,String>> getLatestModSeat() throws customException {
        List<HashMap<String,String>> ret = new ArrayList<>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        date = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        String sql = "select `room_number`,`number`,`building`,`is_valid` from `study_seat` where mtime >= '" + sdf.format(date) + " 22:00:00'";
        try{
            List<Map<String,Object>> sqlRet = jdbcTemplate.queryForList(sql);
            for (int i = 0; i< sqlRet.size(); ++i){
                HashMap<String,String>  row = new HashMap<>();
                row.put("room_number",sqlRet.get(i).get("room_number").toString());
                row.put("number",sqlRet.get(i).get("number").toString());
                row.put("is_valid",sqlRet.get(i).get("is_valid").toString());
                row.put("building",sqlRet.get(i).get("building").toString());
                ret.add(row);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_LATEST_MOD_SEAT_FAIL);
        }
        return ret;
    }
}
