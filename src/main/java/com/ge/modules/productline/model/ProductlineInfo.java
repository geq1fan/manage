package com.ge.modules.productline.model;

import java.util.Date;

import com.ge.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductlineInfo extends BaseEntity {

	/**
     * 生产线名称
     */
	private String productlineName;
	/**
     * 客户端连接状态
     */
	private Boolean isClientConnect;
	/**
     * 客户端连接状态详情
     */
	private String ClientConnectDetail;
	/**
     * 生产线运行状态
     */
	private Boolean isProductlineRun;
	/**
     * 生产线运行状态详情
     */
	private String ProductlineRunDetail;
	/**
     * 工作部温度
     */
	private Double workTemp;
	/**
     * 熔化部温度
     */
	private Double meltTemp;
	/**
     * 窑压
     */
	private Double pressure;
	/**
     * 液面高度
     */
	private Double liquidLevel;
	/**
     * 温度允许误差
     */
	private Double tempDeviation;
	/**
     * 压力允许误差
     */
	private Double pressureDeviation;
	/**
     * 高度允许误差
     */
	private Double heightDeviation;
}
