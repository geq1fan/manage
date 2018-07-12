package com.ge.modules.inventory.service;

import com.ge.common.service.BaseService;
import com.ge.modules.inventory.model.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService extends BaseService<Product> {

	public PageInfo<Product> findPage(Integer pageNum, Integer pageSize,String productName);

	public Product findByProductName(String name) throws Exception;
}
