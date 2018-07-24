package com.ge.modules.schedule.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.Product;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.vo.InventorySchedule;

import java.util.List;
import java.util.Map;

public interface ScheduleService extends BaseService<ScheduleInfo> {
    List<Long> getOrderScheduleResult(List<Object> ids);

    List<Product> getProducts(List<Object> ids);

    List<InventorySchedule> getInventoryScheduleResult(Map map, ScheduleInfo scheduleInfo);
}
