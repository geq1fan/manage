package com.ge.modules.cms.service;

import com.ge.common.service.BaseService;
import com.ge.modules.cms.model.ContentCat;
import com.ge.modules.sys.vo.TreeNode;

import java.util.List;

/**
 * @author cuiP
 * Created by JK on 2017/4/19.
 */
public interface ContentCatService extends BaseService<ContentCat> {
    /**
     * 查询所有分类
     * @return
     */
    List<ContentCat> findAllCat();

    /**
     * 返回树列表
     * @return
     */
    List<TreeNode> findTreeList();
}
