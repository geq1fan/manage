package com.ge.modules.inventory.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.inventory.model.Inventory;
import com.ge.modules.inventory.service.InventoryService;
import com.ge.modules.order.model.Product;
import com.ge.modules.order.service.ProductService;
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

@Service
public class InventoryServiceImpl extends BaseServiceImpl<Inventory> implements InventoryService {

    @Resource
    private
    ProductService productService;

    @Transactional(readOnly = true)
    @Override
    public Inventory findByProductName(String type) {
        Inventory inventory = new Inventory();
        inventory.setType(type);
        return this.findOne(inventory);
    }

    @Override
    public Boolean updateProductInventory(Inventory inventory) {
        String productType = inventory.getType();
        if (StringUtils.isNotEmpty(productType) && (inventory.getCategory() == true)) {
            Product record = new Product();
            record.setType(productType);
            Product product = productService.findOne(record);

            Example example = new Example(Inventory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("type", "%" + productType + "%");
            //选择成品库存
            criteria.andEqualTo("category", true);
            List<Inventory> InventorysList = this.selectByExample(example);
            int inventorySum = 0;
            for (Inventory in : InventorysList) {
                inventorySum += in.getInventory();
            }

            product.setInventory(inventorySum);
            productService.update(product);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Inventory> findProductPage(Integer pageNum, Integer pageSize, String productType) {
        Example example = new Example(Inventory.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productType)) {
            criteria.andLike("type", "%" + productType + "%");
        }
        // 倒序
        example.orderBy("createTime").desc();
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        List<Inventory> InventorysList = this.selectByExample(example);

        return new PageInfo<>(InventorysList);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Inventory> findInventoryPage(Integer pageNum, Integer pageSize, String productType, String startTime,
                                                 String endTime) {
        Example example = new Example(Inventory.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productType)) {
            criteria.andLike("type", "%" + productType + "%");
        }
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            criteria.andBetween("createTime", DateUtil.beginOfDay(DateUtil.parse(startTime)),
                    DateUtil.endOfDay(DateUtil.parse(endTime)));
        }
        // 倒序
        example.orderBy("createTime").desc();
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        List<Inventory> InventorysList = this.selectByExample(example);

        return new PageInfo<>(InventorysList);
    }
}
