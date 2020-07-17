package com.wuwhy.whytest.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Paging<T> implements Serializable {

    public long count;
    public long pageSize=2;
    public long pageNum=1;
    public List<T> data;

    public Paging(long count, List<T> data,long pageNum,long pageSize) {
        this.count = count;
        this.data = data;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }
}
