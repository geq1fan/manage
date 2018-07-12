package com.ge.modules.productline.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.productline.mapper.ProductlineInfoMapper;
import com.ge.modules.productline.model.ProductlineInfo;
import com.ge.modules.productline.service.ProductlineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;

import tk.mybatis.mapper.entity.Example;

@Transactional
@Service
public class ProductlineServiceImpl extends BaseServiceImpl<ProductlineInfo> implements ProductlineService {

	@Resource
	ProductlineInfoMapper mapper;
	
	@Transactional(readOnly = true)
	@Override
	public PageInfo<ProductlineInfo> findHistoryDataPage(Integer pageNum, Integer pageSize, String productlineName,
			String startTime, String endTime) {
		Example example = new Example(ProductlineInfo.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(productlineName)) {
			criteria.andLike("productlineName", "%" + productlineName + "%");
		}
		if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
			criteria.andBetween("createTime", DateUtil.beginOfDay(DateUtil.parse(startTime)),
					DateUtil.endOfDay(DateUtil.parse(endTime)));
		}
		// 倒序
		example.orderBy("createTime").desc();
		// 分页
		PageHelper.startPage(pageNum, pageSize);
		List<ProductlineInfo> list = this.selectByExample(example);

		return new PageInfo<ProductlineInfo>(list);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ProductlineInfo> findNewestHistoryData() {
		List<ProductlineInfo> list = mapper.findByNewestCreateTime();
		return list;
	}
}
