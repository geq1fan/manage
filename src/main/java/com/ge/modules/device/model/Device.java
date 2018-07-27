package com.ge.modules.device.model;

import com.ge.common.model.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class Device extends BaseEntity {

    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 所属生产线
     */
    private String productline;
    /**
     * 位置
     */
    private String location;
    /**
     * 状态
     */
    private int status;
    /**
     * 状态详细信息
     */
    private String statusDetail;
    /**
     * 是否完成检修
     */
    private Boolean isOverhaul;
    /**
     * 上次检修时间
     */
    private Date lastOverhaulTime;
    /**
     * 负责人
     */
    private String serviceman;
}
