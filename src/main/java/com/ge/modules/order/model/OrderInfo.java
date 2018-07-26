package com.ge.modules.order.model;

import com.ge.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderInfo extends BaseEntity {

    /**
     * 订单的产品名称
     */
    private String name;
    /**
     * 订单的产品规格
     */
    private String type;
    /**
     * 订单的产品价格
     */
    private double price;
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
    /**
     * 是否已处理<br/>
     * -1：unprocessed未处理
     * 0：processed已处理
     * 1：Scheduled已调度
     * 2：expire已完成
     */
    private Integer status;
    /**
     * 审核人
     */
    private String verifier;
}
