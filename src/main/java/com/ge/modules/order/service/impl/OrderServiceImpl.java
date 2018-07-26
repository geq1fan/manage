package com.ge.modules.order.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderInfo> implements OrderService {

    /**
     * 根据status分页查询订单列表
     *
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param status
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderInfo> findOrderPage(Integer pageNum, Integer pageSize, String startTime, String endTime,
                                             Integer status) {
        Example example = new Example(OrderInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", status);

        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            criteria.andBetween("createTime", DateUtil.beginOfDay(DateUtil.parse(startTime)),
                    DateUtil.endOfDay(DateUtil.parse(endTime)));
        }
        // 倒序
        example.orderBy("createTime");
        PageHelper.startPage(pageNum, pageSize);
        List<OrderInfo> list = this.selectByExample(example);
        return new PageInfo<>(list);
    }

    /**
     * 分页查询已审核但未过期订单列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderInfo> findProcessedOrderPage(Integer pageNum, Integer pageSize) {
        Example example = new Example(OrderInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", 0);
        example.orderBy("createTime").desc();
        PageHelper.startPage(pageNum, pageSize);
        List<OrderInfo> list = this.selectByExample(example);
        return new PageInfo<>(list);
    }

    /**
     * 将订单设置为未审核状态
     *
     * @param id
     * @return
     */
    @Override
    public Integer updateUnprocessedOrder(Long id) {
        OrderInfo orderInfo = this.findById(id);
        orderInfo.setStatus(-1);
        return this.updateSelective(orderInfo);
    }

    /**
     * 将订单设置为已审核状态
     *
     * @param id
     * @return
     */
    @Override
    public Integer updateProcessedOrder(Long id) {
        OrderInfo orderInfo = this.findById(id);
        orderInfo.setStatus(0);
        return this.updateSelective(orderInfo);
    }

    /**
     * 将订单设置为已调度状态
     *
     * @param id
     * @return
     */
    @Override
    public Integer updateScheduledOrder(Long id) {
        OrderInfo orderInfo = this.findById(id);
        orderInfo.setStatus(1);
        return this.updateSelective(orderInfo);
    }

    /**
     * 将订单设置为已完成状态
     *
     * @param id
     * @return
     */
    @Override
    public Integer updateCompletedOrder(Long id) {
        OrderInfo orderInfo = this.findById(id);
        orderInfo.setStatus(2);
        return this.updateSelective(orderInfo);
    }

}
