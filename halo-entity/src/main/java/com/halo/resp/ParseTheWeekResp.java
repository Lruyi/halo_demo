package com.halo.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2022/5/29 16:34
 */
@Data
public class ParseTheWeekResp implements Serializable {
    private static final long serialVersionUID = 5413795488337779391L;

    /**
     * key:课次号、value:日期（yyyy-MM-dd）
     */
    private Map<Integer, Date> curriculumNoDate;
}
