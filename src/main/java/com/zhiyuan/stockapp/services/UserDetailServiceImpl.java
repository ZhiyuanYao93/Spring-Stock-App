package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.models.Role;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Zhiyuan Yao
 */

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService{
    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (!userOptional.isPresent()){
            throw new UsernameNotFoundException(userName + "is not found.");
        }

        User user = userOptional.get();

        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();

        for (Role role : user.getRoles()){
            grantedAuthoritySet.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),grantedAuthoritySet);
    }
}
