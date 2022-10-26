package com.halo.convert;


import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @Description: 转换器
 * @Author: Halo_ry
 * @Date: 2022/10/26 16:22
 */
public class StringConverter implements Converter<String> {


    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 将Excel对象转成Java对象，这里读的时候会调用
     * @param cellData              NotNull
     * @param contentProperty       Nullable
     * @param globalConfiguration   NotNull
     * @return
     * @throws Exception
     */
    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return cellData.getStringValue();
    }

    /**
     * 将Java对象转成String对象，写出的时候调用
     * @param value               Java Data.NotNull.
     * @param contentProperty     Content property.Nullable.
     * @param globalConfiguration Global configuration.NotNull.
     * @return
     * @throws Exception
     */
    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value);
    }
}
