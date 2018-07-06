package com.ge.modules.productline.service;

import java.util.List;

import com.ge.common.service.BaseService;
import com.ge.modules.productline.model.ProductlineInfo;
import com.github.pagehelper.PageInfo;

public interface ProductlineService extends BaseService<ProductlineInfo>{
	
	/**
     * 分页查询日志列表
     * @param pageNum
     * @param pageSize
     * @param username
     * @param startTime
     * @param endTime
     * @return
     */
    PageInfo<ProductlineInfo> findHistoryDataPage(Integer pageNum, Integer pageSize, String productlineName, String startTime, String endTime);
    
    List<ProductlineInfo> findNewestHistoryData();
}
