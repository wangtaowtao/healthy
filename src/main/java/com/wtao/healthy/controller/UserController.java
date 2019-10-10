package com.wtao.healthy.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wtao.healthy.entity.UseDate;
import com.wtao.healthy.entity.User;
import com.wtao.healthy.service.IUseDateService;
import com.wtao.healthy.service.IUserService;
import com.wtao.healthy.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2019-08-18
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUseDateService useDateService;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String HEALTH_ADMIN_ID = "health_admin_id";

    @GetMapping("getUseList")
    public Object getUseList(Long id) {
        User user = userService.getById(id);
        List<UseDate> list = useDateService.lambdaQuery().eq(UseDate::getUserId, id).list();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("useDateList", list);
        return map;
    }

    @GetMapping("getUserById")
    public Object getUserById(Long id) {
        return userService.getById(id);
    }

    @GetMapping("index")
    public Object index(ModelAndView modelAndView, HttpServletRequest request) {

        Long adminId = getAdminIdByCookie(request);

        if (adminId == null) {
            modelAndView.setViewName("login");
            return modelAndView;
        }


        List<User> users = userService.lambdaQuery().eq(User::getAdminId, adminId).orderByDesc(User::getId).list();
        List<UserVo> userVos = users.stream().map(e -> transForUserVo(e)).collect(Collectors.toList());

        modelAndView.addObject("users", userVos);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("save")
    public void save(User user, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, HttpServletResponse response, HttpServletRequest request) throws IOException {

        Long adminId = getAdminIdByCookie(request);

        user.setCreateDate(new Date());
        user.setAdminId(adminId);
        userService.save(user);
        Long userId = user.getId();

        Calendar instance = Calendar.getInstance();
        instance.setTime(date);

        Date times1 = instance.getTime();
        instance.add(Calendar.DATE, 7);
        Date times2 = instance.getTime();
        instance.add(Calendar.DATE, 14);
        Date times3 = instance.getTime();
        instance.add(Calendar.DATE, 14);
        Date times4 = instance.getTime();

        for (int i = 0; i < 4; i++) {
            UseDate useDate = new UseDate();
            useDate.setUserId(userId);
            useDate.setTimes(i + 1);
            useDate.setName("第" + (i + 1) + "次使用");
            if (i == 0) {
                useDate.setUseDate(times1);
            } else if (i == 1){
                useDate.setUseDate(times2);
            }else if (i == 2){
                useDate.setUseDate(times3);
            }else if (i == 3){
                useDate.setUseDate(times4);
            }
            useDateService.save(useDate);
        }
        response.sendRedirect("index");
    }

    /**
     * 根据用户id获取最后一次的下一次应该使用的日期
     * @param interval 下一次的间隔(单位:天)
     * @param id 用户id
     * @return 下一次使用时间
     */
    @GetMapping("getNextDateByUserId")
    public Date getLastUseDateByUserId(Long id, @RequestParam(value = "interval", required = false, defaultValue = "14") Integer interval) {
        UseDate useDate = useDateService.lambdaQuery()
                .eq(UseDate::getUserId, id)
                .orderByDesc(UseDate::getId)
                .last("limit 1")
                .one();
        Calendar instance = Calendar.getInstance();
        instance.setTime(useDate.getUseDate());
        instance.add(Calendar.DATE, interval);
        Date nextDate = instance.getTime();
        return nextDate;
    }

    /**
     * 新添加一条用户使用记录
     * @param userId 用户id
     * @param date 添加的时间
     */
    @GetMapping("addUseDate")
    public Boolean addUseDate(Long userId, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){

        // 用户下现有使用次数
        Integer count = useDateService.lambdaQuery().eq(UseDate::getUserId, userId).count();

        UseDate useDate = new UseDate();
        useDate.setUseDate(date);
        useDate.setName("第" + (count + 1) + "次使用");
        useDate.setTimes(count + 1);
        useDate.setUserId(userId);

        // 添加一条使用记录
        boolean save = useDateService.save(useDate);
        return save;
    }

    @GetMapping("delete")
    public void delete(Long id, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<UseDate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UseDate::getUserId, id);
        useDateService.remove(lambdaQueryWrapper);
        userService.removeById(id);

        response.sendRedirect("index");
    }
    @GetMapping("updateUseDate")
    public Object updateUseDate(Long id, @DateTimeFormat(pattern = "yyyy-MM-dd") Date update) throws IOException {
        UseDate useDate = useDateService.getById(id);

        List<UseDate> useDateList = useDateService.lambdaQuery().eq(UseDate::getUserId, useDate.getUserId())
                .ge(UseDate::getTimes, useDate.getTimes()).list();

        List<UseDate> useDates = updateUseDate(update, useDateList);
        useDateService.updateBatchById(useDates);
        return useDate;
    }

    @PostMapping("updateUser")
    public Object updateUser(User user){
        userService.updateById(user);
        return user;
    }

    private List<UseDate> updateUseDate(Date update, List<UseDate> useDateList) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(update);
        if (useDateList.size() == 4) {
            Date times1 = instance.getTime();
            instance.add(Calendar.DATE, 7);
            Date times2 = instance.getTime();
            instance.add(Calendar.DATE, 14);
            Date times3 = instance.getTime();
            instance.add(Calendar.DATE, 14);
            Date times4 = instance.getTime();

            useDateList.get(0).setUseDate(times1);
            useDateList.get(1).setUseDate(times2);
            useDateList.get(2).setUseDate(times3);
            useDateList.get(3).setUseDate(times4);
        } else if (useDateList.size() == 3) {
            Date times1 = instance.getTime();
            instance.add(Calendar.DATE, 14);
            Date times2 = instance.getTime();
            instance.add(Calendar.DATE, 14);
            Date times3 = instance.getTime();

            useDateList.get(0).setUseDate(times1);
            useDateList.get(1).setUseDate(times2);
            useDateList.get(2).setUseDate(times3);
        } else if (useDateList.size() == 2) {
            Date times1 = instance.getTime();
            instance.add(Calendar.DATE, 14);
            Date times2 = instance.getTime();

            useDateList.get(0).setUseDate(times1);
            useDateList.get(1).setUseDate(times2);
        } else if (useDateList.size() == 1) {
            Date times1 = instance.getTime();

            useDateList.get(0).setUseDate(times1);
        }
        return useDateList;
    }


    private UserVo transForUserVo(User user) throws RuntimeException {
        if (user == null) {
            throw new RuntimeException("参数有误");
        }
        LambdaQueryWrapper<UseDate> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UseDate::getUserId, user.getId());
        lambdaQueryWrapper.ge(UseDate::getUseDate, DATE_FORMAT.format(new Date()));
        lambdaQueryWrapper.last("limit 1");
        UseDate useDate = useDateService.getOne(lambdaQueryWrapper, false);

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        if (useDate != null) {
            userVo.setNextUseDate(useDate.getUseDate());
        }
        return userVo;
    }

    private Long getAdminIdByCookie(HttpServletRequest request) {

        Long adminId = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals(HEALTH_ADMIN_ID)) {
                    adminId = Long.parseLong(cookie.getValue());
                }
            }
        }
        return adminId;
    }
}
