package com.ge.modules.schedule.vo;

import com.ge.modules.order.model.OrderInfo;
import lombok.Data;

@Data
public class OrderSchedule {
    /**
     * 订单信息
     */
    private OrderInfo orderInfo;
    /**
     * 生产线
     */
    private String productline;
}
