package com.bishe.club.dao;

import com.bishe.club.entity.TeamTypes;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 数据层处理接口
 * 社团类型
 */
@Repository("teamTypesDao")
public interface TeamTypesDao extends BaseMapper<TeamTypes> {
	

}