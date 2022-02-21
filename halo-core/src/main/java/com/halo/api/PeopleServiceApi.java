package com.halo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halo.common.Result;
import com.halo.entity.People;

import java.util.List;

/**
 * @Description:
 * @Author: Halo_ry
 * @Date: 2020/4/6 16:14
 */
public interface PeopleServiceApi extends IService<People> {

    /**
     * 查询对应的人
     * @param people
     * @return
     */
    Result<People> queryPeople(People people);

    /**
     * 新增
     * @param people
     * @return
     */
    Result<People> savePeople(People people);

    List<People> queryPeopleList();

}
