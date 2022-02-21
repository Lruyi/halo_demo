package com.halo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.halo.entity.People;

import java.util.List;

/**
 * @author Halo_ry
 */
public interface PeopleMapper extends BaseMapper<People> {

    /**
     * 查询对应的人
     * @param people
     * @return
     */
    People queryPeople(People people);

    /**
     * 保存
     * @param people
     * @return
     */
    int savePeople(People people);

    /**
     * 查询所有的人员
     * @return
     */
    List<People> queryPeopleList();
}