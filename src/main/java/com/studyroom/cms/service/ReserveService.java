package com.studyroom.cms.service;

import com.studyroom.cms.entity.OrderSeat;
import com.studyroom.cms.entity.StudentOrderMessage;
import com.studyroom.cms.entity.StudySeat;
import com.studyroom.cms.result.ExceptionCodeEnum;
import com.studyroom.cms.result.Result;
import com.studyroom.cms.result.ResultCodeEnum;
import com.studyroom.cms.result.customException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReserveService {
    //按照该项目的编码需求,在接口层中写实现层


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    StudyRoomService studyRoomService;

    @Autowired
    StudySeatService studySeatService;

    @Autowired
    StudentOrderMessageService studentOrderMessageService;


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

        //检测是否过了签到时间: startTime + 15min
        if(!timeDistance(getNowTime(),ST,900)){
            return Result.fail(ResultCodeEnum.SIGNIN_TIME_OUT);
        }
        //提前15分钟签到
        if(timeDistance(getNowTime(),ST,-900)){
            return Result.fail(ResultCodeEnum.SIGNIN_TIME_TOO_EARLY);
        }

        //所有的逻辑检测完成,现在进行签到操作
        sql = "update student_order_message set is_sign_in='" + 1+ "' where student_number='"+ studentNumber + "' and `room_number`='" + roomNumber + "' and `seat_number`='"+ seatNumber+"'";
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
     * 取消订单逻辑
     * @param orderNo
     * @param studentSessionNo
     * @return
     * @throws Exception
     */
    public Result cancelLogic(String orderNo,String studentSessionNo) throws Exception{

        //判断订单是否存在
        StudentOrderMessage studentOrderMessage = null;
        int count = 0;
        String sql = "select count(*) from `student_order_message` where `id`='" + orderNo +"' ";
        count = jdbcTemplate.queryForObject(sql,Integer.class);
        if(count==0){
            return Result.fail(ResultCodeEnum.ORDER_NUMBER_NOT_EXIST);
        }

        //查询订单信息
        sql = "select * from `student_order_message` where `id`='" + orderNo +"' ";
        try{
            studentOrderMessage = jdbcTemplate.queryForObject(sql, new RowMapper<StudentOrderMessage>() {
                @Override
                public StudentOrderMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
                    StudentOrderMessage row = new StudentOrderMessage();

                    String dateStr = rs.getString("order_start_time");
                    Date ST = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        ST =sdf.parse(dateStr.split("\\.")[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    row.setIsOrderValid(rs.getInt("is_sign_in"));
                    row.setIsOrderValid(rs.getInt("is_order_valid"));
                    row.setOrderStartTime(ST);
                    row.setStudentNumber(rs.getString("student_number"));
                    return row;
                }
            });

        } catch (Exception e){
            //其他异常错误
            e.printStackTrace();
        }
        //权限检查
        if(!studentIDAuthority(studentOrderMessage.getStudentNumber(),studentSessionNo)){
            return Result.fail(ResultCodeEnum.STUDENT_ID_NOT_MATCHING);
        }
        //判断是否签过到
        if(studentOrderMessage.getIsSignIn()==1){
            return Result.fail(ResultCodeEnum.IS_SIGNINED);
        }
        //判断取消签到是否晚于开始时间
        if(!timeDistance(getNowTime(),studentOrderMessage.getOrderStartTime(),0)){
            return Result.fail(ResultCodeEnum.CANCEL_TIME_OUT);
        }
        //判断是否已经取消预约
        if(studentOrderMessage.getIsOrderValid()!=1){
            return Result.fail(ResultCodeEnum.IS_CANCEL);
        }

        //所有逻辑检测完成,进行取消预约操作,将is_vaild 改为0
        sql = "update student_order_message set is_order_valid='" + 0 +"' where id = '" + orderNo +"'";

        try{
            jdbcTemplate.update(sql);
        } catch (Exception e){
            //其他异常错误
            e.printStackTrace();
        }
        return Result.success();
    }

    /**
     * 根据对应的studentNumber返回预约成功的预约记录
     * @param studentNumber
     * @param studentSessionNo
     * @return
     */
    public Result queryReserverListLogic(String studentNumber,String studentSessionNo){
        //权限检查
        if(!studentIDAuthority(studentNumber,studentSessionNo)){
            return Result.fail(ResultCodeEnum.STUDENT_ID_NOT_MATCHING);
        }
        //使用StudentOrderMessageService的方法
        List<StudentOrderMessage> ret = new ArrayList<>();
        try{
            StringBuffer condition = new StringBuffer();
            condition.append("`is_order_valid`=1 and ");
            condition.append("`student_number`=");
            condition.append(studentNumber);

            ret = studentOrderMessageService.getOrderMessageByCondition(condition.toString());
            System.out.println(ret);

            //return
            return Result.listSuccess(ret.size(),ret);

        }catch (Exception e){
            e.printStackTrace();
            return Result.fail(ResultCodeEnum.REVERSE_MESSAGE_QUERY_FAIL);
        }

    }

    public Result queryUnReserverListLogic(String studyRoomNumber){


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

    /**
     *
     * @return
     * 返回当前时间
     */
    public Date getNowTime() throws ParseException {
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String nowTimeStr = sdf.format(calendar.getTime());
        Date nowTime = sdf.parse(nowTimeStr);
        return nowTime;
    }




}
