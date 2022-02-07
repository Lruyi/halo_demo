package com.halo.mapper;

import com.halo.entity.Subaccount;

/**
 * @author Halo_ry
 */
public interface SubaccountMapper {
    int deleteByPrimaryKey(Integer subaccountId);

    int insert(Subaccount record);

    int insertSelective(Subaccount record);

    Subaccount selectByPrimaryKey(Integer subaccountId);

    int updateByPrimaryKeySelective(Subaccount record);

    int updateByPrimaryKey(Subaccount record);
}