package com.bishe.club.controller;

import com.bishe.club.entity.Teams;
import com.bishe.club.entity.Users;
import com.bishe.club.utils.CacheHandle;
import com.bishe.club.utils.PageData;
import com.bishe.club.utils.R;
import com.bishe.club.service.UsersService;
import com.bishe.club.utils.DateUtils;
import com.bishe.club.utils.IDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bishe.club.service.TeamsService;

import java.util.List;

/**
 * 系统请求响应控制器
 * 社团信息
 */
@Controller
@RequestMapping("/teams")
public class TeamsController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(TeamsController.class);

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private UsersService usersService;

    @Autowired
    private TeamsService teamsService;

    @RequestMapping("")
    public String index() {

        return "pages/Teams";
    }

    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id) {

        Log.info("查找指定社团信息，ID：{}", id);
        Teams teams = teamsService.getOne(id);
        return R.successData(teams);
    }

    @GetMapping("/all")
    @ResponseBody
    public R getAll(){

        Log.info("获取全部的社团");
        List<Teams> list = teamsService.getAll();
        return R.successData(list);
    }

    @GetMapping("/man")
    @ResponseBody
    public R getListByManId(String manId){

        Log.info("获取指定社团管理员相关的社团列表");
        List<Teams> list = teamsService.getListByManId(manId);
        return R.successData(list);
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                         String token, Teams teams) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        if(user.getType() == 1){
            teams.setManager(user.getId());
        }
        Log.info("分页查找社团信息，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teams);
        PageData page = teamsService.getPageInfo(pageIndex, pageSize, teams);
        return R.successData(page);
    }

    @PostMapping("/add")
    @ResponseBody
    public R addInfo(Teams teams) {

        teams.setId(IDUtils.makeIDByCurrent());
        teams.setCreateTime(DateUtils.getNowDate("yyyy-MM-dd"));
        Log.info("添加社团信息，传入参数：{}", teams);
        teamsService.add(teams);
        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    public R updInfo(Teams teams) {

        Log.info("修改社团信息，传入参数：{}", teams);
        teamsService.update(teams);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {

        Log.info("删除社团信息, ID:{}", id);
        Teams teams = teamsService.getOne(id);
        teamsService.delete(teams);
        return R.success();
    }
}