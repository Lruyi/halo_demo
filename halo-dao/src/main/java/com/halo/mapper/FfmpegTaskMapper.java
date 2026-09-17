package com.halo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.halo.entity.FfmpegTask;
import com.halo.enums.TaskStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:
 * @date: 2026/04/17 18:43
 * @description:
 */

@Mapper
public interface FfmpegTaskMapper extends BaseMapper<FfmpegTask> {

    /**
     * 查询需要恢复的任务（PENDING 和 RUNNING 状态）
     */
    default List<FfmpegTask> selectRecoverableTasks() {
        return this.selectList(new LambdaQueryWrapper<FfmpegTask>()
                .in(FfmpegTask::getStatus, TaskStatusEnum.PENDING.getCode(), TaskStatusEnum.RUNNING.getCode())
                .orderByDesc(FfmpegTask::getPriority)
                .orderByAsc(FfmpegTask::getId));
    }

    /**
     * 更新回调状态
     */
    @Update("UPDATE tb_ffmpeg_task SET callback_status = #{callbackStatus}, last_callback_time = NOW() WHERE id = #{taskId}")
    int updateCallbackStatus(@Param("taskId") Long taskId, @Param("callbackStatus") Integer callbackStatus);

    /**
     * 按日期范围统计各状态任务数
     */
    @Select("SELECT COUNT(*) FROM tb_ffmpeg_task WHERE status = #{status} AND create_time BETWEEN #{from} AND #{to}")
    long countByStatusAndDateRange(@Param("status") Integer status,
                                   @Param("from") LocalDateTime from,
                                   @Param("to") LocalDateTime to);

    /**
     * 按日期范围统计回调失败数
     */
    @Select("SELECT COUNT(*) FROM tb_ffmpeg_task WHERE callback_status = #{callbackStatus} AND create_time BETWEEN #{from} AND #{to}")
    long countCallbackStatusByDateRange(@Param("callbackStatus") Integer callbackStatus,
                                        @Param("from") LocalDateTime from,
                                        @Param("to") LocalDateTime to);

    /**
     * 按日期范围统计平均执行时长（ms）
     */
    @Select("SELECT COALESCE(AVG(TIMESTAMPDIFF(SECOND, start_time, end_time)) * 1000, 0) FROM tb_ffmpeg_task " +
            "WHERE status = 2 AND start_time IS NOT NULL AND end_time IS NOT NULL AND create_time BETWEEN #{from} AND #{to}")
    long avgDurationMsByDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
