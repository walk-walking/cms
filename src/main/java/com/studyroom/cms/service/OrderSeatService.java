package com.studyroom.cms.service;

import com.studyroom.cms.entity.OrderRule;
import com.studyroom.cms.entity.OrderSeat;
import com.studyroom.cms.result.ExceptionCodeEnum;
import com.studyroom.cms.result.customException;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderSeatService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoggerUtils loggerUtils;

    @Autowired OrderRuleService orderRuleService;

    public void resetStatus() throws customException{
        try{
            String sql = "update `order_seat` set `order_status` = -1 where `order_status` in (0,1)";
            int effectRow = jdbcTemplate.update(sql);
            loggerUtils.info("被重置状态的座位数量: " + effectRow);
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.RESET_ORDER_SEAT_STATUS_FAIL);
        }
    }

    public void deleteUnruledSeat(List<String> ruledRooms) throws customException{
        if (ruledRooms.isEmpty()){
            return;
        }
        try{
            StringBuffer sql = new StringBuffer();
            sql.append("delete from `order_seat` where `room_number` not in ");
            sql.append("(");
            int size = ruledRooms.size();
            for (int i = 0; i < size; ++i){
                sql.append("'");
                sql.append(ruledRooms.get(i));
                sql.append("'");
                if(i != (size - 1)){
                    sql.append(",");
                }
            }
            sql.append(")");
            int effectRow = jdbcTemplate.update(sql.toString());
            loggerUtils.info("被删除的座位数量: " + effectRow);
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.DELETE_UNRULE_SEAT_FAIL);
        }
    }

    public void updateSeatOrderRule(List<String> latestRuledRooms) throws customException{
        if(latestRuledRooms.isEmpty()){
            return;
        }
        try{
            String sql = "update `order_seat` set `order_start_time`=?,order_end_time=?,order_max_time=? where `room_number`=?";
            for(int i = 0; i < latestRuledRooms.size(); ++i){
                OrderRule orderRule = orderRuleService.getOneByRoomNumber(latestRuledRooms.get(i));
                if (orderRule != null){
                    jdbcTemplate.update(new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                            //指定主键
                            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
                            preparedStatement.setString(1, orderRule.getOpenTime());
                            preparedStatement.setString(2, orderRule.getCloseTime());
                            preparedStatement.setInt(3, orderRule.getSingleOrderTime());
                            preparedStatement.setString(4, orderRule.getRoomNumber());
                            return preparedStatement;
                        }
                    });
                    loggerUtils.info("自习室 "+ orderRule.getRoomNumber() + " 的最新规则被应用到座位");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.UPDATE_ORDER_RULE_TO_SEAT_FAIL);
        }
    }

    public void delOrAddSeat(List<HashMap<String,String>> latestModSeats) throws customException{
        if(latestModSeats.isEmpty()){
            return;
        }

        String insertSql = "insert into `order_seat` " +
                "(`room_number`,`seat_number`,`building`,`order_start_time`,`order_end_time`,`order_max_time`) " +
                "values(?,?,?,?,?,?)";

        try{
            for (int i = 0; i < latestModSeats.size(); ++i){
                HashMap<String,String> oneSeat = latestModSeats.get(i);
                String roomNumber = oneSeat.get("room_number");
                String seatNumber = oneSeat.get("number");
                if (oneSeat.get("is_valid").equals("0")){
                    String delSql = "delete from `order_seat` where `room_number`='"+
                            roomNumber + "' and `seat_number`='"+ seatNumber + "'";
                    int effectRow = jdbcTemplate.update(delSql);
                    if (effectRow > 0){
                        loggerUtils.info("删除自习室 " + roomNumber + " 下的 "+ seatNumber + " 座位");
                    }
                }else{
                    OrderSeat orderSeat = getOneByMixNumber(roomNumber,seatNumber);
                    if (orderSeat == null){
                        OrderRule orderRule = orderRuleService.getOneByRoomNumber(roomNumber);
                        if (orderRule != null){
                            jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    //指定主键
                                    PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(insertSql);
                                    preparedStatement.setString(1, roomNumber);
                                    preparedStatement.setString(2, seatNumber);
                                    preparedStatement.setString(3, oneSeat.get("building"));
                                    preparedStatement.setString(4, orderRule.getOpenTime());
                                    preparedStatement.setString(5, orderRule.getCloseTime());
                                    preparedStatement.setInt(6, orderRule.getSingleOrderTime());
                                    return preparedStatement;
                                }
                            });

                            loggerUtils.info("添加自习室 " + roomNumber + " 下的 " + seatNumber + " 座位");
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.DELETE_OR_ADD_ORDER_SEAT_FAIL);
        }
    }

    public OrderSeat getOneByMixNumber(String roomNumber, String seatNumber) throws Exception{
        OrderSeat orderSeat = null;
        String sql = "select `id`,`seat_number`,`room_number`,`building`,`order_status`,`order_start_time`,`order_end_time`,`order_max_time` from order_seat where ";
        sql += "`room_number`='" +roomNumber +"' and `seat_number`='" + seatNumber +"'";
        try{
            orderSeat = jdbcTemplate.queryForObject(sql, new RowMapper<OrderSeat>() {
                @Override
                public OrderSeat mapRow(ResultSet rs, int rowNum) throws SQLException {
                    OrderSeat row = new OrderSeat();
                    row.setId(rs.getInt("id"));
                    row.setSeatNumber(rs.getString("seat_number"));
                    row.setRoomNumber(rs.getString("room_number"));
                    row.setBuilding(rs.getString("building"));
                    row.setOrderStatus(rs.getInt("order_status"));
                    row.setOrderStartTime(rs.getString("order_start_time"));
                    row.setOrderEndTime(rs.getString("order_end_time"));
                    row.setOrderMaxTime(rs.getInt("order_max_time"));
                    return row;
                }
            });
        }catch (EmptyResultDataAccessException e){
            //未找到该可预约座位
        }catch (Exception e){
            //其他异常错误
            e.printStackTrace();
            throw new Exception("mysql execute error");
        }

        return orderSeat;
    }

    public void modSeatStatusByRoom(List<String> roomList,int newStatus) throws customException{
        if(roomList.isEmpty()){
            return;
        }
        try{
            StringBuffer sql = new StringBuffer();
            sql.append("update `order_seat` set `order_status` = ");
            sql.append(newStatus);
            sql.append(" where `room_number` in ");
            sql.append("(");
            for (int i = 0; i < roomList.size(); ++i){
                sql.append("'");
                sql.append(roomList.get(i));
                sql.append("'");
                sql.append(",");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(")");
            int effectRow = jdbcTemplate.update(sql.toString());
            loggerUtils.info("状态修改为"+ newStatus + "的座位数量: " + effectRow);
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.MOD_SEAT_STATUS_BY_ROOM_FAIL);
        }
    }

    public void releaseExpiringSeat(List<HashMap<String,String>> expiringSeats) throws customException{
        if (expiringSeats.isEmpty()){
            return;
        }

        String sql = "update `order_seat` set `order_status`=0 " +
                "where `seat_number`=? and room_number=? and order_status=1";
        try{
            for (int i = 0; i < expiringSeats.size(); ++i){
                String roomNumber = expiringSeats.get(i).get("room_number");
                String seatNumber = expiringSeats.get(i).get("seat_number");

                int effectRow = jdbcTemplate.update(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        //指定主键
                        PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
                        preparedStatement.setString(1, seatNumber);
                        preparedStatement.setString(2, roomNumber);
                        return preparedStatement;
                    }
                });

                if(effectRow > 0){
                    loggerUtils.info("自习室 " + roomNumber + " 下的 " + seatNumber + " 座位被释放");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.RELEASE_EXPIRING_SEAT_FAIL);
        }
    }
}
