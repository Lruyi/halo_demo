package com.halo.job.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2021/8/12 15:54
 */
@Component
public abstract class AbstractCaptrueJob<T> {

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * 以offset为最小时间维度，增量摄取数据
     *
     * @param offset
     * @param pageSize 每次提取最大数量
     * @return
     */
    protected abstract String captrueDatas(Object offset, Integer pageSize, Long id, Integer threadPoolCoreSize) throws JsonProcessingException;

    /**
     * 获取本次最大时间维度offset，作为下次增量获取的输入参数
     *
     * @param datas
     * @return
     */
    protected abstract String getMaxOffset(List<T> datas) throws JsonProcessingException;
}
