package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.repository.RoleRepository;
import com.zhiyuan.stockapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final  RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User updateUser(User user) {
        User userReference = userRepository.findOne(user.getUserId());
        userReference.setUserName(user.getUserName());
        userReference.setPassword(user.getPassword());
        userReference.setStocks(user.getStocks());
        return userRepository.save(userReference);
    }

    @Override
    public User findUserById(Integer userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()){
            log.error("User with ID: {} is not found.",userId);
            throw new NotFoundException("User with ID " + userId +" is not found.");
        }
        return userOptional.get();
    }

    @Override
    public User findUserByName(String userName) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (!userOptional.isPresent()){
            log.error("User with ID: {} is not found.",userName);
            throw new NotFoundException("User with ID " + userName +" is not found.");
        }
        return userOptional.get();
    }

    @Override
    public void deleteUserById(Integer userId) {
        userRepository.delete(userId);
    }

    @Override
    public void deleteUserByUserName(String userName) {
        userRepository.deleteByUserName(userName);
    }
}
