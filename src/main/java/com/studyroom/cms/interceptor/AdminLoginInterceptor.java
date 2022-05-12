package com.studyroom.cms.interceptor;

import com.studyroom.cms.result.Const;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();

        // 获取用户信息，如果没有用户信息直接返回提示信息
        Object adminFlag = session.getAttribute(Const.SAVE_ADMIN_LOGIN_MESSAGE_COLUMN);
        if (adminFlag == null) {
            response.getWriter().write(Const.ADMIN_NEED_LOGIN_MESSAGE);
            return false;
        }

        return true;
    }

}
