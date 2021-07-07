package com.example.halo.demo.mapper;

import com.example.halo.demo.entity.Subaccount;

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