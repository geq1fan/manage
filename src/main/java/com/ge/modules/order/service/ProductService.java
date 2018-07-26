package com.ge.modules.order.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService extends BaseService<Product> {

    /**
     * 根据产品规格查找产品列表
     *
     * @param pageNum
     * @param pageSize
     * @param productType
     * @return
     */
    PageInfo<Product> findProductPage(Integer pageNum, Integer pageSize, String productType);
}
