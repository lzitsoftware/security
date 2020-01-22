/*
 * SysRoleUser.java
 * http://www.wenfan.club
 * Copyright © 2019 wenfan All Rights Reserved
 * 作者：wenfan
 * QQ：571696215
 * E-Mail：guwenfan@qq.com
 * 2019-12-28 11:18 Created
 */
package club.wenfan.security.entity;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "sys_role_user")
public class SysRoleUser implements Serializable {


    @Id
    @Column(name = "userId")
    private Integer userid;

    @Id
    @Column(name = "roleId")
    private Integer roleid;


    private static final long serialVersionUID = 1L;

    /**
     * @return userId
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * @param userid
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * @return roleId
     */
    public Integer getRoleid() {
        return roleid;
    }

    /**
     * @param roleid
     */
    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

}