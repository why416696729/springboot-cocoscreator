package com.wuwhy.whytest.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ResultMap extends HashMap<String, Object> {
    public ResultMap() {
    }

    public ResultMap message(String str) {
        this.put("msg", str);
        return this;
    }

    public ResultMap code(int code) {
        this.put("code", code);
        return this;
    }

    public ResultMap data(Object data) {
        this.put("data", data);
        return this;
    }
}