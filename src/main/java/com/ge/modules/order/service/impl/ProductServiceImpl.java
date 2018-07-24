package com.ge.modules.order.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.order.model.Product;
import com.ge.modules.order.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ProductServiceImpl extends BaseServiceImpl<Product> implements ProductService {

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Product> findProductPage(Integer pageNum, Integer pageSize, String productType) {
        Example example = new Example(Product.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productType)) {
            criteria.andLike("type", "%" + productType + "%");
        }
        // 倒序
        example.orderBy("modifyTime").desc();
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productsList = this.selectByExample(example);

        return new PageInfo<>(productsList);
    }
}
