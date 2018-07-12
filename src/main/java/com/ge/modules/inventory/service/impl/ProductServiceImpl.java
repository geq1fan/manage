package com.ge.modules.inventory.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.inventory.model.Product;
import com.ge.modules.inventory.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService {

	@Transactional(readOnly = true)
	@Override
	public Product findByProductName(String type) {
		Product product = new Product();
		product.setType(type);
		return this.findOne(product);
	}

	@Transactional(readOnly = true)
	@Override
	public PageInfo<Product> findPage(Integer pageNum, Integer pageSize, String productType) {
		Example example = new Example(Product.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(productType)) {
			criteria.andLike("type", "%" + productType + "%");
		}
		// 倒序
		example.orderBy("createTime").desc();
		// 分页
		PageHelper.startPage(pageNum, pageSize);
		List<Product> productsList = this.selectByExample(example);

		return new PageInfo<>(productsList);
	}
}
