package com.usian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbContentCategoryMapper;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import com.usian.pojo.TbContentExample;
import com.usian.service.ContentCategoryService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Autowired
    private TbContentMapper tbContentMapper;

    /**
     * 根据父节点 Id 查询子节点
     *
     * @param parentId
     * @return
     */
    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long parentId) {
        TbContentCategoryExample tbContentCategoryExample = new
                TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria =
                tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list =
                this.tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        return list;
    }

    @Override
    public void insertContentCategory(Long parentId, String name) {
        TbContentCategory tb = new TbContentCategory();
        tb.setParentId(parentId);
        tb.setName(name);

        Date date = new Date();
        tb.setCreated(date);
        tb.setIsParent(false);
        tb.setStatus(1);
        tb.setSortOrder(1);
        tbContentCategoryMapper.insertSelective(tb);

        TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!category.getIsParent()){
            category.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(tb);
        }
    }
    /**
     * 删除内容分类
     * @param categoryId
     * @return
     */
    @Override
    public Integer deleteContentCategoryById(Long categoryId) {
        //查询当前节点
        TbContentCategory tbContentCategory =
                this.tbContentCategoryMapper.selectByPrimaryKey(categoryId);
        //父节点 不允许删除
        if(tbContentCategory.getIsParent()==true){
            return 0;
        }
        //不是父节点
        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        //当前节点的兄弟节点
        TbContentCategoryExample tbContentCategoryExample = new
                TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria =
                tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        List<TbContentCategory> tbContentCategoryList =
                tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        //删除之后如果父节点没有孩子，则修改isParent为false
        if(tbContentCategoryList.size()==0){
            TbContentCategory parenttbContentCategory = new TbContentCategory();
            parenttbContentCategory.setId(tbContentCategory.getParentId());
            parenttbContentCategory.setIsParent(false);
            parenttbContentCategory.setUpdated(new Date());
            this.tbContentCategoryMapper.updateByPrimaryKeySelective(
                    parenttbContentCategory);
        }
        return 200;
    }


    //修改
    @Override
    public void updateContentCategory(Long id, String name) {

        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        tbContentCategory.setName(name);
        tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }

    @Override
    public PageResult selectTbContentAllByCategoryId(Integer page, Integer rows,
                                                     Long categoryId) {
        PageHelper.startPage(page, rows);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = this.tbContentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
        PageResult result = new PageResult();
        result.setPageIndex(pageInfo.getPageNum());
        result.setTotalPage(pageInfo.getTotal());
        result.setResult(pageInfo.getList());
        return result;
    }

    //添加内容管理
    @Override
    public void insertTbContent(TbContent tbContent) {
        tbContent.setPic2("");
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.insert(tbContent);
    }

    @Override
    public void deleteContentByIds(Long ids) {
        tbContentMapper.deleteByPrimaryKey(ids);
    }


}