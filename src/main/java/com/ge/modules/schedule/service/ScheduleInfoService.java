package com.ge.modules.schedule.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.Product;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.vo.InventorySchedule;
import com.ge.modules.schedule.vo.OrderSchedule;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ScheduleInfoService extends BaseService<ScheduleInfo> {
    /**
     * 获取订单调度结果，并将调度结果持久化
     *
     * @param pageNum
     * @param pageSize
     * @param ids
     * @param productline
     * @param scheduleInfo
     * @return
     */
    PageInfo<OrderSchedule> getOrderScheduleResult(Integer pageNum, Integer pageSize, List<Object> ids, String productline, ScheduleInfo scheduleInfo);

    /**
     * 根据ids获取产品信息
     *
     * @param ids
     * @return
     */
    List<Product> getProducts(List<Object> ids);

    /**
     * 获取库存调度结果，并将调度结果持久化
     *
     * @param map
     * @param scheduleInfo
     * @return
     */
    List<InventorySchedule> getInventoryScheduleResult(Map map, ScheduleInfo scheduleInfo);

}
