package club.wenfan.security.service.impl;

import club.wenfan.security.dto.SysRoleAndItsPermission;
import club.wenfan.security.entity.PageBean;
import club.wenfan.security.entity.SysPermission;
import club.wenfan.security.entity.SysRole;
import club.wenfan.security.entity.SysUser;
import club.wenfan.security.exception.SystemException;
import club.wenfan.security.mapper.SysPermissionMapper;
import club.wenfan.security.mapper.SysRoleMapper;
import club.wenfan.security.mapper.SysRoleUserMapper;
import club.wenfan.security.service.SysRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wenfan on 2020/1/19 13:15
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public PageBean<SysRole> getSysRolesByPage(int pageIndex, int pageSize) {
        Page page = PageHelper.startPage(pageIndex, pageSize);
        List<SysRole> allSysRoles = roleMapper.selectAll();
        int count = allSysRoles.size();
        PageBean<SysRole> pageBean = new PageBean<>(pageIndex, pageSize, count);
        pageBean.setItems(allSysRoles);
        pageBean.setTotalNum((int) page.getTotal());
        return pageBean;
    }

    @Override
    public SysRoleAndItsPermission getRoleAndPerByRoleId(Integer roleId) {

        SysRole sysRole = roleMapper.selectByPrimaryKey(roleId);
        if (sysRole == null)
            throw new SystemException("系统无该角色");
        List<SysPermission> permissions = sysPermissionMapper.listPermissionByRoleId(roleId);
        return new SysRoleAndItsPermission(sysRole, permissions);
    }


}
