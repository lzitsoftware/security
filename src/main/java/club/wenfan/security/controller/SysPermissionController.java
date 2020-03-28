package club.wenfan.security.controller;

import club.wenfan.security.annotation.SysLoger;
import club.wenfan.security.entity.SysPermission;
import club.wenfan.security.exception.SystemException;
import club.wenfan.security.mapper.SysPermissionMapper;
import club.wenfan.security.rest.RestMsg;
import club.wenfan.security.service.SysPermissionService;
import club.wenfan.security.utils.StringUtils;
import club.wenfan.security.vo.ResponseInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by wenfan on 2019/12/31 17:14
 */
@RestController
@RequestMapping("/sys/admin/permissions")
public class SysPermissionController {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysPermissionService sysPermissionService;


    @GetMapping("/all")
    public ResponseInfo<SysPermission> getAllPermissions(){
        List<SysPermission> permissions = sysPermissionMapper.selectAll();
        List<SysPermission> list = Lists.newArrayList();
        setPermissionsList(0, permissions, list);

        return new ResponseInfo<>(RestMsg.SUCCESS, list);
    }


    private void setPermissionsList(int pId, List<SysPermission> permissionsAll, List<SysPermission> list) {
        for (SysPermission per : permissionsAll) {
            if (per.getParentId().equals(pId)) {
                list.add(per);
                if (permissionsAll.stream().filter(p -> p.getParentId().equals(per.getId())).findFirst() != null) {
                    setPermissionsList(per.getId(), permissionsAll, list);
                }
            }
        }
    }


    @GetMapping("/{roleId}")
    public ResponseInfo<SysPermission> getPermissionByRoleId(@PathVariable("roleId") Integer roleId){
        return new ResponseInfo<>(RestMsg.SUCCESS,sysPermissionMapper.listPermissionByRoleId(roleId));
    }



    @GetMapping("/allParent")
    public ResponseInfo<SysPermission> getAllMenuParents(){
        Example example = new Example(SysPermission.class);
        example.createCriteria().andEqualTo("parentId",0);
        List<SysPermission> sysPermissions = sysPermissionMapper.selectByExample(example);
       return ResponseInfo.success(sysPermissions);
    }

    @PostMapping("/edit/{permissionId}")
    @SysLoger(modulName = "更改权限")
    public ResponseInfo editPermission(
            @PathVariable("permissionId") String permissionId
            ,@RequestParam("name") String name
            ,@RequestParam("icon") String icon
            ,@RequestParam("url") String url
            ,@RequestParam("sort") Integer sort
            ,@RequestParam("parentId") Integer parentId
    ){
        SysPermission sysPermission = new SysPermission();
        sysPermission.setId(Integer.parseInt(permissionId));
        sysPermission.setHref(url);
        sysPermission.setName(name);
        sysPermission.setSort(sort);
        sysPermission.setIcon(icon);
        sysPermission.setParentId(parentId);
        sysPermissionService.editPermission(sysPermission);
        return ResponseInfo.success();
    }

    @GetMapping("/getPermission/{permissionId}")
    public ResponseInfo getPermissionById(@PathVariable("permissionId") String permissionId){
        SysPermission sysPermission = sysPermissionMapper.getPermissionById(Integer.valueOf(permissionId));
        return ResponseInfo.success(sysPermission);
    }


    @PostMapping("/addPermission")
    @SysLoger(modulName = "添加权限")

    public ResponseInfo addPermission(
            @RequestParam("name") String name
            ,@RequestParam("icon") String icon
            ,@RequestParam("url") String url
            ,@RequestParam("sort") Integer sort
            ,@RequestParam("parentId") Integer parentId){
        SysPermission sysPermission = new SysPermission();
        sysPermission.setParentId(parentId);
        sysPermission.setName(name);
        sysPermission.setHref(url);
        sysPermission.setSort(sort);
        // 菜单类型为0 -- 表示为菜单或者连接  不是权限
        sysPermission.setType(false);
        sysPermission.setPermission("");
        sysPermission.setIcon(icon);
        int i = sysPermissionService.addPermission(sysPermission);
        if (i < 0)
            return ResponseInfo.fail();
        return ResponseInfo.success();
    }

    @SysLoger(modulName = "删除权限")
    @GetMapping("/del/{permissionId}")
    public ResponseInfo deletePermission(@PathVariable("permissionId") String permissionId){
        sysPermissionService.delPermission(Integer.parseInt(permissionId));

        return ResponseInfo.success();
    }


}
