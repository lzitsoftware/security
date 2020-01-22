package club.wenfan.security.controller;

import club.wenfan.security.entity.SysPermission;
import club.wenfan.security.mapper.SysPermissionMapper;
import club.wenfan.security.vo.EntityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wenfan on 2019/12/31 17:14
 */
@RestController("permissions")
public class PermissionController {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;


}
