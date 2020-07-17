package com.wuwhy.whytest.service;

import com.wuwhy.whytest.Dto.UserDto;
import com.wuwhy.whytest.entity.User;
import com.wuwhy.whytest.utils.ResultMap;

import java.util.Date;
import java.util.Map;

public interface UserService {
    ResultMap Register(UserDto userDto);
    ResultMap Login(String userName,String password);
}
