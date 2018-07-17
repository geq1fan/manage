package com.ge.modules.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderInfo> implements OrderService {

	@Transactional(readOnly = true)
	@Override
	public PageInfo<OrderInfo> findOrderPage(Integer pageNum, Integer pageSize, String startTime, String endTime,
			Boolean processed) {
		Example example = new Example(OrderInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("processed", processed);
		if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
			criteria.andBetween("createTime", DateUtil.beginOfDay(DateUtil.parse(startTime)),
					DateUtil.endOfDay(DateUtil.parse(endTime)));
		}
		// 倒序
		example.orderBy("createTime").desc();
		PageHelper.startPage(pageNum, pageSize);
		List<OrderInfo> list = this.selectByExample(example);
		return new PageInfo<OrderInfo>(list);
	}

}
