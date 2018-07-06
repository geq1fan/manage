package com.ge.modules.cms.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.cms.mapper.ContentCatMapper;
import com.ge.modules.cms.model.ContentCat;
import com.ge.modules.cms.service.ContentCatService;
import com.ge.modules.sys.vo.TreeNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cuiP
 * Created by JK on 2017/4/19.
 */
@Transactional
@Service
public class ContentCatServiceImpl extends BaseServiceImpl<ContentCat> implements ContentCatService {

    @Resource
    private ContentCatMapper contentCatMapper;

    @Transactional(readOnly=true)
    @Override
    public List<TreeNode> findTreeList() {
        return contentCatMapper.findTreeList();
    }

    @Transactional(readOnly = true)
	@Override
	public List<ContentCat> findAllCat() {
		List<ContentCat> catList = super.findAll();
		return catList;
	}
}
