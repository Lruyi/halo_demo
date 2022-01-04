package com.halo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.halo.entity.People;

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
}