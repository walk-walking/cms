package com.studyroom.cms.service;

import com.studyroom.cms.entity.StudySeat;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReserveService {
    //按照该项目的编码需求,在接口层中写实现层


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    StudyRoomService studyRoomService;

    @Autowired
    StudySeatService studySeatService;



    public Result OrderLogic(String studentNumber,String roomNumber, String seatNumber, Date startTime, Date Endtime) throws Exception {


        long diff = (Endtime.getTime() - startTime.getTime())/1000;
        System.out.println(diff);
        //time not enough
        if(diff <14400){
            return Result.fail(ResultCodeEnum.TIME_NOT_ENOUGH);
        }
        //检查studentNumber
        String counts = "";
        String sql = "select count(*) from `student` where `number`='" + studentNumber + "' and `is_valid`=1";
        try{
            counts = jdbcTemplate.queryForObject(sql,String.class);
        } catch (Exception e){
            //其他异常错误
            e.printStackTrace();
        }

        if(!counts.equals("1")){
            return Result.fail(ResultCodeEnum.STUDENT_NUMBER_NOT_VALID);
        }

        //检查roomNumber,seatNumber
        StudySeat ss = studySeatService.getSeatByMixNumber(roomNumber,seatNumber);
        if(ss == null){
            return Result.fail(ResultCodeEnum.STUDY_SEAT_NOT_VALID);
        }



        //检查预约时间是否满足预约规则
        //to be c





        return Result.success();
    }

}
