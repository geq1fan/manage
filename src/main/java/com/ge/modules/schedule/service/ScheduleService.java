package com.ge.modules.schedule.service;

import com.ge.common.service.BaseService;
import com.ge.modules.schedule.model.ScheduleInfo;

import java.util.List;

public interface ScheduleService extends BaseService<ScheduleInfo> {
    public List<Long> getOrderScheduleResult(List<Object> ids);
}
