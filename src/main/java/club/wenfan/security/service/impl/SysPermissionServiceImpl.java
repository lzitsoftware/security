package club.wenfan.security.service.impl;

import club.wenfan.security.entity.SysPermission;
import club.wenfan.security.entity.SysRole;
import club.wenfan.security.entity.SysRolePermission;
import club.wenfan.security.entity.SysUser;
import club.wenfan.security.exception.SystemException;
import club.wenfan.security.mapper.SysPermissionMapper;
import club.wenfan.security.mapper.SysRoleMapper;
import club.wenfan.security.mapper.SysRolePermissionMapper;
import club.wenfan.security.rest.RestMsg;
import club.wenfan.security.service.SysPermissionService;
import club.wenfan.security.service.SysRoleService;
import club.wenfan.security.utils.SysUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by wenfan on 2020/2/11 14:10
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {


    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    @Transactional
    public int updateRoleAndPermission(Integer roleId, String name, String desc, String permissionIds) {

        SysRole sysRole = new SysRole();
        sysRole.setId(roleId);
        sysRole.setDescription(desc);
        sysRole.setName(name);
        sysRole.setUpdatetime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(sysRole);

        String permissionIdsStr [] = permissionIds.split(",");
        Integer permissions[] = new Integer[permissionIdsStr.length];
        for (int i = 0; i<permissionIdsStr.length ;i++ )
            permissions[i] = Integer.parseInt(permissionIdsStr[i]);
        sysRolePermissionMapper.deletePermissionIdByRoleId(roleId);
        return sysRolePermissionMapper.updatePermissionByRoleId(roleId,Arrays.asList(permissions));
    }

    @Override
    public int addRoleAndPermission(String name, String desc, String permissionIds) {
        SysRole sysRole = new SysRole();
        sysRole.setDescription(desc);
        sysRole.setName(name);
        sysRole.setUpdatetime(new Date());
        sysRole.setCreatetime(new Date());
        sysRoleMapper.insert(sysRole);

        String permissionIdsStr [] = permissionIds.split(",");
        Integer permissions[] = new Integer[permissionIdsStr.length];
        for (int i = 0; i<permissionIdsStr.length ;i++ )
            permissions[i] = Integer.parseInt(permissionIdsStr[i]);
        return sysRolePermissionMapper.updatePermissionByRoleId(sysRole.getId(),Arrays.asList(permissions));
    }

    @Override
    public int editPermission(SysPermission sysPermission) {
        int i = sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);
        if (i < 0)
            throw new SystemException(RestMsg.INNER_ERROR);
        return i;
    }

    @Override
    @Transactional
    public int addPermission(SysPermission sysPermission) {
        SysUser currentUser = SysUserUtil.getLoginUser();
        SysRole sysRole = sysRoleMapper.getRolesByUserId(currentUser.getId()).get(0);
        int i = sysPermissionMapper.insert(sysPermission);
        SysRolePermission sysRolePermission = new SysRolePermission();
        sysRolePermission.setRoleid(sysRole.getId());
        sysRolePermission.setPermissionid(sysPermission.getId());
        int j = sysRolePermissionMapper.insert(sysRolePermission);


        return i&j;
    }

    @Override
    @Transactional
    public int delPermission(Integer permissionId) {
        // 删除 与角色对应的关系
        int i = sysRolePermissionMapper.deletePermissionIdByRoleId(permissionId);
        // 删除实体权限
        int j = sysPermissionMapper.delPermissionById(permissionId);
        return i&j;
    }
}
