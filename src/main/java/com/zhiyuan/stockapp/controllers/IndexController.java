package com.zhiyuan.stockapp.controllers;

import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Controller
public class IndexController {

    private UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"","/","index"})
    public String getIndex(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser")){
            log.debug("No user is authenticated now.");
        }else{
            log.debug(authentication.getName() + " is logged in now.");
            return "redirect:/user/" + userService.findUserByName(authentication.getName()).getUserId() + "/home";
        }
        model.addAttribute("user",new User());
        return "index";
    }
}
