package com.wtao.healthy.controller;


import com.wtao.healthy.entity.Admin;
import com.wtao.healthy.service.IAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wtao
 * @since 2019-10-09
 */
@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @GetMapping("login")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("login")
    public Map<String, Object> login(Admin admin) {
        Map<String, Object> map = new HashMap<>();
        if (admin == null || StringUtils.isBlank(admin.getUsername()) || StringUtils.isBlank(admin.getPassword())) {
            map.put("isSuccess", false);
            return map;
        }

        String username = admin.getUsername();

        Admin loginAdmin = adminService.lambdaQuery().eq(Admin::getUsername, username).one();

        if (loginAdmin == null || !StringUtils.equals(loginAdmin.getPassword(), admin.getPassword())) {
            map.put("isSuccess", false);
            return map;
        }
        map.put("isSuccess", true);
        map.put("admin", loginAdmin);
        return map;

    }

}
