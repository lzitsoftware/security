/*
* SysLogs.java
* http://www.wenfan.club
* Copyright © 2020 wenfan All Rights Reserved
* 作者：wenfan
* QQ：571696215
* E-Mail：guwenfan@qq.com
* 2020-03-22 15:19 Created
*/ 
package club.wenfan.security.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_logs")
public class SysLogs implements Serializable {
    /**
     * 日志id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 操作用户id
     */
    @Column(name = "operate_user_id")
    private Integer operateUserId;

    /**
     * 错做的模块
     */
    @Column(name = "operate_modul_name")
    private String operateModulName;

    /**
     * 操作结果
     */
    @Column(name = "operate_result")
    private String operateResult;

    /**
     * 操作时间
     */
    @Column(name = "operate_time")
    private Date operateTime;

    /**
     * 操作浏览器
     */
    @Column(name = "operate_browser")
    private String operateBrowser;

    /**
     * 操作ip
     */
    @Column(name = "operate_ip")
    private String operateIp;

    /**
     * 操作者的系统
     */
    @Column(name = "operate_OS")
    private String operateOs;

    /**
     * 备注
     */
    private String description;

    /**
     * 操作者
     */
    @Column(name = "operate_user_name")
    private String operateUserName;

    /**
     * 操作参数id
     */
    @Column(name = "operate_params_id")
    private String operateParamsId;

    private static final long serialVersionUID = 1L;

    /**
     * 获取日志id
     *
     * @return id - 日志id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置日志id
     *
     * @param id 日志id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取操作用户id
     *
     * @return operate_user_id - 操作用户id
     */
    public Integer getOperateUserId() {
        return operateUserId;
    }

    /**
     * 设置操作用户id
     *
     * @param operateUserId 操作用户id
     */
    public void setOperateUserId(Integer operateUserId) {
        this.operateUserId = operateUserId;
    }

    /**
     * 获取错做的模块
     *
     * @return operate_modul_name - 错做的模块
     */
    public String getOperateModulName() {
        return operateModulName;
    }

    /**
     * 设置错做的模块
     *
     * @param operateModulName 错做的模块
     */
    public void setOperateModulName(String operateModulName) {
        this.operateModulName = operateModulName;
    }

    /**
     * 获取操作结果
     *
     * @return operate_result - 操作结果
     */
    public String getOperateResult() {
        return operateResult;
    }

    /**
     * 设置操作结果
     *
     * @param operateResult 操作结果
     */
    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    /**
     * 获取操作时间
     *
     * @return operate_time - 操作时间
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /**
     * 设置操作时间
     *
     * @param operateTime 操作时间
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取操作浏览器
     *
     * @return operate_browser - 操作浏览器
     */
    public String getOperateBrowser() {
        return operateBrowser;
    }

    /**
     * 设置操作浏览器
     *
     * @param operateBrowser 操作浏览器
     */
    public void setOperateBrowser(String operateBrowser) {
        this.operateBrowser = operateBrowser;
    }

    /**
     * 获取操作ip
     *
     * @return operate_ip - 操作ip
     */
    public String getOperateIp() {
        return operateIp;
    }

    /**
     * 设置操作ip
     *
     * @param operateIp 操作ip
     */
    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp;
    }

    /**
     * 获取操作者的系统
     *
     * @return operate_OS - 操作者的系统
     */
    public String getOperateOs() {
        return operateOs;
    }

    /**
     * 设置操作者的系统
     *
     * @param operateOs 操作者的系统
     */
    public void setOperateOs(String operateOs) {
        this.operateOs = operateOs;
    }

    /**
     * 获取备注
     *
     * @return description - 备注
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置备注
     *
     * @param description 备注
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取操作者
     *
     * @return operate_user_name - 操作者
     */
    public String getOperateUserName() {
        return operateUserName;
    }

    /**
     * 设置操作者
     *
     * @param operateUserName 操作者
     */
    public void setOperateUserName(String operateUserName) {
        this.operateUserName = operateUserName;
    }

    /**
     * 获取操作参数id
     *
     * @return operate_params_id - 操作参数id
     */
    public String getOperateParamsId() {
        return operateParamsId;
    }

    /**
     * 设置操作参数id
     *
     * @param operateParamsId 操作参数id
     */
    public void setOperateParamsId(String operateParamsId) {
        this.operateParamsId = operateParamsId;
    }
}