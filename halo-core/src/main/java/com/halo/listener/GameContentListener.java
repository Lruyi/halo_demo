package com.halo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.halo.dto.GameContentDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 游戏内容Excel解析监听器
 * 处理合并单元格和层级关系
 * 
 * @author halo_ry
 */
@Slf4j
public class GameContentListener extends AnalysisEventListener<GameContentDTO> {

    @Getter
    private final List<GameContentDTO> dataList = new ArrayList<>();
    
    // 用于存储上一行的数据，处理合并单元格
    private String lastProject = null;
    private String lastGameTemplate = null;
    private String lastSeasonLectureOrder = null;
    private Integer lastGameNumber = null;
    private Integer lastLevelQuantity = null;
    private String lastGameInfo = null;

    @Override
    public void invoke(GameContentDTO data, AnalysisContext context) {
        // 处理合并单元格：如果当前行为空，使用上一行的值
        lastProject = fillIfBlank(data.getProject(), lastProject, data::setProject);
        lastGameTemplate = fillIfBlank(data.getGameTemplate(), lastGameTemplate, data::setGameTemplate);
        lastSeasonLectureOrder = fillIfBlank(data.getSeasonLectureOrder(), lastSeasonLectureOrder, data::setSeasonLectureOrder);
        lastGameNumber = fillIfNull(data.getGameNumber(), lastGameNumber, data::setGameNumber);
        lastLevelQuantity = fillIfNull(data.getLevelQuantity(), lastLevelQuantity, data::setLevelQuantity);
        lastGameInfo = fillIfBlank(data.getGameInfo(), lastGameInfo, data::setGameInfo);

        // 添加到数据列表
        dataList.add(data);
    }

    /**
     * 如果当前值为空，使用上一个值填充
     */
    private String fillIfBlank(String current, String last, Consumer<String> setter) {
        if (StringUtils.isNotBlank(current)) {
            return current;
        }
        setter.accept(last);
        return last;
    }

    /**
     * 如果当前值为空，使用上一个值填充（Integer类型）
     */
    private Integer fillIfNull(Integer current, Integer last, Consumer<Integer> setter) {
        if (Objects.nonNull(current)) {
            return current;
        }
        setter.accept(last);
        return last;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("Excel解析完成，共{}条数据", dataList.size());
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("解析Excel时发生异常，行号: {}", context.readRowHolder().getRowIndex() + 1, exception);
        super.onException(exception, context);
    }
}

