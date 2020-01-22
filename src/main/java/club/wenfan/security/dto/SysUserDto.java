package club.wenfan.security.dto;

import club.wenfan.security.entity.SysRole;
import club.wenfan.security.entity.SysUser;

import java.util.List;

/**
 * Created by wenfan on 2019/12/28 15:17
 */
public class SysUserDto extends SysUser {
    private static final long serialVersionUID = -184009306207076712L;

    private List<SysRole> roles;

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}
