package com.zhiyuan.stockapp.bootstrap;

import com.zhiyuan.stockapp.models.Role;
import com.zhiyuan.stockapp.models.Stock;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.repository.RoleRepository;
import com.zhiyuan.stockapp.repository.StockRepository;
import com.zhiyuan.stockapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Component
@Profile("default")
public class StockBootStrap implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public StockBootStrap(UserRepository userRepository, StockRepository stockRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        stockRepository.save(getStocks());
        log.debug("Preloaded stocks saved");

        userRepository.save(getUserAdmin());
        log.debug("Administrator saved");

        roleRepository.save(getRoles());
        log.debug("Preloaded roles saved");

        Optional<User> adminOptional = userRepository.findByUserName("admin");
        User admin = adminOptional.get();
        log.debug(admin.toString());
        List<Stock> stockList = stockRepository.findAll();

        for (Stock stock : stockList){
            admin.getStocks().add(stock);
            stock.getUsers().add(admin);
        }

        List<Role> roleList = roleRepository.findAll();

        for (Role role : roleList){
            admin.getRoles().add(role);
            role.getUsers().add(admin);
        }

        userRepository.save(admin);
        stockRepository.save(stockList);
        roleRepository.save(roleList);

        log.debug("Initial assignment done.");
    }

    public List<Role> getRoles(){
        List<Role> preloadedRoles = new ArrayList<>();

        Optional<Role> adminRoleOptional = roleRepository.findByRoleName("ADMIN");

        if (! adminRoleOptional.isPresent()){
            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            preloadedRoles.add(adminRole);
        }else{
            preloadedRoles.add(adminRoleOptional.get());
        }

        Optional<Role> userRoleOptional = roleRepository.findByRoleName("USER");

        if (! adminRoleOptional.isPresent()){
            Role userRole = new Role();
            userRole.setRoleName("USER");
            preloadedRoles.add(userRole);
        }else{
            preloadedRoles.add(userRoleOptional.get());
        }

        return preloadedRoles;
    }


    public List<Stock> getStocks(){
        List<Stock> preloadedStocks = new ArrayList<>();

        Optional<Stock> appleStockOptional = stockRepository.findByStockName("Apple");
        if (!appleStockOptional.isPresent()){
            Stock appleStock = new Stock();
            appleStock.setStockName("Apple");
            appleStock.setStockSymbol("AAPL");
            preloadedStocks.add(appleStock);
        }else{
            preloadedStocks.add(appleStockOptional.get());
        }

        Optional<Stock> microsoftStockOptional = stockRepository.findByStockName("Microsoft");
        if (!microsoftStockOptional.isPresent()){
            Stock microsoftStock = new Stock();
            microsoftStock.setStockName("Microsoft");
            microsoftStock.setStockSymbol("MSFT");
            preloadedStocks.add(microsoftStock);
        }else{
            preloadedStocks.add(microsoftStockOptional.get());
        }

        Optional<Stock> shellStockOptional = stockRepository.findByStockName("GOOG");
        if (!shellStockOptional.isPresent()){
            Stock shellStock = new Stock();
            shellStock.setStockName("Google");
            shellStock.setStockSymbol("GOOG");
            preloadedStocks.add(shellStock);
        }else{
            preloadedStocks.add(shellStockOptional.get());
        }

        return preloadedStocks;
    }


    public User getUserAdmin(){
        Optional<User> adminUserOptional = userRepository.findByUserName("admin");
        if (!adminUserOptional.isPresent()){
            User adminUser = new User();
            adminUser.setUserName("admin");
            adminUser.setPassword(bCryptPasswordEncoder.encode("admin"));
            return adminUser;
        }else{
            User adminUser = adminUserOptional.get();
            return adminUser;
        }
    }



}
