package com.bishe.club.dao;

import com.bishe.club.entity.ActiveLogs;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 数据层处理接口
 * 报名记录
 */
@Repository("activeLogsDao")
public interface ActiveLogsDao extends BaseMapper<ActiveLogs> {
	

}