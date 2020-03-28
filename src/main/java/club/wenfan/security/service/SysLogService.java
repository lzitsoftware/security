package club.wenfan.security.service;

import club.wenfan.security.entity.PageBean;
import club.wenfan.security.entity.SysLogs;
import club.wenfan.security.entity.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wenfan on 2020/3/22 15:22
 */
public interface SysLogService {

    PageBean<SysLogs> getSysLogsByPage(int pageIndex, int pageSize,String operateUserName,String startDate,String endDate,String status);

    Integer save(HttpServletRequest request,String operateUserName,String operateModul);

    void saveStatusAfterOperation(Integer logId,String result);
}
