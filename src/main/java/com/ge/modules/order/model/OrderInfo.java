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
     * 是否已处理
     */
    private Boolean processed;
    /**
     * 审核人
     */
    private String verifier;
    /**
     * 是否过期
     */
    private Boolean expired;
}
