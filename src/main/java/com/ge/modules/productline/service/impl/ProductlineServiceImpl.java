package com.ge.modules.productline.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.productline.mapper.ProductlineInfoMapper;
import com.ge.modules.productline.model.ProductlineInfo;
import com.ge.modules.productline.service.ProductlineService;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.TimeAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.series.Line;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
        return mapper.findByNewestCreateTime();
    }

    @Override
    public Option selectRemoveCauses(String productlineName) {
        //查询前10
        PageHelper.startPage(1, 10);
        //数据库查询获取统计数据
        Example example = new Example(ProductlineInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productlineName)) {
            criteria.andLike("productlineName", "%" + productlineName + "%");
        }
        List<ProductlineInfo> list = mapper.selectByExample(example);
        //创建Option
        Option option = new Option();
        option.title("生产线详情").tooltip(Trigger.axis).legend("");
        //折线图
        Line line = new Line();
        //设置折线图参数
        line.smooth(true);
        //循环数据
        for (ProductlineInfo productlineInfo : list) {
            //填充数据
            line.data(productlineInfo.getCreateTime(), productlineInfo.getPressure());
        }
        //设置X轴
        option.xAxis(new TimeAxis());
        //设置Y轴
        option.yAxis(new ValueAxis().boundaryGap(0d, 0.01));
        //设置数据
        option.series(line);
        //返回Option
        return option;
    }
}
