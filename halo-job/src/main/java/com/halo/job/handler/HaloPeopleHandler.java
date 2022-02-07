package com.halo.job.handler;

import com.halo.api.PeopleServiceApi;
import com.halo.entity.People;
import com.xxl.job.core.biz.model.ReturnT;
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
public class HaloPeopleHandler {

    @Autowired
    private PeopleServiceApi peopleServiceApi;

    @XxlJob("haloPeopleHandler")
    public ReturnT<String> handler() {
        XxlJobHelper.log("HaloPeopleHandler 的入参：", XxlJobHelper.getJobParam());
        List<People> peopleList = peopleServiceApi.list();
        XxlJobHelper.handleSuccess();
        XxlJobHelper.log("是否执行成功：" + XxlJobHelper.handleSuccess());
        return ReturnT.SUCCESS;
    }
}
