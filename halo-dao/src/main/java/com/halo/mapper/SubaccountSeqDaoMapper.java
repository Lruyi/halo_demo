package com.halo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.halo.entity.SubaccountSeq;
import com.halo.vo.AdjustmentBillListVo;
import com.halo.vo.FlowingBillListVo;

import java.util.List;
import java.util.Map;

/**
* 账户资金变动流水表
* @author HuangLei
*/
public interface SubaccountSeqDaoMapper extends BaseMapper<SubaccountSeq> {

    /**
    * [新增]
    * @param subaccountSeq
    * @return
    **/
    int add(SubaccountSeq subaccountSeq);


    /**
    * [更新]
    * @param subaccountSeq
    * @return 
    **/
    int update(SubaccountSeq subaccountSeq);

    /**
    * [查詢]
    * @param params
    * @return  
    **/
    List<SubaccountSeq> queryByMap(Map<String, Object> params);

    /**
     * 查询行数
     * @param params
     * @return
     **/
     int queryTotal(Map<String, Object> params);

    /**
     * 查询调额记录条数
     * @param param
     * @return
     */
    Integer queryTotalByAdjustment(Map<String, Object> param);

    /**
     * 分页查询调额记录
     * @param param
     * @return
     */
    List<AdjustmentBillListVo> queryAdjustmentBillByMap(Map<String, Object> param);

    /**
     * 查询流水条数
     * @param param
     * @return
     */
    List<Integer> queryTotal4Flowing(Map<String, Object> param);

    /**
     * 查询流水记录
     * @param param
     * @return
     */
    List<FlowingBillListVo> queryFlowingRecordByMap(Map<String, Object> param);
}
