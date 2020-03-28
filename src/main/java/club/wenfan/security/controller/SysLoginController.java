package club.wenfan.security.controller;

import club.wenfan.security.annotation.SysLoger;
import club.wenfan.security.entity.PageBean;
import club.wenfan.security.entity.SysLogs;
import club.wenfan.security.entity.SysUser;
import club.wenfan.security.service.SysLogService;
import club.wenfan.security.utils.AgentUtils;
import club.wenfan.security.vo.EntityVO;
import club.wenfan.security.vo.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.Addressing;

/**
 * Created by wenfan on 2019/12/29 19:14
 */
@RestController
@RequestMapping("/sys/admin/log")
public class SysLoginController {


    @Autowired
    private SysLogService sysLogService;


    @PostMapping("/allLogs")
    @PreAuthorize("hasAnyAuthority('sys:user:query')")
    public EntityVO getAllUsers(@RequestParam("pageIndex") Integer pageIndex,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("operateUserName") String operateUserName,
                                @RequestParam("startDate") String startDate,
                                @RequestParam("endDate") String endDate,
                                @RequestParam("status") String status) {
        int recordsTotal,recordsFiltered;
        PageBean<SysLogs> page = sysLogService.getSysLogsByPage(pageIndex, pageSize,operateUserName,startDate,endDate,status);
        recordsTotal = page.getTotalNum();
        recordsFiltered = recordsTotal;
        return new EntityVO(recordsTotal, recordsFiltered, page.getItems());
    }

}
