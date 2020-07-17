package com.wuwhy.whytest.entity;
import lombok.Data;

import java.util.Map;

@Data
public class ResponseResult {
    private Integer code;
    private String message;
    private Map<String, Object> result;
}