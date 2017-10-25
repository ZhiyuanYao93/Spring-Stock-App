package com.zhiyuan.stockapp.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by Zhiyuan Yao
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }




//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//
//        httpSecurity
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/index/").permitAll()
//                .antMatchers("index").permitAll()
//                .antMatchers("index/").permitAll()
//                .antMatchers("/favicon.ico").permitAll()
//                .antMatchers("/index").permitAll()
//                .antMatchers("/user/new").permitAll()
//                .antMatchers("/user/login").permitAll()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/console/**").permitAll()
//                .antMatchers("/user/**").permitAll()
//                .antMatchers("home").permitAll()
//                .antMatchers("/stock/**").permitAll()
//                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
//                .authenticated()
//
//                .and()
//                .csrf().disable();
////                .formLogin()
//
////                .loginPage("/user/login")
////                .permitAll()
////                .usernameParameter("email")
////                .passwordParameter("password");
//
////                .and()
//
////                .logout()
////                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
////                .logoutSuccessUrl("/")
////
////                .and()
////
////                .exceptionHandling().accessDeniedPage("/access-denied");
//
//
//        httpSecurity.headers().frameOptions().disable();
//
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index/").permitAll()
                .antMatchers("index").permitAll()
                .antMatchers("index/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/favicon.ico").permitAll()

                .antMatchers("/register").permitAll()
//                .antMatchers("/login").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/user/**").hasAuthority("USER").anyRequest().permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/index").failureUrl("/index?error=true")

                .usernameParameter("userName").passwordParameter("password")
                .successForwardUrl("/login")

                .and().exceptionHandling()
                .accessDeniedPage("/access-denied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/webjars/**");

    }




}
