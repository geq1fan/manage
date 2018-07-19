package com.ge.modules.order.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.OrderInfo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderService extends BaseService<OrderInfo> {

    PageInfo<OrderInfo> findOrderPage(Integer pageNum, Integer pageSize, String startTime, String endTime,
                                      Boolean processed, Boolean expired);

    PageInfo<OrderInfo> findProcessedOrderPage(Integer pageNum, Integer pageSize);

    PageInfo<OrderInfo> findOrderPageByIds(Integer pageNum, Integer pageSize, List ids);
}
