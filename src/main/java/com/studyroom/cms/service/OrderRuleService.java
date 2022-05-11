package com.studyroom.cms.service;

import com.studyroom.cms.entity.OrderRule;
import com.studyroom.cms.result.ExceptionCodeEnum;
import com.studyroom.cms.result.customException;
import com.studyroom.cms.utils.LoggerUtils;
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
public class OrderRuleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoggerUtils loggerUtils;

    public List<String> getRuledRooms() throws customException {
        List<String> ret = new ArrayList<>();
        String sql = "select `room_number` from `order_rule`";

        try{
            List<Map<String,Object>> sqlRet = jdbcTemplate.queryForList(sql);
            for (int i = 0; i< sqlRet.size(); ++i){
                ret.add(sqlRet.get(i).get("room_number").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_RULED_ROOMS_FAIL);
        }
        return ret;
    }

    public List<String> getLatestRuledRooms() throws customException {
        List<String> ret = new ArrayList<>();

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        date = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sql = "select `room_number` from `order_rule` where `mtime` >= '" + sdf.format(date) + " 22:00:00'";

        try{
            List<Map<String,Object>> sqlRet = jdbcTemplate.queryForList(sql);
            for (int i = 0; i< sqlRet.size(); ++i){
                ret.add(sqlRet.get(i).get("room_number").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_LATEST_RULED_ROOMS_FAIL);
        }
        return ret;
    }

    public OrderRule getOneByRoomNumber(String roomNumber) throws Exception{
        OrderRule orderRule = null;
        String sql = "select `room_number`,`open_time`,`close_time`,`single_order_time` from `order_rule` where `room_number`='" + roomNumber + "'";
        try{
            orderRule = jdbcTemplate.queryForObject(sql, new RowMapper<OrderRule>() {
                @Override
                public OrderRule mapRow(ResultSet rs, int rowNum) throws SQLException {
                    OrderRule row = new OrderRule();
                    row.setRoomNumber(rs.getString("room_number"));
                    row.setOpenTime(rs.getString("open_time"));
                    row.setCloseTime(rs.getString("close_time"));
                    row.setSingleOrderTime(rs.getInt("single_order_time"));
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

        return orderRule;
    }

    public List<String> getRoomsByTime(String time, String type) throws customException{
        List<String> ret = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        if(type == OrderRule.ORDER_TIME_TYPE_OPEN){
            sql.append("select `room_number` from `order_rule` where `open_time` = '");
        }else{
            sql.append("select `room_number` from `order_rule` where `close_time` = '");
        }
        sql.append(time);
        sql.append("'");

        try{
            List<Map<String,Object>> rs = jdbcTemplate.queryForList(sql.toString());
            for (int i = 0; i < rs.size(); ++i){
                ret.add(rs.get(i).get("room_number").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.GET_ROOM_BY_TIME_FAIL);
        }
        return ret;
    }

}
