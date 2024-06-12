package com.bishe.club.dao;

import com.bishe.club.entity.Teams;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 数据层处理接口
 * 社团信息
 */
@Repository("teamsDao")
public interface TeamsDao extends BaseMapper<Teams> {
	

}