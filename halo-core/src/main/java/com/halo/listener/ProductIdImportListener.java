package com.halo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.halo.constant.ErrorCode;
import com.halo.dto.ProductIdDTO;
import com.halo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Description: Excel导入商品ID监听器
 * @author: halo_ry
 * @Date: 2022/10/22 18:53
 */
@Slf4j
public class ProductIdImportListener extends AnalysisEventListener<ProductIdDTO> {

    List<Object> productIdList = Lists.newArrayList();

    /**
     * 每解析一行，回调该方法
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context analysis context
     */
    @Override
    public void invoke(ProductIdDTO data, AnalysisContext context) {

        String productId = data.getProductId();
        if (StringUtils.isEmpty(productId)) {
            throw new BusinessException(ErrorCode.BUSINESS_EXCEL_VALIDATE_ERROR, "序号index=" + data.getIndex() + "，商品ID为空");
        }
        if (productIdList.contains(productId)) {
            throw new BusinessException(ErrorCode.BUSINESS_EXCEL_REPEAT_ERROR, "序号index=" + data.getIndex() + "，商品ID已重复，请核实");
        } else {
            productIdList.add(productId);
        }
        // todo 可以做业务逻辑
    }

    /**
     * 解析完全部回调
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("解析完成后,全部回调,productIdList: " + productIdList);
        productIdList.clear();
    }

    /**
     * 出现异常回调
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        // todo 当数据转换异常的时候，会抛出该异常
        super.onException(exception, context);
    }
}
