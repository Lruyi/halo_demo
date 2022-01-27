package com.halo.job.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.halo.entity.People;

import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/1/27 18:33
 */
public class HaloPeopleJob extends HaloPeopleHandler {
    @Override
    protected String captrueDatas(Object offset, Integer pageSize, Long id, Integer threadPoolCoreSize) throws JsonProcessingException {
        return null;
    }

    @Override
    protected String getMaxOffset(List<People> datas) throws JsonProcessingException {
        return null;
    }
}
