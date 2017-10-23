package com.zhiyuan.stockapp.controllers;

import com.zhiyuan.stockapp.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Controller
public class IndexController {

    @GetMapping({"","/","index"})
    public String getIndex(Model model){
        model.addAttribute("user",new User());
        return "index";
    }
}
