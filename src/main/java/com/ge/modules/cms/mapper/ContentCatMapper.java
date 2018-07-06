package com.ge.modules.cms.mapper;

import com.ge.modules.cms.model.ContentCat;
import com.ge.common.mapper.BaseMapper;
import com.ge.modules.sys.vo.TreeNode;

import java.util.List;

/**
 * @author cuiP
 * Created by JK on 2017/4/19.
 */
public interface ContentCatMapper extends BaseMapper<ContentCat> {
    /**
     * 返回树列表
     * @return
     */
    List<TreeNode> findTreeList();
}
