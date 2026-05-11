package com.gzu.decoration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzu.decoration.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户Mapper接口
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
