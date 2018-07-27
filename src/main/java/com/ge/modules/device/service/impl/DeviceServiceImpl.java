package com.ge.modules.device.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.device.model.Device;
import com.ge.modules.device.service.DeviceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

@Service
public class DeviceServiceImpl extends BaseServiceImpl<Device> implements DeviceService {

    /**
     * 根据情况分页查找设备信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo findDevicePageByConditon(Integer pageNum, Integer pageSize, String condition, Integer status) {
        Example example = new Example(Device.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(condition)) {
            if (condition.trim().split(":").length == 2) {
                String property = condition.trim().split(":")[0];
                String value = condition.trim().split(":")[1];
                if (property.contains("名称")) {
                    criteria.andLike("deviceName", "%" + value + "%");
                } else if (property.contains("类型")) {
                    criteria.andLike("deviceType", "%" + value + "%");
                } else if (property.contains("生产线")) {
                    criteria.andLike("productline", "%" + value + "%");
                } else if (property.contains("位置")) {
                    criteria.andLike("location", "%" + value + "%");
                }
            }
        }
        if (Objects.nonNull(status)) {
            criteria.andEqualTo("status", status);
        }
        // 倒序
        example.orderBy("modifyTime").desc();
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        List<Device> deviceList = this.selectByExample(example);

        return new PageInfo<>(deviceList);
    }
}
