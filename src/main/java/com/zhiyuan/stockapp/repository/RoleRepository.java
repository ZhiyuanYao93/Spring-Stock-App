package com.zhiyuan.stockapp.repository;

import com.zhiyuan.stockapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Zhiyuan Yao
 */
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleId(Integer roleId);
    Optional<Role> findByRoleName(String roleName);

    void deleteByRoleId(Integer roleId);
    void deleteByRoleName(String roleName);

}
