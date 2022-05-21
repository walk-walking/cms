package com.studyroom.cms.service;

import com.studyroom.cms.entity.StudentOrderMessage;
import com.studyroom.cms.result.ExceptionCodeEnum;
import com.studyroom.cms.result.customException;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StudentOrderMessageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoggerUtils loggerUtils;

    //获取（已签到且有效）预约即将到期的座位信息
    public List<HashMap<String,String>> getExpiringSeat(String expireTime) throws customException {
        List<HashMap<String,String>>  ret = new ArrayList<>();
        try{
            StringBuffer sql = new StringBuffer();
            sql.append("select `seat_number`,`room_number` from `student_order_message` ");
            sql.append("where `is_sign_in`=1 and `is_order_valid`=1 and ");
            sql.append("`order_end_time`='");
            sql.append(expireTime);
            sql.append("'");

            List<Map<String,Object>> sqlRet = jdbcTemplate.queryForList(sql.toString());
            for (int i = 0; i < sqlRet.size(); ++i){
                HashMap<String,String> row = new HashMap<>();
                row.put("seat_number",sqlRet.get(i).get("seat_number").toString());
                row.put("room_number",sqlRet.get(i).get("room_number").toString());
                ret.add(row);
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_EXPIRING_SEAT_FAIL);
        }

        return ret;
    }

    //获取即将开始但未签到的预约订单信息
    public List<StudentOrderMessage> getNotSignInAndNotStartOrderMessage(String OrderStartTime) throws customException {
        List<StudentOrderMessage>  ret = new ArrayList<>();
        try{
            StringBuffer condition = new StringBuffer();
            condition.append("`is_sign_in`=0 and ");
            condition.append("`is_order_valid`=1 and ");
            condition.append("`order_start_time`='");
            condition.append(OrderStartTime);
            condition.append("'");

            ret = getOrderMessageByCondition(condition.toString());
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_NOT_SIGN_IN_AND_NOT_START_ORDER_MESSAGE_FAIL);
        }

        return ret;
    }

    //获取已经开始但未签到的预约订单信息
    public List<StudentOrderMessage> getNotSignInAndHasStartOrderMessage(String OrderStartTime) throws customException {
        List<StudentOrderMessage>  ret = new ArrayList<>();
        try{
            StringBuffer condition = new StringBuffer();
            condition.append("`is_sign_in`=0 and ");
            condition.append("`is_order_valid`=1 and ");
            condition.append("`order_start_time`='");
            condition.append(OrderStartTime);
            condition.append("'");

            ret = getOrderMessageByCondition(condition.toString());
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_NOT_SIGN_IN_AND_HAS_START_ORDER_MESSAGE_FAIL);
        }

        return ret;
    }

    //根据筛选条件返回预约订单对象
    public List<StudentOrderMessage> getOrderMessageByCondition(String condition){
        List<StudentOrderMessage>  ret = new ArrayList<>();
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            sql.append("`id`,`seat_number`,`room_number`,`student_number`,`order_start_time`,`order_end_time`,`is_sign_in`,`is_order_valid` ");
            sql.append("from `student_order_message` ");
            sql.append("where ");
            sql.append(condition);
            sql.append(" order by `order_start_time` DESC");

            List<Map<String,Object>> sqlRet = jdbcTemplate.queryForList(sql.toString());
            for (int i = 0; i < sqlRet.size(); ++i){
                StudentOrderMessage one = new StudentOrderMessage();
                one.setId(Integer.parseInt(sqlRet.get(i).get("id").toString()));
                one.setSeatNumber(sqlRet.get(i).get("seat_number").toString());
                one.setRoomNumber(sqlRet.get(i).get("room_number").toString());
                one.setStudentNumber(sqlRet.get(i).get("student_number").toString());
                one.setOrderStartTime(sdf.parse(sqlRet.get(i).get("order_start_time").toString()));
                one.setOrderEndTime(sdf.parse(sqlRet.get(i).get("order_end_time").toString()));
                one.setIsSignIn(Integer.parseInt(sqlRet.get(i).get("is_sign_in").toString()));
                one.setIsOrderValid(Integer.parseInt(sqlRet.get(i).get("is_order_valid").toString()));
                ret.add(one);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_ORDER_MESSAGE_BY_CONDITION_FAIL);
        }

        return ret;
    }

    public void reserve_updateSOM(StudentOrderMessage som){

            String insertSql = "insert into student_order_message"+
                "(seat_number,room_number,student_number,order_start_time,order_end_time,is_sign_in,is_order_valid,ctime,mtime)"+
                "values(?,?,?,?,?,?,?,?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String nowTimeStr = sdf.format(calendar.getTime());

        System.out.println("reserve_updateSOM");
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                //指定主键
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(insertSql);
                preparedStatement.setString(1, som.getSeatNumber());
                preparedStatement.setString(2, som.getRoomNumber());
                preparedStatement.setString(3, som.getStudentNumber());
                preparedStatement.setString(4, sdf.format(som.getOrderStartTime()));
                preparedStatement.setString(5, sdf.format(som.getOrderEndTime()));
                preparedStatement.setInt(6, som.getIsSignIn());
                preparedStatement.setInt(7, som.getIsOrderValid());
                preparedStatement.setString(8, nowTimeStr);
                preparedStatement.setString(9, nowTimeStr);
                System.out.println(preparedStatement);
                return preparedStatement;
            }
        });


    }

}
