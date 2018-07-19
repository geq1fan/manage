package com.ge.modules.order.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.OrderProduct;
import com.github.pagehelper.PageInfo;

public interface OrderProductService extends BaseService<OrderProduct> {

    PageInfo<OrderProduct> findProductPage(Integer pageNum, Integer pageSize, String productType);
}
