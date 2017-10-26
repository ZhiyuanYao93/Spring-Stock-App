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

/**
 * Created by Zhiyuan Yao
 */
@Component
@Slf4j
@Profile("dev")
public class MySQLBootStrap implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MySQLBootStrap(UserRepository userRepository, StockRepository stockRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (stockRepository.count() == 0) {
            stockRepository.save(getStocks());
            log.debug("Preloaded Stocks saved");
        }else{
            log.debug("Preset Stocks already exist.");
        }

        if (roleRepository.count() == 0) {
            roleRepository.save(getRoles());
            log.debug("Preloaded Roles saved");
        }else{
            log.debug("Preset Roles already exist.");
        }

        if (userRepository.count() == 0){
            userRepository.save(getUserAdmin());
            log.debug("Administrator saved");
        }else{
            log.debug("Administrator already exists.");
        }


        Optional<User> adminOptional = userRepository.findByUserName("admin");
        User admin = adminOptional.get();
        log.debug(admin.toString());

        if (admin.getStocks().size() == 0 || admin.getRoles().size() == 0) {
            List<Stock> stockList = stockRepository.findAll();
            List<Role> roleList = roleRepository.findAll();

            for (Stock stock : stockList) {
                admin.getStocks().add(stock);
                stock.getUsers().add(admin);
            }

            for (Role role : roleList){
                admin.getRoles().add(role);
                role.getUsers().add(admin);
            }

            userRepository.save(admin);
            stockRepository.save(stockList);
            roleRepository.save(roleList);

            log.debug("Initial assignment done.");
        }else{
            log.debug("Initial assignment already done before.");
        }
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

    public List<Role> getRoles(){
        List<Role> preloadedRoles = new ArrayList<>();
        Optional<Role> adminRoleOptional = roleRepository.findByRoleName("ADMIN");
        if (!adminRoleOptional.isPresent()){
            Role adminRole = new Role();
            adminRole.setRoleName("ADMIN");
            preloadedRoles.add(adminRole);
        }else{
            preloadedRoles.add(adminRoleOptional.get());
        }

        Optional<Role> userRoleOptional = roleRepository.findByRoleName("USER");
        if (!userRoleOptional.isPresent()){
            Role userRole = new Role();
            userRole.setRoleName("USER");
            preloadedRoles.add(userRole);
        }else{
            preloadedRoles.add(userRoleOptional.get());
        }

        return preloadedRoles;
    }

}
