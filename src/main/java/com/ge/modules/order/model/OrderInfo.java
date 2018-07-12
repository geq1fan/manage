package com.ge.modules.order.model;

import com.ge.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class OrderInfo extends BaseEntity {
	
	/**
     * 订单的产品id
     */
	private Integer productId;
	/**
     * 订购总数
     */
	private Integer amount;
	/**
     * 交货期限
     */
	private Integer deadline;
	/**
     * 罚金制度
     */
	private String penalty;
	/**
     * 备注
     */
	private String notes;
}
