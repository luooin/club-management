package com.bishe.club.controller;

import com.bishe.club.entity.TeamTypes;
import com.bishe.club.utils.PageData;
import com.bishe.club.utils.R;
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

import com.bishe.club.service.TeamTypesService;

import java.util.List;

/**
 * 系统请求响应控制器
 * 社团类型
 */
@Controller
@RequestMapping("/teamTypes")
public class TeamTypesController extends BaseController {

    protected static final Logger Log = LoggerFactory.getLogger(TeamTypesController.class);

    @Autowired
    private TeamTypesService teamTypesService;

    @RequestMapping("")
    public String index() {

        return "pages/TeamTypes";
    }

    @GetMapping("/all")
    @ResponseBody
    public R getAll(){

        Log.info("查看全部的社团类型信息");
        List<TeamTypes> list = teamTypesService.getAll();
        return R.successData(list);
    }

    @GetMapping("/info")
    @ResponseBody
    public R getInfo(String id) {

        Log.info("查找指定社团类型，ID：{}", id);
        TeamTypes teamTypes = teamTypesService.getOne(id);
        return R.successData(teamTypes);
    }

    @GetMapping("/page")
    @ResponseBody
    public R getPageInfos(Long pageIndex, Long pageSize,
                          TeamTypes teamTypes) {

        Log.info("分页查找社团类型，当前页码：{}，"
                        + "每页数据量：{}, 模糊查询，附加参数：{}", pageIndex,
                pageSize, teamTypes);
        PageData page = teamTypesService.getPageInfo(pageIndex, pageSize, teamTypes);
        return R.successData(page);
    }

    @PostMapping("/add")
    @ResponseBody
    public R addInfo(TeamTypes teamTypes) {
        teamTypes.setId(IDUtils.makeIDByCurrent());
        teamTypes.setCreateTime(DateUtils.getNowDate());

        Log.info("添加社团类型，传入参数：{}", teamTypes);
        teamTypesService.add(teamTypes);
        return R.success();
    }

    @PostMapping("/upd")
    @ResponseBody
    public R updInfo(TeamTypes teamTypes) {

        Log.info("修改社团类型，传入参数：{}", teamTypes);
        teamTypesService.update(teamTypes);
        return R.success();
    }

    @PostMapping("/del")
    @ResponseBody
    public R delInfo(String id) {

        if(teamTypesService.isRemove(id)){
            Log.info("删除社团类型, ID:{}", id);
            TeamTypes teamTypes = teamTypesService.getOne(id);
            teamTypesService.delete(teamTypes);
            return R.success();
        }else{
            return R.warn("存在关联社团，无法移除");
        }
    }
}