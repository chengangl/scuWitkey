package com.scu.scuWitkey.auth;

import com.scu.scuWitkey.core.domain.UserModel;
import com.scu.scuWitkey.core.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/4/8/0008.
 * 添加自己的拦截器实现AuthInterceptor继承于HandlerInterceptorAdapter
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);
            //没有声明需要权限,或者声明不验证权限
            if(authPassport == null || !authPassport.validate())
                return true;
            else{
                //在这里实现自己的权限验证逻辑
                HashMap<String, Object> resultMap = new HashMap<String, Object>();
                boolean userShow = false;
                HttpSession session = request.getSession();
                UserModel currentUser = (UserModel) session.getAttribute("currentUser");
                logger.info("AuthInterceptor preHandle---" + currentUser);
                resultMap.put("userShow", userShow);
                resultMap.put("user", currentUser);
                if(null != currentUser)
                    return true;
                else
                {
                    PrintWriter out = response.getWriter();
                    out.print(JsonUtil.toJson(resultMap));
                    return false;
                }
            }
        }
        else
            return true;
    }

}
