package com.bishe.club.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bishe.club.dao.ActiveLogsDao;
import com.bishe.club.dao.ActivitiesDao;
import com.bishe.club.dao.TeamsDao;
import com.bishe.club.entity.ActiveLogs;
import com.bishe.club.entity.Activities;
import com.bishe.club.entity.Teams;
import com.bishe.club.utils.PageData;
import com.bishe.club.service.ActivitiesService;
import com.bishe.club.utils.DateUtils;
import com.bishe.club.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("activitiesService")
public class ActivitiesServiceImpl implements ActivitiesService {

    @Autowired
    private TeamsDao teamsDao;

    @Autowired
    private ActiveLogsDao activeLogsDao;

    @Autowired
    private ActivitiesDao activitiesDao;

    @Override
    @Transactional
    public void add(Activities activities) {

        activitiesDao.insert(activities);

        Teams teams = teamsDao.selectById(activities.getTeamId());

        ActiveLogs activeLog = new ActiveLogs();
        activeLog.setId(IDUtils.makeIDByCurrent());
        activeLog.setActiveId(activities.getId());
        activeLog.setUserId(teams.getManager());
        activeLog.setCreateTime(DateUtils.getNowDate());
        activeLogsDao.insert(activeLog);
    }

    @Override
    @Transactional
    public void update(Activities activities) {

        activitiesDao.updateById(activities);
    }

    @Override
    @Transactional
    public void delete(Activities activities) {

        QueryWrapper<ActiveLogs> qw = new QueryWrapper<ActiveLogs>();
        qw.eq("active_id", activities.getId());
        activeLogsDao.delete(qw);

        activitiesDao.deleteById(activities);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Activities getOne(String id) {

        Activities activities = activitiesDao.selectById(id);

        return activities;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageAll(Long pageIndex, Long pageSize, String activeName, String teamName) {

        Page<Map<String, Object>> page =
                activitiesDao.qryPageAll(new Page<Map<String, Object>>(pageIndex, pageSize), activeName, teamName);

        return parsePage(page);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public PageData getPageByUserId(Long pageIndex, Long pageSize, String userId, String activeName, String teamName) {

        Page<Map<String, Object>> page =
                activitiesDao.qryPageByMemId(new Page<Map<String, Object>>(pageIndex, pageSize), userId, activeName, teamName);

        return parsePage(page);
    }

    /**
     * 转化分页查询的结果
     */
    public PageData parsePage(Page<Map<String, Object>> p) {

        PageData pageData = new PageData(p.getCurrent(), p.getSize(), p.getTotal(), p.getRecords());

        return pageData;
    }
}