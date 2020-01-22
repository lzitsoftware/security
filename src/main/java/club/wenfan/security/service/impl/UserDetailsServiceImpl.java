package club.wenfan.security.service.impl;

import club.wenfan.security.entity.SysPermission;
import club.wenfan.security.entity.SysUser;
import club.wenfan.security.mapper.SysPermissionMapper;
import club.wenfan.security.mapper.SysUserMapper;
import club.wenfan.security.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wenfan on 2019/12/28 15:10
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.getSysUserByUsername(username);
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("用户名不存在");
        }
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);

        List<SysPermission> permissions = permissionMapper.listPermissionsByUserId(user.getId());
        loginUser.setPermissions(permissions);
        return loginUser;
    }
}
