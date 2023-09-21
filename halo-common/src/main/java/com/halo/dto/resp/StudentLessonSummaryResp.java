package com.halo.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/9/20 14:14
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentLessonSummaryResp {

    private Long id;

    /**
     * 原表中的id
     */
    private Long originalId;

    /**
     * 当registType = 1时，存储报名id
     * 当registType = 2时，存储试听id
     */
    private String registId;

    /**
     * 学员uid
     */
    private Integer studentUid;

    /**
     * 学员id
     */
    private String studentId;

    /**
     * 学员姓名
     */
    private String studentName;

    /**
     * 新课程id
     */
    private String newCourseId;

    /**
     * 老课程id
     */
    private String courseId;

    /**
     * 班级id
     */
    private String classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 课次id
     */
    private String curriculumId;

    /**
     * 课次号
     */
    private Integer curriculumNo;

    /**
     * 课次名称
     */
    private String curriculumName;

    /**
     * 上课时间
     */
    private Date startTime;

    /**
     * 下课时间
     */
    private Date endTime;

    /**
     * 解锁时间
     */
    private Date unlockTime;

    /**
     * 是否转班
     */
    private Integer isChangeClass;

    /**
     * 是否调课
     */
    private Integer isChangeCourse;

    /**
     * 分校编码
     */
    private String cityCode;

    /**
     * 当前课次业务操作类型：1-从当前课次缴费  2-从当前课次转班  3-当前课次调课
     */
    private Integer actionType;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 主讲老师id
     */
    private String teacherId;

    /**
     * 主讲老师员工编号
     */
    private String teacherNo;
    /**
     * 辅导老师员工编号
     */
    private String tutorNo;

    /**
     * 直播id
     */
    private String chapterId;

    /**
     * 课表类型：1-必修讲  2-选修讲
     */
    private Integer type;

    /**
     * 选修讲类型
     */
    private Integer electiveType;

    /**
     * 学科id
     */
    private String subjectId;

    /**
     * 年级id
     */
    private String gradeId;

    /**
     * 授课类型
     */
    private Integer categoryType;

    /**
     * 年份
     */
    private String year;

    /**
     * 教室id
     */
    private String classroomId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 时间戳
     */
    private Date tmTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 商品类型：2-普通班 15-托管商品
     */
    private Integer goodsType;


    /**
     * 退费类型标识：REFUND、SPECIAL_REFUND、LEAVE
     */
    private String refundType;


    /**
     * 扩展字段
     * 业务类型 ：1:一键下单 2：报班回放课次 3：课次回放报名 4:赠送回放课次
     */
    private String businessType;


    /**
     * 扩展字段
     * 排课类型：1-前置排课 2-后置排课
     */
    private String arrangeType;


    /**
     * 1v1加课主班报名ID
     */
    private String sourceRegistId;

    /**
     * 报名类型：报名类型：1-报班 2-试听 3-回放
     */
    private Integer registType;


    /**
     * 业务归属：1-培优 3-小猴学堂
     */
    private Integer businessBelong;

    /**
     * 对应课程类型
     */
    private Integer courseType;

    /**
     * 对应课程用途
     */
    private Integer useLabel;



    /**
     * 选修讲关联必修讲课次号
     */
    private Integer relatedCurriculumNo;

    /**
     *   0: 普通
     *   1: 1对1学员请假
     *   2: 1对1教师请假
     *   3: 1对1少排
     *   4: 1对1少报
     */
    private Integer curriculumType;
}
