package club.wenfan.security.controller;

import club.wenfan.security.annotation.SysLoger;
import club.wenfan.security.dto.SysRoleAndItsPermission;
import club.wenfan.security.entity.PageBean;
import club.wenfan.security.entity.SysRole;
import club.wenfan.security.exception.SystemException;
import club.wenfan.security.mapper.SysRoleMapper;
import club.wenfan.security.mapper.SysRoleUserMapper;
import club.wenfan.security.rest.RestMsg;
import club.wenfan.security.service.SysPermissionService;
import club.wenfan.security.service.SysRoleService;
import club.wenfan.security.vo.EntityVO;
import club.wenfan.security.vo.ResponseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wenfan on 2020/1/14 13:31
 * 系统角色
 */
@RestController
@RequestMapping("/sys/admin/role")
public class SysRoleController {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @GetMapping("/allRole")
    public ResponseInfo<SysRole> getAllSysRole() {
        List<SysRole> roles = sysRoleMapper.selectAll();
        return new ResponseInfo("200", "ok", roles);
    }

    @GetMapping("/allRoleByPage")
    @PreAuthorize("hasAnyAuthority('sys:role:query')")
    public EntityVO<SysRole> getAllSysRoleByPage(HttpServletRequest request) {
        String strPageIndex = request.getParameter("pageIndex");
        String strPageSize = request.getParameter("pageSize");
        int recordsTotal = 0;
        int recordsFiltered;
        int pageIndex = Integer.parseInt(strPageIndex);
        int pageSize = Integer.parseInt(strPageSize);
        PageBean<SysRole> page = sysRoleService.getSysRolesByPage(pageIndex, pageSize);
        recordsTotal = page.getTotalNum();
        recordsFiltered = recordsTotal;
        return new EntityVO(recordsTotal, recordsFiltered, page.getItems());
    }


    @GetMapping("/getSingleRole/{id}")
    public ResponseInfo getSysRoleAndItsPermissionsById(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))
            throw new IllegalArgumentException();
        SysRoleAndItsPermission rs = sysRoleService.getRoleAndPerByRoleId(Integer.parseInt(id));
        return ResponseInfo.success(rs);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('sys:role:add')")
    @SysLoger(modulName = "添加角色和权限")
    public ResponseInfo addRoleAndItsPermission(
            @RequestParam("selectedPermissionId") String selectedPermissionId
            ,@RequestParam("name") String name
            ,@RequestParam("description") String description){
        if (club.wenfan.security.utils.StringUtils.isAllEmptys(name,description,selectedPermissionId))
            throw new SystemException(RestMsg.ERROR_PARAMETER);
        sysPermissionService.addRoleAndPermission(name,description,selectedPermissionId);
        return new ResponseInfo(RestMsg.SUCCESS);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('sys:role:edit')")
    @SysLoger(modulName = "更改角色的权限")
    public ResponseInfo updateRoleAndItsPermission(
            @RequestParam("roleId") Integer roleId
            ,@RequestParam("selectedPermissionId") String selectedPermissionId
            ,@RequestParam("name") String name
            ,@RequestParam("description") String description){
        if (club.wenfan.security.utils.StringUtils.isAllEmptys(name,description,selectedPermissionId,roleId.toString()))
            throw new SystemException(RestMsg.ERROR_PARAMETER);
        sysPermissionService.updateRoleAndPermission(roleId,name,description,selectedPermissionId);
        return new ResponseInfo(RestMsg.SUCCESS);
    }


    @SysLoger(modulName = "删除角色及权限")
    @PreAuthorize("hasAnyAuthority('sys:role:delete')")
    @DeleteMapping("/{roleId}")
    public ResponseInfo deleteRoleAndItsPermission(@PathVariable("roleId") String roleId){
        Integer id = Integer.parseInt(roleId);
        if (sysRoleUserMapper.getRoleUserByRoleId(id).size() > 0)
            return ResponseInfo.restMsg(RestMsg.HAVE_USER_FAILURE);
        if (sysRoleService.deleteRoleAndPermission(id) > 0)
            return ResponseInfo.restMsg(RestMsg.DEL_SUCCESS);
        return ResponseInfo.restMsg(RestMsg.DEL_FAILURE);
    }

}
