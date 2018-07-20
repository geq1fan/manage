package com.ge.modules.productline.service;

import com.ge.common.service.BaseService;
import com.ge.modules.productline.model.ProductlineInfo;
import com.github.abel533.echarts.Option;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProductlineService extends BaseService<ProductlineInfo> {

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
    PageInfo<ProductlineInfo> findHistoryDataPage(Integer pageNum, Integer pageSize, String productlineName, String startTime, String endTime);

    /**
     * 根据createTime查询最新的历史数据
     *
     * @return
     */
    List<ProductlineInfo> findNewestHistoryData();

    /**
     * 根据生产线名称返回熔窑压力实时数据
     *
     * @param productlineName
     * @return
     */
    Option selectProductlinePress(String productlineName);

    /**
     * 根据生产线名称返回熔窑温度实时数据
     *
     * @param productlineName
     * @return
     */
    Option selectProductlineTemp(String productlineName);

    /**
     * 根据生产线名称返回熔窑液面高度实时数据
     *
     * @param productlineName
     * @return
     */
    Option selectProductlineHeight(String productlineName);
}
