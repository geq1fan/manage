package com.ge.modules.order.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.order.model.OrderProduct;
import com.ge.modules.order.service.OrderProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class OrderProductServiceImpl extends BaseServiceImpl<OrderProduct> implements OrderProductService {

	@Transactional(readOnly = true)
	@Override
	public PageInfo<OrderProduct> findProductPage(Integer pageNum, Integer pageSize, String productType) {
		Example example = new Example(OrderProduct.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(productType)) {
			criteria.andLike("type", "%" + productType + "%");
		}
		// 倒序
		example.orderBy("createTime").desc();
		// 分页
		PageHelper.startPage(pageNum, pageSize);
		List<OrderProduct> productsList = this.selectByExample(example);

		return new PageInfo<>(productsList);
	}
}
