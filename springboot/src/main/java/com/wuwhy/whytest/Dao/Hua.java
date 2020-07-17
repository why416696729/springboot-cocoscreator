package com.wuwhy.whytest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("huajia")
public class Hua {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    private String name;
    private String color;
    private String color2;
    private String color3;
    private String xian;
    private String xian2;
    private String xian3;
}
