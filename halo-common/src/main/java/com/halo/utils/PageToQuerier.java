package com.halo.utils;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 分页查询list
 */
public class PageToQuerier {

    public static <R> List<R> query(int limitQueryCountPage,int pageSize, QueryAction<R> action) {
        List<R> result = Lists.newArrayList();
        int pageNo = 1;
        List<R> tempRes;
        do{
            tempRes = action.doQuery(pageNo,pageSize);
            if(tempRes != null){
                result.addAll(tempRes);
            }else{
                break;
            }
            pageNo++;
        }while (tempRes.size() == pageSize && pageNo < limitQueryCountPage);
        return result;
    }

    @FunctionalInterface
    public interface QueryAction<R> {
        List<R> doQuery(int pageNo,int pageSize);
    }
}