package club.wenfan.security.service;

import club.wenfan.security.dto.SysUserDto;
import club.wenfan.security.entity.PageBean;
import club.wenfan.security.entity.SysUser;
import com.github.pagehelper.Page;

/**
 * Created by wenfan on 2019/12/28 15:11
 */
public interface UserService {

    PageBean<SysUser> getSysUsersByPage(int pageIndex, int pageSize);

    int editSysUser(SysUser sysUser, String roleId);

    boolean deleteSysUser(Integer userId);

    PageBean<SysUser> getSysUserByNickNameAndPage(int pageIndex, int pageSize, String nickname);

    PageBean<SysUser> getSysUserByStatusAndPage(int pageIndex, int pageSize, int status);

    SysUser addSysUser(SysUserDto userDto);

    SysUser getSysUser(String username);

    PageBean<SysUser> getSysUsersByPageAndUsername(int pageIndex, int pageSize, String username);

    boolean changePassword(String ussername, String oldPassword, String newPassword);
}
