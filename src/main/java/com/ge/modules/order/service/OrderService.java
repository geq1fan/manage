package com.ge.modules.order.service;

import com.ge.common.service.BaseService;
import com.ge.modules.order.model.OrderInfo;
import com.github.pagehelper.PageInfo;

public interface OrderService extends BaseService<OrderInfo> {

    /**
     * 根据订单状态分页查看订单列表
     *
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    PageInfo<OrderInfo> findOrderPage(Integer pageNum, Integer pageSize, String startTime, String endTime,
                                      Integer status);

    /**
     * 查找已审核订单列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<OrderInfo> findProcessedOrderPage(Integer pageNum, Integer pageSize);

    /**
     * 将订单设置为未审核状态
     *
     * @param id
     * @return
     */
    Integer updateUnprocessedOrder(Long id);

    /**
     * 将订单设置为已审核状态
     *
     * @param id
     * @return
     */
    Integer updateProcessedOrder(Long id);

    /**
     * 更新已调度的订单
     *
     * @param id
     * @return
     */
    Integer updateScheduledOrder(Long id);

    /**
     * 更新已完成的订单
     *
     * @param id
     * @return
     */
    Integer updateCompletedOrder(Long id);

}
