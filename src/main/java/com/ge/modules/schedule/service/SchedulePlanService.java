package com.ge.modules.schedule.service;

import com.ge.common.service.BaseService;
import com.ge.modules.schedule.model.SchedulePlan;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SchedulePlanService extends BaseService<SchedulePlan> {
    /**
     * 根据生产线名称和任务状态分页获取生产计划
     *
     * @param pageNum
     * @param pageSize
     * @param productlineName
     * @param status
     * @return
     */
    PageInfo getSchedulePage(Integer pageNum, Integer pageSize, String productlineName, Integer status);

    /**
     * 根据调度结果更新生产计划
     *
     * @param scheduleInfoId
     * @return
     */
    Boolean updateSchedulePlan(Long scheduleInfoId);

    /**
     * 设置生产计划为进行中
     *
     * @param ids
     */
    void setSchedulePlanDoing(List<Object> ids);

    /**
     * 设置生产计划为已完成
     *
     * @param ids
     */
    void setSchedulePlanDone(List<Object> ids);
}
