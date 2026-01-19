package com.halo.demo.facade;

import com.halo.common.CommonServerResult;
import com.halo.demo.client.LessonClient;
import com.halo.demo.page.LessonApiPage;
import com.halo.dto.req.CustomPageQueryReq;
import com.halo.dto.resp.StudentLessonSummaryResp;
import com.halo.exception.RemoteCallException;
import com.halo.utils.PageToQuerier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/26 15:06
 */
@Slf4j
@Component
public class LessonFacade {

    @Autowired(required = false)
    private LessonClient lessonClient;


    public LessonApiPage<StudentLessonSummaryResp> customPageQuery(CustomPageQueryReq req) {
        if (Objects.isNull(req)) {
            throw new RemoteCallException("调用课表通用接口入参为空");
        }
        return lessonClient.customPageQuery(req).getData();
    }

    public List<StudentLessonSummaryResp> customPageQueryList(CustomPageQueryReq req) {
        if (Objects.isNull(req)) {
            throw new RemoteCallException("调用课表通用接口入参为空");
        }
        // 批量分页查询
        return PageToQuerier.query(1000, 100, (pageNo, pageSize) -> {
            CommonServerResult<LessonApiPage<StudentLessonSummaryResp>> result = lessonClient.customPageQuery(req);
            LessonApiPage<StudentLessonSummaryResp> data = result.getData();
            return data.getRecords();
        });
    }
}
