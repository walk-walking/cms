package com.studyroom.cms.service;

import com.studyroom.cms.result.ExceptionCodeEnum;
import com.studyroom.cms.result.customException;
import com.studyroom.cms.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.println(sql.toString());
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
}
