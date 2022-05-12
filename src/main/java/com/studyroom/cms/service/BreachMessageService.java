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

@Service
public class BreachMessageService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoggerUtils loggerUtils;

    public void batchAdd(List<HashMap<String,String>> data) throws customException{
        if (data.isEmpty()){
            return;
        }

        String sql = "insert into `breach_message` (`student_number`,`breach_time`,`breach_order_id`) values (?,?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < data.size(); ++i){
            batchArgs.add(new Object[]{data.get(i).get("student_number"), data.get(i).get("breach_time"), data.get(i).get("order_id")});
        }
        try{
            int[] effectRow = jdbcTemplate.batchUpdate(sql,batchArgs);
            loggerUtils.info("成功写入违约记录" + effectRow.length + "条");
        }catch (Exception e){
            e.printStackTrace();
            throw new customException(ExceptionCodeEnum.BATCH_ADD_BREACH_MESSAGE_FAIL);
        }
    }
}
