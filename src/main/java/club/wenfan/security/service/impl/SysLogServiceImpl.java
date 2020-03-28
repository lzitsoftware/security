package club.wenfan.security.service.impl;

import club.wenfan.security.entity.PageBean;
import club.wenfan.security.entity.SysLogs;
import club.wenfan.security.entity.SysUser;
import club.wenfan.security.exception.SystemException;
import club.wenfan.security.mapper.SysLogsMapper;
import club.wenfan.security.rest.RestMsg;
import club.wenfan.security.service.SysLogService;
import club.wenfan.security.utils.AgentUtils;
import club.wenfan.security.utils.SysUserUtil;
import club.wenfan.security.vo.LoginUser;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import nl.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wenfan on 2020/3/22 15:28
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    private Logger log =LoggerFactory.getLogger("adminLogger");

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    public SysLogsMapper sysLogsMapper;

    @Override
    public PageBean<SysLogs> getSysLogsByPage(int pageIndex, int pageSize,String operateUserName,String startDate,String endDate,String status) {
        Page page = PageHelper.startPage(pageIndex, pageSize);
        List<SysLogs> allSysLogs = sysLogsMapper.getSysLogByCustomCondition(operateUserName,startDate,endDate,status);
        int count = allSysLogs.size();
        PageBean<SysLogs> pageBean = new PageBean<>(pageIndex, pageSize, count);
        pageBean.setItems(allSysLogs);
        pageBean.setTotalNum((int) page.getTotal());
        return pageBean;
    }

    /**
     *  异步插入操作日志
     * @param request
     * @param operateUserName
     * @param operateModul
     */
    @Async
    @Override
    public Integer save(HttpServletRequest request,String operateUserName, String operateModul) {

        UserAgent agent = AgentUtils.getUserAgent(request);
        String operateOS = agent.getOperatingSystem().getName()+" "+agent.getOperatingSystem().getDeviceType();
        String operateBrowser = agent.getBrowser().getName()+" "+agent.getBrowser().getBrowserType();
        String ipAddress = AgentUtils.getIpAddress(request);
        Map<String,String[]> parameMap = request.getParameterMap();
        String paramsStr = JSONObject.toJSONString(parameMap);
        String paramId = UUID.randomUUID().toString().replace("-","");
        SysLogs sysLog = new SysLogs();
        sysLog.setOperateBrowser(operateBrowser);
        sysLog.setOperateOs(operateOS);
        sysLog.setOperateIp(ipAddress);
        sysLog.setOperateTime(new Date());
        sysLog.setOperateModulName(operateModul);
        sysLog.setOperateUserName(operateUserName);
        sysLog.setOperateParamsId(paramId);
        sysLog.setOperateModulName(operateModul);
        sysLogsMapper.insertSelective(sysLog);
        redisTemplate.opsForValue().set("logs:"+paramId,paramsStr);
        log.info("before logged!");
        return sysLog.getId();
    }

    @Async
    @Override
    public void saveStatusAfterOperation(Integer logId, String result) {
        sysLogsMapper.setStatusByLogId(logId,result);
        log.info("after logged!");
    }
}
