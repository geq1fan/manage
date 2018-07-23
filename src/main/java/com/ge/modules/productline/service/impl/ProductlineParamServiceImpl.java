package com.ge.modules.productline.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.productline.model.ProductlineParam;
import com.ge.modules.productline.service.ProductlineParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductlineParamServiceImpl extends BaseServiceImpl<ProductlineParam> implements ProductlineParamService {
}
