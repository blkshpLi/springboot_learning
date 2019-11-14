package com.learning.springboot.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {

    //该处使用泛型旨在将查询的List数据放入分页对象中
    private List<T> dataList;

    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;         //当前页
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalCount, Integer page, Integer size){

        //计算总页数
        if(totalCount < size){
            totalPage = 1;
        }else if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else if(totalCount % size != 0){
            totalPage = totalCount / size + 1;
        }

        if(page < 1)
            page = 1;

        if(page > totalPage)
            page = totalPage;

        this.page = page;
        pages.add(page);

        for(int i = 1; i <= 3; i++){
            if(page - i > 0){
                pages.add(0,page - i);
            }
            if(page + i <= totalPage){
                pages.add(page + i);
            }
        }

        //是否显示上一页
        showPrevious = (page != 1);

        //是否显示下一页
        showNext = (page != totalPage);

        //是否显示首页
        showFirstPage = !pages.contains(1);

        //是否显示尾页
        showEndPage = !pages.contains(totalPage);

    }

}
