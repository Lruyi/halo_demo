package com.halo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.halo.dto.InitInteractModelDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: halo_ry
 * @Date: 2025/3/22 21:21
 */
@Slf4j
public class AnalysisInteractModelListenerV2 extends AnalysisEventListener<InitInteractModelDTO> {

    private List<InitInteractModelDTO> dataList = new ArrayList<>();
    // 用于存储已经出现的数据组合，检查重复
    private Set<String> uniqueDataSet = new HashSet<>();
    private Map<String, Integer> uniqueDataMap = new HashMap<>();


    @Override
    public void invoke(InitInteractModelDTO data, AnalysisContext context) {
        // 获取当前行号
        int rowIndex = context.readRowHolder().getRowIndex() + 1;
        // 校验数据并收集错误信息
        String errorMsg = validateData(data, rowIndex);

        // 设置错误信息
        data.setErrorMsg(errorMsg);
        // 添加到数据列表
        dataList.add(data);
    }

    private String validateData(InitInteractModelDTO data, Integer rowIndex) {
        List<String> errors = new ArrayList<>();

        // 校验班级ID
        if (StringUtils.isBlank(data.getClassId())) {
            errors.add("班级ID不能为空");
        }

        // 校验互动模式
        if (data.getInteractModel() == null) {
            errors.add("交互模式不能为空");
        } else {
            try {
                int interactModel = data.getInteractModel();
                if (interactModel < 0 || interactModel > 3) {
                    errors.add("交互模式必须是0-3之间的数字");
                }
            } catch (NumberFormatException e) {
                errors.add("交互模式必须是数字");
            }
        }

        // 检查数据重复
        if (errors.isEmpty()) {  // 只有在基础验证通过后才检查重复
            String uniqueKey = generateUniqueKey(data);
//            if (!uniqueDataSet.add(uniqueKey)) {  // 如果添加失败，说明已经存在
//                errors.add(String.format("数据重复：班级ID[%s]和产品ID[%s]的组合已存在",
//                        data.getClassId(), data.getProductId()));
//            }
            Integer existingRowIndex = uniqueDataMap.get(uniqueKey);
            if (existingRowIndex != null) {  // 如果已经存在
                errors.add(String.format("与第%d行数据重复：班级ID[%s]和产品ID[%s]的组合已存在",
                        existingRowIndex, data.getClassId(), data.getProductId()));
            } else {
                // 记录当前行号
                uniqueDataMap.put(uniqueKey, rowIndex);
            }
        }

        return errors.isEmpty() ? "" : String.join("; ", errors);
    }

    // 生成用于判断重复的唯一键
    private String generateUniqueKey(InitInteractModelDTO data) {
        // 根据业务需求组合关键字段，这里使用班级ID和产品ID的组合
        return data.getClassId() + "|" + data.getProductId();
    }

    public List<InitInteractModelDTO> getDataList() {
        return dataList;
    }

    public List<InitInteractModelDTO> getSuccessDataList() {
        return dataList.stream()
                .filter(data -> StringUtils.isBlank(data.getErrorMsg()))
                .collect(Collectors.toList());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("Excel解析完成，共{}条数据", dataList.size());
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        super.onException(exception, context);
    }
}
