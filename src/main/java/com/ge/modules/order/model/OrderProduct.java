package com.ge.modules.order.model;

import com.ge.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderProduct extends BaseEntity {
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 产品规格
	 */
	private String type;
	/**
	 * 产品价格
	 */
	private Double price;
}
