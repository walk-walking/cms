package com.studyroom.cms.service;

import com.studyroom.cms.entity.StudyRoom;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudySeatService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean batchAdd(String roomNumber,int count){
        String sql = "insert into `study_seat`(`number`,`room_number`) values (?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 1; i <= count; ++i){
            batchArgs.add(new Object[]{i,roomNumber});
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
            System.out.println(effectRow);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }
        return effectRow;
    }
}
