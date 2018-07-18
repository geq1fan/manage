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

import java.util.LinkedList;
import java.util.List;

@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderInfo> implements OrderService {

    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderInfo> findOrderPage(Integer pageNum, Integer pageSize, String startTime, String endTime,
                                             Boolean processed, Boolean expired) {
        Example example = new Example(OrderInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (processed == null) {
            processed = false;
        }
        if (expired == null) {
            expired = false;
        }
        if (expired) {
            criteria.andEqualTo("expired", expired);
        } else {
            criteria.andEqualTo("processed", processed);
        }

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

    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderInfo> findProcessedOrderPage(Integer pageNum, Integer pageSize) {
        Example example = new Example(OrderInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processed", true);
        criteria.andEqualTo("expired", false);
        example.orderBy("createTime").desc();
        PageHelper.startPage(pageNum, pageSize);
        List<OrderInfo> list = this.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderInfo> findOrderPageByIds(Integer pageNum, Integer pageSize, List ids) {
        List list = new LinkedList();
        for (Object id : ids) {
            OrderInfo orderInfo = this.findById((Long) id);
            list.add(orderInfo);
        }
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(list);
    }

}
