package com.liu.security.aop;

import com.liu.security.annotation.HasPermission;
import com.liu.security.model.Permission;
import com.liu.security.model.User;
import com.liu.security.util.HttpRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Component
@Aspect
public class PermissionAop {
    private static Logger logger = LoggerFactory.getLogger(PermissionAop.class);

    @Around("@annotation(com.liu.security.annotation.HasPermission)")
    public Object before(ProceedingJoinPoint point){
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        HttpServletRequest request = HttpRequest.getRequest();
        HttpSession session =request.getSession();
        User user =(User) session.getAttribute("user");
        if(user == null){
            logger.error("请登录");
            return null;
        }else {
            HasPermission hasPermission = methodSignature.getMethod().getAnnotation(HasPermission.class);
            String value = hasPermission.value();
            if(!hasPermission(value, user)){
                logger.error("没有权限访问");
                return null;
            }
            try {
                return point.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        }
    }

    private boolean hasPermission(String value, User user){
        Set<Permission> permissions = user.getRole().getPermissions();
        for (Permission permission : permissions){
            if(permission.getUrl().equals(value)){
                return true;
            }
        }
        return false;
    }
}
