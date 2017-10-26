package com.zhiyuan.stockapp.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Created by Zhiyuan Yao
 */
@Component
@Slf4j
public class SessionUtils {
    private SessionRegistry sessionRegistry;

    @Autowired
    public SessionUtils(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }


    public void expireUserSessions(String userName){
        for (Object pricipal : sessionRegistry.getAllPrincipals()){
            if (pricipal instanceof User){
                UserDetails userDetails = (UserDetails) pricipal;
                log.debug("In SessionUtils::expireUserSession: " + userDetails.getUsername());
                if (userDetails.getUsername().equals(userName)){
                    for(SessionInformation sessionInformation : sessionRegistry.getAllSessions(userDetails,true)){
                        sessionInformation.expireNow();
                    }
                }
            }
        }
    }

}
