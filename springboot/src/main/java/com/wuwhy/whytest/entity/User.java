package com.wuwhy.whytest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "user")
public class User implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String userName;
    private String password;
    private String telephone;
    private Date createDate;
    private int score;
}
