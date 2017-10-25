package com.zhiyuan.stockapp.controllers;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import com.zhiyuan.stockapp.models.Role;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.services.DataUpdater;
import com.zhiyuan.stockapp.services.UserService;
import com.zhiyuan.stockapp.utilities.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Controller
public class UserController {

    private static final String USER_NEWUSERFORM_URL = "user/userform";
    private final UserService userService;
    private final DataUpdater dataUpdater;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserValidator userValidator;


    public UserController(UserService userService, DataUpdater dataUpdater, BCryptPasswordEncoder bCryptPasswordEncoder, UserValidator userValidator) {
        this.userService = userService;
        this.dataUpdater = dataUpdater;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userValidator = userValidator;
    }

    @GetMapping("/register")
    public String createUser(Model model){
        model.addAttribute("user",new User());
        return USER_NEWUSERFORM_URL;
    }

    @PostMapping
    @RequestMapping("user")
    public String saveUser(@ModelAttribute("user") User user, BindingResult bindingResult){
        userValidator.validate(user,bindingResult);
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "redirect:/register";
        }
        User savedUser = userService.saveUser(user);
        return "redirect:/index";
    }


    @GetMapping("/user/login")
    public String loginUser(@ModelAttribute("user") User user,BindingResult bindingResult,Model model){
        log.debug("From UserController::loginUser, Entered user is: " + user.toString());
        User savedUser;
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            log.debug("From UserController::loginUser, BindingResult has errors. Returning to index page...");
            return "redirect:/index";
        }

        try{

            savedUser = userService.findUserByName(user.getUserName());

        }catch(NotFoundException nfe){
            return "redirect:/register";
        }
        log.info(savedUser.toString());
        if (bCryptPasswordEncoder.matches(user.getPassword(),savedUser.getPassword()) ){
            log.debug("From UserController::loginUser, Correct credentials. Going to user home");
            for(Role role: savedUser.getRoles()){
                user.getRoles().add(role);
            }
            model.addAttribute("user",savedUser);
            return "redirect:/user/" + savedUser.getUserId() + "/home";
        }
        else{
            log.debug("From UserController::loginUser,Incorrect password. Returning to index page... ");
            return "redirect:/index";
        }
    }


    @GetMapping("user/{id}/home")
    public String showUserHome(@PathVariable(value = "id") Integer userId,Model model){
        dataUpdater.updateStockInDB(userService.findUserById(userId));
        model.addAttribute("user",userService.findUserById(userId));
        return "user/userhome";
    }


    @PostMapping("/login")
    public String login(){
        log.debug("In login()");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.debug("auth.getName() is: " + auth.getName());
        User user = userService.findUserByName(auth.getName());
        return "redirect:/user/" + user.getUserId() + "/home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin/admin-home";
    }


}
