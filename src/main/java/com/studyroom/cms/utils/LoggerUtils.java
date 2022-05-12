package com.studyroom.cms.utils;

import com.studyroom.cms.CmsApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtils {

    private static final Logger logger = LoggerFactory.getLogger(CmsApplication.class);

    public void debug(String message){
        logger.debug(message);
    }

    public void trace(String message){
        logger.trace(message);
    }

    public void info(String message){
        logger.info(message);
    }

    public void warn(String message){
        logger.warn(message);
    }

    public void error(String message){
        logger.error(message);
    }
}
