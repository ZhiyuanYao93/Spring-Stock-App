package com.zhiyuan.stockapp.utilities;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@WebListener
public class HttpSrssionChecker implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("Session ID %s created at %s%n", httpSessionEvent.getSession().getId(), new Date());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        log.info("Session ID %s destroyed at %s%n", httpSessionEvent.getSession().getId(), new Date());
    }
}
