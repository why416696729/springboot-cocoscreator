package com.wuwhy.whytest.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wuwhy.whytest.Dao.UserMapper;
import com.wuwhy.whytest.Dto.UserDto;
import com.wuwhy.whytest.entity.User;
import com.wuwhy.whytest.service.UserService;
import com.wuwhy.whytest.utils.ResultMap;
import com.wuwhy.whytest.utils.Validator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public ResultMap resultMap = new ResultMap();

    public ResultMap Register(UserDto userDto){
        if (Validator.isEmpty(userDto.getUserName())){
            return resultMap.code(0).message("请输入用户名").data(null);
        }
        if (Validator.isEmpty(userDto.getPassword())){
            return resultMap.code(0).message("请输入密码").data(null);
        }
        if (Validator.isEmpty(userDto.getTelephone())){
            return resultMap.code(0).message("请输入电话号码").data(null);
        }
        User user =  userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, userDto.getUserName()));
        if (Validator.isEmpty(user)){
            user = new User();
            user.setUserName(userDto.getUserName());
            user.setTelephone(userDto.getTelephone());
            user.setPassword(userDto.getPassword());
            user.setCreateDate(new Date());
            user.setScore(0);
            userMapper.insert(user);
            return resultMap.code(0).message("注册成功").data(null);
        } else {
            return resultMap.code(0).message("用户名已存在").data(null);
        }
    }

    public ResultMap Login(String userName,String password){
        User user =  userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, userName));
        if (Validator.isNotEmpty(user)){
            if (user.getPassword().equals(password)){
                resultMap.message("登陆成功");
                resultMap.data(true);
            }else{
                resultMap.data(false);
                resultMap.message("密码错误");
            }
        }
        else{
            resultMap.message("用户不存在");
            resultMap.data(false);
        }

        return resultMap;
    }
}
