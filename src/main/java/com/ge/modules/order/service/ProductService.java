package com.ge.modules.order.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService extends BaseService<Product> {

    PageInfo<Product> findProductPage(Integer pageNum, Integer pageSize, String productType);
}
