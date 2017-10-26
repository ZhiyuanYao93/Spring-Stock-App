package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import com.zhiyuan.stockapp.models.Role;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.repository.RoleRepository;
import com.zhiyuan.stockapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.*;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
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
        Optional<Role> roleOptional = roleRepository.findByRoleName("USER");
        Role updatedRole = new Role();
        if (roleOptional.isPresent()){
            Role role = roleOptional.get();
            user.setRoles(new HashSet<>(Arrays.asList(role)));
            role.getUsers().add(user);
             updatedRole = roleRepository.save(role);
        }

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
            log.error("User with ID: {} is not found. (from UserService.findUserById)",userId);
            throw new NotFoundException("User with ID " + userId +" is not found.");
        }
        return userOptional.get();
    }

    @Override
    public User findUserByName(String userName) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (!userOptional.isPresent()){
            log.error("User with ID: {} is not found.(from UserService.findUserByUserName)",userName);
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

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (!userOptional.isPresent()){
            log.error("User: " + userName + " is not found. (from UserService.loadUserByUsername)");
            throw new UsernameNotFoundException("User " + userName + "is not found");
        }

        User user = userOptional.get();

        List<GrantedAuthority> authorityList = getUserAuthorityList(user.getRoles());
        log.debug("Going to return userAuthBuilder");
        return userAuthenticationBuilder(user,authorityList);

    }

    private List<GrantedAuthority> getUserAuthorityList(Set<Role> userRoles){
        Set<GrantedAuthority> roles = new LinkedHashSet<>();
        for(Role role : userRoles){
            roles.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        return grantedAuthorities;
    }

    private UserDetails userAuthenticationBuilder(User user,List<GrantedAuthority> grantedAuthorities){
        log.debug("In userAuthenticationBuilder. User name is :" + user.getUserName() +" . User password is:" + user.getPassword());
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),grantedAuthorities);
    }

}
