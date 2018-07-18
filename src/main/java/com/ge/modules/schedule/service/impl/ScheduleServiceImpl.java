package com.ge.modules.schedule.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.order.mapper.OrderMapper;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.service.ScheduleService;
import com.ge.util.ga.GeneticAlgorithmUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class ScheduleServiceImpl extends BaseServiceImpl<ScheduleInfo> implements ScheduleService {

    @Resource
    OrderMapper orderMapper;

    @Override
    public List<Long> getOrderScheduleResult(List<Object> ids) {
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setOrderId(ids.toString());

        Example example = new Example(OrderInfo.class);
        example.createCriteria().andIn("id", ids);
        List<OrderInfo> orderInfos = orderMapper.selectByExample(example);

        return GeneticAlgorithmUtils.calculate(orderInfos);
    }
}
