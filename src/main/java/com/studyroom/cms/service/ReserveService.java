package com.studyroom.cms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReserveService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

}
