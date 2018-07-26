package com.ge.modules.schedule.model;

import com.ge.common.model.BaseEntity;
import lombok.Data;

@Data
public class SchedulePlan extends BaseEntity {
    /**
     * 生产的订单id或产品id
     */
    private Long targetId;
    /**
     * 生产线名称
     */
    private String productline;

    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品规格
     */
    private String type;
    /**
     * 产品数量
     */
    private Integer amount;
    /**
     * 生产状态<br/>
     * -1：未进行
     * 0：进行中
     * 1：已完成
     */
    private Integer status;
}
