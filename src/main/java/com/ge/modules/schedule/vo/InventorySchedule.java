package com.ge.modules.schedule.vo;

import lombok.Data;

@Data
public class InventorySchedule {
    /**
     * 生产线名称
     */
    private String productline;
    /**
     * 调度结果
     */
    private String result;
}
