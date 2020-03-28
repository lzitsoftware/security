/*
* SysLogsMapper.java
* http://www.wenfan.club
* Copyright © 2020 wenfan All Rights Reserved
* 作者：wenfan
* QQ：571696215
* E-Mail：guwenfan@qq.com
* 2020-03-22 15:19 Created
*/ 
package club.wenfan.security.mapper;


import club.wenfan.security.entity.SysLogs;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
@org.apache.ibatis.annotations.Mapper
public interface SysLogsMapper extends Mapper<SysLogs> {

    // 1. 成功 2.失败
    @Update("update sys_logs set operate_result = #{status} where id = #{logId}")
    void setStatusByLogId(@Param("logId") Integer logId,@Param("status") String status);

    List<SysLogs> getSysLogByCustomCondition(@Param("operateUserName") String operateUserName,@Param("startDate") String startDate, @Param("endDate") String endDate,@Param("status")String status);

}