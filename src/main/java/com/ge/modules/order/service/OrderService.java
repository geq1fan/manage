package com.ge.modules.order.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.OrderInfo;
import com.github.pagehelper.PageInfo;

public interface OrderService extends BaseService<OrderInfo> {

	public PageInfo<OrderInfo> findOrderPage(Integer pageNum, Integer pageSize, String startTime, String endTime,
			Boolean processed);
}
