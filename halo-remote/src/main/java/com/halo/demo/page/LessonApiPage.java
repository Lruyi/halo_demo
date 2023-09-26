package com.halo.demo.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/26 15:51
 */
@Data
public class LessonApiPage<T> implements Serializable {
    private static final long serialVersionUID = 8040805185598479908L;

    private long total;
    private int size;
    private int current;
    private boolean searchCount;
    private int page;
    private List<T> records;
}
