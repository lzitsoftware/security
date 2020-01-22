/*
 * SysPermissionMapper.java
 * http://www.wenfan.club
 * Copyright © 2019 wenfan All Rights Reserved
 * 作者：wenfan
 * QQ：571696215
 * E-Mail：guwenfan@qq.com
 * 2019-12-28 11:18 Created
 */
package club.wenfan.security.mapper;


import club.wenfan.security.entity.SysPermission;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Component
@org.apache.ibatis.annotations.Mapper
public interface SysPermissionMapper extends Mapper<SysPermission> {

    List<SysPermission> listPermissionsByUserId(Integer userId);

    List<SysPermission> listPermissionByRoleId(Integer roleId);

}