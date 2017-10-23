package com.zhiyuan.stockapp.utilities;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

/**
 * Created by Zhiyuan Yao
 */
@Component
@Slf4j
public class UserValidator implements Validator {
    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty");

        if (user.getUserName().length() < 3 || user.getUserName().length() > 32) {
            errors.rejectValue("userName", "Size.userForm.username");
        }

        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        if (!(VALID_EMAIL_ADDRESS_REGEX.matcher(user.getUserName()).matches())) {
            errors.rejectValue("userName", "ValidEmail.userForm.username");
        }


        try{
            userService.findUserByName(user.getUserName());
            errors.rejectValue("userName", "Duplicate.userForm.username");
        }catch(NotFoundException nfe){
            log.debug("Duplicate username NOT FOUND. Valid username!");
        }


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() < 5 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }
    }
}
