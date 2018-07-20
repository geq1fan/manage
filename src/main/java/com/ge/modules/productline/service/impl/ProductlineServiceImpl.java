package com.ge.modules.productline.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.productline.mapper.ProductlineInfoMapper;
import com.ge.modules.productline.model.ProductlineInfo;
import com.ge.modules.productline.service.ProductlineService;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.TimeAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ProductlineServiceImpl extends BaseServiceImpl<ProductlineInfo> implements ProductlineService {

    @Resource
    ProductlineInfoMapper mapper;

    /**
     * 分页查询日志列表
     *
     * @param pageNum
     * @param pageSize
     * @param productlineName
     * @param startTime
     * @param endTime
     * @return
     */
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

        return new PageInfo<>(list);
    }

    /**
     * 根据createTime查询最新的历史数据
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductlineInfo> findNewestHistoryData() {
        return mapper.findByNewestCreateTime();
    }

    /**
     * 根据生产线名称返回熔窑压力实时数据
     *
     * @param productlineName
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Option selectProductlinePress(String productlineName) {
        //查询前10
        PageHelper.startPage(1, 10);
        //数据库查询获取统计数据
        Example example = new Example(ProductlineInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productlineName)) {
            criteria.andLike("productlineName", "%" + productlineName + "%");
        }
        //倒序
        example.orderBy("createTime").desc();
        List<ProductlineInfo> list = mapper.selectByExample(example);
        //创建Option
        Option option = new Option();
        //设置tooltip
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("");
        tooltip.setTrigger(Trigger.axis);
        //设置title
        Title title = new Title();
        title.setText("熔窑压力实时数据");
        title.setX("center");
        option.title(title).tooltip(tooltip);
        //折线图
        Line line = new Line();
        //设置折线图参数
        line.name("熔窑压力");
        line.smooth(true);
        //从尾部开始循环数据，读取时间最新的10个数据
        for (int i = list.size() - 1; i >= 0; i--) {
            ProductlineInfo productlineInfo = list.get(i);
            List<Object> dataList = new ArrayList<>();
            dataList.add(productlineInfo.getCreateTime());
            dataList.add(productlineInfo.getPressure());
            //填充数据
            line.data(dataList);
        }
        //设置X轴
        option.xAxis(new TimeAxis().name("当前时间"));
        //设置Y轴
        option.yAxis(new ValueAxis().boundaryGap("20%", "20%").name("压力(10^5Pa)"));
        //设置数据
        option.series(line);
        //返回Option
        return option;
    }

    /**
     * 根据生产线名称返回熔窑温度实时数据
     *
     * @param productlineName
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Option selectProductlineTemp(String productlineName) {
        //查询前10
        PageHelper.startPage(1, 10);
        //数据库查询获取统计数据
        Example example = new Example(ProductlineInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productlineName)) {
            criteria.andLike("productlineName", "%" + productlineName + "%");
        }
        //倒序
        example.orderBy("createTime").desc();
        List<ProductlineInfo> list = mapper.selectByExample(example);
        //创建Option
        Option option = new Option();
        //设置tooltip
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("");
        tooltip.setTrigger(Trigger.axis);
        //设置title
        Title title = new Title();
        title.setText("熔窑温度实时数据");
        title.setX("center");
        //设置legend
        Legend legend = new Legend("工作部温度", "熔化部温度");
        legend.setX("left");
        option.title(title).tooltip(tooltip).legend(legend);

        //工作部温度折线图
        Line workTempLine = new Line();
        //设置折线图参数
        workTempLine.smooth(true);
        workTempLine.name("工作部温度");
        //从尾部开始循环数据，读取时间最新的10个数据
        for (int i = list.size() - 1; i >= 0; i--) {
            ProductlineInfo productlineInfo = list.get(i);
            List<Object> dataList = new ArrayList<>();
            dataList.add(productlineInfo.getCreateTime());
            dataList.add(productlineInfo.getWorkTemp());
            //填充数据
            workTempLine.data(dataList);
        }

        //熔化部温度折线图
        Line meltTempLine = new Line();
        //设置折线图参数
        meltTempLine.smooth(true);
        meltTempLine.name("熔化部温度");
        //从尾部开始循环数据，读取时间最新的10个数据
        for (int i = list.size() - 1; i >= 0; i--) {
            ProductlineInfo productlineInfo = list.get(i);
            List<Object> dataList = new ArrayList<>();
            dataList.add(productlineInfo.getCreateTime());
            dataList.add(productlineInfo.getMeltTemp());
            //填充数据
            meltTempLine.data(dataList);
        }

        //设置X轴
        option.xAxis(new TimeAxis().name("当前时间"));
        //设置Y轴
        option.yAxis(new ValueAxis().boundaryGap("20%", "20%").name("温度(℃)"));
        //设置数据
        List<Series> series = new ArrayList<>();
        series.add(workTempLine);
        series.add(meltTempLine);
        option.series(series);
        //返回Option
        return option;
    }

    /**
     * 根据生产线名称返回熔窑液面高度实时数据
     *
     * @param productlineName
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Option selectProductlineHeight(String productlineName) {
        //查询前10
        PageHelper.startPage(1, 10);
        //数据库查询获取统计数据
        Example example = new Example(ProductlineInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productlineName)) {
            criteria.andLike("productlineName", "%" + productlineName + "%");
        }
        //倒序
        example.orderBy("createTime").desc();
        List<ProductlineInfo> list = mapper.selectByExample(example);
        //创建Option
        Option option = new Option();
        //设置tooltip
        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("");
        tooltip.setTrigger(Trigger.axis);
        //设置title
        Title title = new Title();
        title.setText("熔窑液面高度实时数据");
        title.setX("center");
        option.title(title).tooltip(tooltip);
        //折线图
        Line line = new Line();
        //设置折线图参数
        line.name("液面高度");
        line.smooth(true);
        //从尾部开始循环数据，读取时间最新的10个数据
        for (int i = list.size() - 1; i >= 0; i--) {
            ProductlineInfo productlineInfo = list.get(i);
            List<Object> dataList = new ArrayList<>();
            dataList.add(productlineInfo.getCreateTime());
            dataList.add(productlineInfo.getLiquidLevel());
            //填充数据
            line.data(dataList);
        }
        //设置X轴
        option.xAxis(new TimeAxis().name("当前时间"));
        //设置Y轴
        option.yAxis(new ValueAxis().boundaryGap("20%", "20%").name("液面高度(cm)"));
        //设置数据
        option.series(line);
        //返回Option
        return option;
    }
}
