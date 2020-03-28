package club.wenfan.security.service;

import club.wenfan.security.entity.SysPermission;

/**
 * Created by wenfan on 2020/2/11 14:09
 */
public interface SysPermissionService {

    int updateRoleAndPermission(Integer roleId, String name, String desc, String permissionIds);

    int addRoleAndPermission(String name, String desc, String permissionIds);

    int editPermission(SysPermission sysPermission);

    int addPermission(SysPermission sysPermission);

    int delPermission(Integer permissionId);
}
