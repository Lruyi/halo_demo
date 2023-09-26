package com.halo.controller;

import com.halo.common.Result;
import com.halo.demo.client.LessonClient;
import com.halo.demo.facade.RemoteFacadeFactory;
import com.halo.demo.page.LessonApiPage;
import com.halo.dto.req.CustomPageQueryReq;
import com.halo.dto.req.StudentSummaryReq;
import com.halo.dto.resp.StudentLessonSummaryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/21 17:40
 */
@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private RemoteFacadeFactory remoteFacadeFactory;

    @Autowired(required = false)
    private LessonClient lessonClient;

    @PostMapping("/querySummaryList")
    public Result<List<StudentLessonSummaryResp>> querySummaryList(@Validated @RequestBody StudentSummaryReq req) {
        return Result.getSuccess(lessonClient.querySummaryByStudentUidAndDatePeriod(req).getData());
    }

    @PostMapping("queryCustomPageQuery")
    public Result<LessonApiPage<StudentLessonSummaryResp>> customPageQuery(@RequestBody CustomPageQueryReq req) {
        return Result.getSuccess(remoteFacadeFactory.getLessonFacade().customPageQuery(req));
    }
}
