package com.zhiyuan.stockapp.repository;

import com.zhiyuan.stockapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Zhiyuan Yao
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByUserId(Integer userId);
    void deleteByUserName(String userName);
}
