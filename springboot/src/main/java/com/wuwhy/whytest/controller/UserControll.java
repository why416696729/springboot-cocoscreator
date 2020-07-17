package com.wuwhy.whytest.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwhy.whytest.Dto.UserDto;
import com.wuwhy.whytest.service.UserService;
import com.wuwhy.whytest.utils.ResultMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserControll {
    @Resource
    ResultMap resultMap;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    public ResultMap Register(@RequestBody UserDto userDto){
        return userService.Register(userDto);
    }

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    public ResultMap Login(@RequestBody UserDto userDto){
        return userService.Login(userDto.getUserName(),userDto.getPassword());
    }
}
