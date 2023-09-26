package com.halo.demo.facade;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/26 15:09
 */
@Getter
@Component
public class RemoteFacadeFactory {

    @Autowired(required = false)
    private LessonFacade lessonFacade;
}
