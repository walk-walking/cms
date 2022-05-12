package com.studyroom.cms.service;

import com.studyroom.cms.entity.StudySeat;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@Service
public class ReserveService {
    //按照该项目的编码需求,在接口层中写实现层


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    StudyRoomService studyRoomService;

    @Autowired
    StudySeatService studySeatService;


    /**
     * 预约逻辑
     * 根据如下参数进行预约逻辑
     * @param studentNumber
     * @param roomNumber
     * @param seatNumber
     * @param startTime
     * @param Endtime
     * @return
     * @throws Exception
     */
    public Result OrderLogic(String studentNumber,String roomNumber, String seatNumber, Date startTime, Date Endtime, String studentSessionNo) throws Exception {

        if(!studentIDAuthority(studentNumber,studentSessionNo)){
            return Result.fail(ResultCodeEnum.STUDENT_ID_NOT_MATCHING);
        }
        if(!studentNumberCheck(studentNumber)){
            return Result.fail(ResultCodeEnum.STUDENT_NUMBER_NOT_VALID);
        }


        if(!timeDistance(Endtime,startTime,14400)){
            return Result.fail(ResultCodeEnum.TIME_NOT_ENOUGH);
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


    /**
     * 签到逻辑
     * 当IP(位置)检测成功后,利用如下学生代码/预约代码进行签到
     * @param studentNumber
     * @return
     */
    public Result signInLogic(String studentNumber,String roomNumber,String seatNumber,String studentSessionNo) throws Exception{

        if(!studentIDAuthority(studentNumber,studentSessionNo)){
            return Result.fail(ResultCodeEnum.STUDENT_ID_NOT_MATCHING);
        }
        if(!studentNumberCheck(studentNumber)){
            return Result.fail(ResultCodeEnum.STUDENT_NUMBER_NOT_VALID);
        }

        String res = "-1";
        String sql = "select is_sign_in from `student_order_message` where `seat_number`='" + seatNumber + "'  and `room_number`='" + roomNumber + "" +
                "'  and `student_number`='" + studentNumber + "'";
        try{
            res = jdbcTemplate.queryForObject(sql,String.class);
            System.out.println(res);
        } catch (Exception e){
            //其他异常错误
            e.printStackTrace();
        }
        //判断是否签过到
        if(res.equals("1")){
            return Result.fail(ResultCodeEnum.IS_SIGNINED);
        }

        res = null;
        sql = "select order_start_time from `student_order_message` where `seat_number`='" + seatNumber + "'  and `room_number`='" + roomNumber + "" +
                "'  and `student_number`='" + studentNumber + "'";
        System.out.println(sql);
        try{
            res = jdbcTemplate.queryForObject(sql,String.class);
            System.out.println(res);
        } catch (Exception e){
            //其他异常错误
            e.printStackTrace();
        }

        //检测该签到信息是否存在
        if(res==null){
            return Result.fail(ResultCodeEnum.ORDER_MESSAGE_NOT_EXIST);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date ST =sdf.parse(res.split("\\.")[0]);
        //获取当前时间
        Calendar calendar = Calendar.getInstance();
        String nowTimeStr = sdf.format(calendar.getTime());
        Date nowTime = sdf.parse(nowTimeStr);

        //检测是否过了签到时间: startTime + 15min
        if(!timeDistance(nowTime,ST,900)){
            return Result.fail(ResultCodeEnum.SIGNIN_TIME_OUT);
        }
        //提前15分钟签到
        if(timeDistance(nowTime,ST,-900)){
            return Result.fail(ResultCodeEnum.SIGNIN_TIME_TOO_EARLY);
        }

        //所有的逻辑检测完成,现在进行签到操作
        sql = "update student_order_message set is_sign_in='" + 1+ "' where student_number='"+ studentNumber + "' and `room_number`='" + roomNumber + "' and `seat_number`='"+ seatNumber+"'";
        System.out.println(sql);
        try{
            jdbcTemplate.update(sql);
        } catch (Exception e){
            //其他异常错误
            e.printStackTrace();
        }

        //success
        return Result.success();
    }


    /**
     * 学生NumberCheck——复用代码
     * @return
     */
    public boolean studentNumberCheck(String studentNumber){
        String counts = "";
        String sql = "select count(*) from `student` where `number`='" + studentNumber + "' and `is_valid`=1";
        try{
            counts = jdbcTemplate.queryForObject(sql,String.class);

        } catch (Exception e){
            //其他异常错误
            e.printStackTrace();
        }
        if(!counts.equals("1")){
            return false;
        }

        return true;

    }

    /**
     * 验证学生ID以及Session的ID是否相等
     * @param studentSessionNo
     * @param studentNumber
     * @return
     */
    public boolean studentIDAuthority(String studentSessionNo,String studentNumber){
        return studentSessionNo.equals(studentNumber);
    }

    /**
     * 位置check(觉得应该在前端完成)
     * @return
     */
    public boolean positionCheck(){
        return true;
    }

    /**
     * 从startTime到EndTime 计算其时间是否超过了X,若超过则返回false,若未超过则返回true
     * @param startTime  实际操作时间
     * @param Endtime   系统让学生操作的时间
     * @param diff :second 例如900 = 15分钟, 3600 = 1h
     * @return

     */
    public boolean timeDistance(Date startTime,Date Endtime,long diff){
        long checkDiff = (startTime.getTime() - Endtime.getTime())/1000;
        if(checkDiff>diff){
            return false;
        }
        return true;

    }




}
