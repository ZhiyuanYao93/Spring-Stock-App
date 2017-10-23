package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.models.User;

/**
 * Created by Zhiyuan Yao
 */
public interface UserService {
    User saveUser(User user);
    User updateUser(User user);


    User findUserById(Integer userId);
    User findUserByName(String userName);

    void deleteUserById(Integer userId);
    void deleteUserByUserName(String userName);
}
