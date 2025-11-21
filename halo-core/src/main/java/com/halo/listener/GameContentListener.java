package com.halo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.halo.dto.GameContentDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏内容Excel解析监听器
 * 处理合并单元格和层级关系
 * 
 * @author halo_ry
 */
@Slf4j
public class GameContentListener extends AnalysisEventListener<GameContentDTO> {

    private List<GameContentDTO> dataList = new ArrayList<>();
    
    // 用于存储上一行的数据，处理合并单元格
    private String lastProject = null;
    private String lastGameTemplate = null;
    private String lastSeasonLectureOrder = null;
    private String lastGameNumber = null;
    private String lastLevelQuantity = null;
    private String lastGameInfo = null;

    @Override
    public void invoke(GameContentDTO data, AnalysisContext context) {
        // 处理合并单元格：如果当前行为空，使用上一行的值
        if (StringUtils.isNotBlank(data.getProject())) {
            lastProject = data.getProject();
        } else {
            data.setProject(lastProject);
        }

        if (StringUtils.isNotBlank(data.getGameTemplate())) {
            lastGameTemplate = data.getGameTemplate();
        } else {
            data.setGameTemplate(lastGameTemplate);
        }

        if (StringUtils.isNotBlank(data.getSeasonLectureOrder())) {
            lastSeasonLectureOrder = data.getSeasonLectureOrder();
        } else {
            data.setSeasonLectureOrder(lastSeasonLectureOrder);
        }

        if (StringUtils.isNotBlank(data.getGameNumber())) {
            lastGameNumber = data.getGameNumber();
        } else {
            data.setGameNumber(lastGameNumber);
        }

        if (StringUtils.isNotBlank(data.getLevelQuantity())) {
            lastLevelQuantity = data.getLevelQuantity();
        } else {
            data.setLevelQuantity(lastLevelQuantity);
        }

        if (StringUtils.isNotBlank(data.getGameInfo())) {
            lastGameInfo = data.getGameInfo();
        } else {
            data.setGameInfo(lastGameInfo);
        }

        // 添加到数据列表
        dataList.add(data);
    }

    public List<GameContentDTO> getDataList() {
        return dataList;
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

