package club.wenfan.security.service;

import club.wenfan.security.dto.SysRoleAndItsPermission;
import club.wenfan.security.entity.PageBean;
import club.wenfan.security.entity.SysRole;

/**
 * Created by wenfan on 2020/1/19 13:14
 */
public interface SysRoleService {

    PageBean<SysRole> getSysRolesByPage(int pageIndex, int pageSize);

    SysRoleAndItsPermission getRoleAndPerByRoleId(Integer roleId);

    int deleteRoleAndPermission(Integer roleIds);

}
