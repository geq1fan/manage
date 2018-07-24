package com.ge.modules.schedule.model;

import com.ge.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleInfo extends BaseEntity {

	/**
     * 库存调度的产品id
     */
	private String productId;
	/**
     * 订单调度的订单id
     */
	private String orderId;
	/**
     * 调度结果
     */
	private String result;
	/**
     * 待生产的生产线id
     */
    private String productline;
	/**
     * 是否提交
     */
	private Boolean isCommit;
}
