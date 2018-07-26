package com.ge.modules.inventory.service;

import com.ge.common.service.BaseService;
import com.ge.modules.inventory.model.Inventory;
import com.github.pagehelper.PageInfo;

public interface InventoryService extends BaseService<Inventory> {

    /**
     * 根据产品规格找到库存列表
     *
     * @param pageNum
     * @param pageSize
     * @param productType
     * @return
     */
    PageInfo<Inventory> findProductPage(Integer pageNum, Integer pageSize, String productType);

    /**
     * 根据产品规格和起始日期找到库存列表
     *
     * @param pageNum
     * @param pageSize
     * @param productType
     * @param startTime
     * @param endTime
     * @return
     */
    PageInfo<Inventory> findInventoryPage(Integer pageNum, Integer pageSize, String productType, String startTime,
                                          String endTime);

    /**
     * 根据产品规格找到库存信息
     *
     * @param productType
     * @return
     * @throws Exception
     */
    Inventory findByProductName(String productType) throws Exception;

    /**
     * 根据产品规格和是否为成品来更新产品总库存
     *
     * @param inventory
     * @return
     */
    Boolean updateProductInventory(Inventory inventory);

    /**
     * 根据产品id更新产品库存
     *
     * @param productId
     * @return
     */
    void updateCompletedInventory(Long productId);
}
