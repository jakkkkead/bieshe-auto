package com.example.auto.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yun.comom.UserUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandler implements HandlerInterceptor {
    @Reference
    private UserUtil userUtil;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Object user =  httpServletRequest.getSession().getAttribute("user");
        if(user == null){
         httpServletResponse.sendRedirect("http://localhost:8083/tologin");
            //httpServletRequest.getRequestDispatcher("/tologin");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
