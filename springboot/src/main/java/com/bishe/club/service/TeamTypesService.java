package com.bishe.club.service;

import com.bishe.club.entity.TeamTypes;
import com.bishe.club.utils.PageData;

import java.util.List;

/**
 * 业务层处理
 * 社团类型
 */
public interface TeamTypesService extends BaseService<TeamTypes, String> {

	/**
	 * 检查指定的社团类型是否可以删除
	 * 如果存在关联社团不能删除
	 * @param typeId 社团ID
	 * @return
	 */
	public Boolean isRemove(String typeId);

	/**
	 * 获取全部的社团类型信息
	 * @return
	 */
	public List<TeamTypes> getAll();

	/**
	 * 分页查询社团类型信息
	 * @param pageIndex 当前页码
	 * @param pageSize 每页数据量
	 * @param teamTypes 模糊查询条件
	 * @return
	 */	
	public PageData getPageInfo(Long pageIndex, Long pageSize, TeamTypes teamTypes);
}