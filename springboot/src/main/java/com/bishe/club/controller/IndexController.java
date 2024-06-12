package com.bishe.club.controller;

import com.bishe.club.entity.Notices;
import com.bishe.club.utils.CacheHandle;
import com.bishe.club.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bishe.club.entity.Users;
import com.bishe.club.service.NoticesService;
import com.bishe.club.service.UsersService;
import com.bishe.club.utils.IDUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    private static final Logger Log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UsersService usersService;

    @Autowired
    private CacheHandle cacheHandle;

    @Autowired
    private NoticesService noticesService;

    @GetMapping("/sys/notices")
    @ResponseBody
    public R getNoticeList(String token){

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        if(user.getType() == 0){
            List<Notices> list = noticesService.getSysNotices();
            return R.successData(list);
        }else if(user.getType() == 1){
            List<Notices> list = noticesService.getManNotices(user.getId());
            return R.successData(list);
        }else{
            List<Notices> list = noticesService.getMemNotices(user.getId());
            return R.successData(list);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public R login(String userName, String passWord, HttpSession session){

        Log.info("用户登录，用户名：{}， 用户密码：{}", userName, passWord);
        Users user = usersService.getUserByUserName(userName);
        if(user == null) {
            return R.error("输入的用户名不存在");
        }else {
            if(passWord.equals(user.getPassWord().trim())) {
                String token = IDUtils.makeIDByUUID();
                cacheHandle.addUserCache(token, user.getId());
                return R.success("登录成功", token);
            }else {
                return R.error("输入的密码错误");
            }
        }
    }

    @RequestMapping("/exit")
    @ResponseBody
    public R exit(String token) {

        Log.info("用户退出系统并移除登录信息");
        cacheHandle.removeUserCache(token);
        return R.success();
    }

    @GetMapping("/info")
    @ResponseBody
    public R info(String token){

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        return R.successData(user);
    }

    @PostMapping("/info")
    @ResponseBody
    public R info(Users user){

        Log.info("修改用户信息，{}", user);
        usersService.update(user);
        return R.success();
    }

    @RequestMapping("/checkPwd")
    @ResponseBody
    public R checkPwd(String oldPwd, String token) {

        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        if(oldPwd.equals(user.getPassWord())) {
            return R.success();
        }else {
            return R.warn("原始密码和输入密码不一致");
        }
    }

    @PostMapping("/pwd")
    @ResponseBody
    public R pwd(String token, String password) {

        Log.info("修改用户密码，{}", password);
        Users user = usersService.getOne(cacheHandle.getUserInfoCache(token));
        user.setPassWord(password);
        usersService.update(user);
        return R.success();
    }
}