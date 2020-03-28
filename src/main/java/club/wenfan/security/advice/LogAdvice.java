package club.wenfan.security.advice;

import club.wenfan.security.annotation.SysLoger;
import club.wenfan.security.exception.SystemException;
import club.wenfan.security.rest.RestMsg;
import club.wenfan.security.service.SysLogService;
import club.wenfan.security.utils.SysUserUtil;
import club.wenfan.security.vo.LoginUser;
import club.wenfan.security.vo.ResponseInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wenfan on 2020/3/22 11:10
 */
@Aspect
@Component
public class LogAdvice {

    @Autowired
    private SysLogService sysLogService;

    @Around(value = "@annotation(around)")
    public Object logSave(ProceedingJoinPoint point, SysLoger around) throws Throwable {
        LoginUser user = SysUserUtil.getLoginUser();
        if (user == null)
            throw new SystemException(RestMsg.NOT_LOGIN);
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        // pointcut before
        Integer logId = sysLogService.save(request,user.getUsername(),around.modulName());
        // run
        ResponseInfo result =(ResponseInfo) point.proceed();
        // pointcut after
        if (result.getCode().equals("200"))
            sysLogService.saveStatusAfterOperation(logId,"成功");
        else
            sysLogService.saveStatusAfterOperation(logId,"失败");
        return result;
    }

}
