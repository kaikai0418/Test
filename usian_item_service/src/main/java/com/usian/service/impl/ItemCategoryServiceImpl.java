package com.usian.service.impl;


import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;
import com.usian.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public  List<TbItemCat> selectItemCategoryByParentId(Long id) {


        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria1 = tbItemCatExample.createCriteria();
        criteria1.andParentIdEqualTo(id);
        List<TbItemCat> list1 = this.tbItemCatMapper.selectByExample(tbItemCatExample);

        return list1;
    }


}
