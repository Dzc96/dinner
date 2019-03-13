package com.appointment.dinner.util;

import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Desc: 分页信息
 */
@Data
public class PageVO<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private long total;      // 总记录数
    private int size = 10;  //每页记录数
    private int pages;      //总页数
    private int current = 1;    //当前页
    private List<T> records;    //结果集


    public PageVO(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.total = page.getTotal();
            this.current = page.getPageNum();
            this.size = page.getPageSize();
            this.pages = page.getPages();
            this.records = page;
        }
    }

}
