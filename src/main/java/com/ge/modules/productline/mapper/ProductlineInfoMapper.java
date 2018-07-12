package com.ge.modules.productline.mapper;

import java.util.List;

import com.ge.common.mapper.BaseMapper;
import com.ge.modules.productline.model.ProductlineInfo;

public interface ProductlineInfoMapper extends BaseMapper<ProductlineInfo>{

	List<ProductlineInfo> findByNewestCreateTime();
}
