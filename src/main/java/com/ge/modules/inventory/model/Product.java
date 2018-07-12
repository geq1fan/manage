package com.ge.modules.inventory.model;

import com.ge.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class Product extends BaseEntity {
	
	/**
     * 产品名称
     */
	private String name;
	/**
     * 产品类型
     */
	private String type;
	/**
     * 产品价格
     */
	private Double price;
	/**
     * 产品库存
     */
	private Integer inventory;
	/**
     * 产品工艺路线
     */
	private String routing;
}
