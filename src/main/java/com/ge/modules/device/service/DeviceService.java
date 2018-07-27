package com.ge.modules.device.service;

import com.ge.common.service.BaseService;
import com.ge.modules.device.model.Device;
import com.github.pagehelper.PageInfo;

public interface DeviceService extends BaseService<Device> {

    /**
     * 根据情况分页查找设备信息
     *
     * @param pageNum
     * @param pageSize
     * @param condition
     * @return
     */
    PageInfo findDevicePageByConditon(Integer pageNum, Integer pageSize, String condition, Integer status);
}
