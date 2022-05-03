package com.studyroom.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getPassword(String name){
        String sql = "select password from administrator where name='" + name + "' and is_valid=1";
        String password = jdbcTemplate.queryForObject(sql,String.class);
        return password;
    }

}
