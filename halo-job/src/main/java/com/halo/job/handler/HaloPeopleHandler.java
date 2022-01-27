package com.halo.job.handler;

import com.halo.api.PeopleServiceApi;
import com.halo.entity.People;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/1/27 18:27
 */
@Slf4j
@Component
public abstract class HaloPeopleHandler extends AbstractCaptrueJob<People> {

    @Autowired
    private PeopleServiceApi peopleServiceApi;

    @XxlJob("haloPeopleHandler")
    public String handler() {
        XxlJobHelper.log("HaloPeopleHandler 的入参：", XxlJobHelper.getJobParam());
        List<People> peopleList = peopleServiceApi.list();
        XxlJobHelper.handleSuccess();
        return "SUCCESS";
    }
}
