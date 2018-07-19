package com.ge.modules.inventory.service;

import com.ge.common.service.BaseService;
import com.ge.modules.inventory.model.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService extends BaseService<Product> {

    PageInfo<Product> findProductPage(Integer pageNum, Integer pageSize, String productType);

    PageInfo<Product> findInventoryPage(Integer pageNum, Integer pageSize, String productType, String startTime,
                                        String endTime);

    Product findByProductName(String name) throws Exception;
}
