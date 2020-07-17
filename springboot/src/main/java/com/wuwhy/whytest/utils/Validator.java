package com.wuwhy.whytest.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 校验工具
 * @author Whitlock
 */
public class Validator {

    /** 私有构造 */
    private Validator() {
    }

    /**
     * 为空校验<br>
     * 1. 为null校验(是否为null)<br>
     * 2. 字符串校验(去掉首尾空格后是否为空字符串)<br>
     * 3. Collection类型校验(是否有元素)<br>
     * 4. Map类型校验(是否有元素)<br>
     * 5. 自定义其他类型校验<br>
     * @param value 待校验值
     * @return
     */
    public static boolean isEmpty(Object value) {
        return ((null == value)
                || (value instanceof String && 0 == String.valueOf(value).trim().length())
                || (value instanceof Collection && Collection.class.cast(value).isEmpty())
                || (value instanceof Map && Map.class.cast(value).isEmpty()));
    }

    /**
     * 非空校验
     * @param value 待校验对象
     * @return
     */
    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

    /**
     * 字符串数组为空校验<br>
     * 1. 数组为null或者没有元素<br>
     * 2. 数组任意元素为空<br>
     * @param values 待校验的数组
     * @return
     */
    public static boolean isEmpty(String... values) {
        if (null == values || 0 == values.length) {
            return true;
        }

        // 遍历 -> 查找任意为空的元素
        for (String value : values) {
            if (isEmpty(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 非空校验/字符串数组所有元素不为空
     * @param values 待校验的字符串数组
     * @return
     */
    public static boolean isNotEmpty(String... values) {
        return !isEmpty(values);
    }

    /**
     * 非空校验/字符串数组任意元素不为空
     * @param values 待校验的字符串数组
     * @return
     */
    public static boolean isAnyNotEmpty(String... values) {
        if (null == values || 0 == values.length) {
            return false;
        }

        // 遍历 -> 查找任意非空元素
        for (String value : values) {
            if (isNotEmpty(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 集合为空校验<br>
     * 1. 集合为null或者没有元素<br>
     * 2. 集合任意元素为空<br>
     * @param values 待校验的集合
     * @param <T> 集合元素类型
     * @return
     */
    public static <T> boolean isAnyEmpty(Collection<T> values) {
        if (isEmpty(values)) {
            return true;
        }

        for (T value : values) {
            if (isEmpty(value)) {
                return true;
            }
        }

        return false;
    }

}
