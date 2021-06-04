package com.usian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.TbContentMapper;
import com.usian.mapper.TbItemCatMapper;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.service.ItemParamService;
import com.usian.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private TbContentMapper tbContentMapper;


    @Autowired
    private RedisClient redisClient;


    @Override
    public List<AdNode> selectFrontendContentByAD() {
        //查询缓存
        List<AdNode> adNodeListRedis =
                (List<AdNode>)redisClient.hget("PORTAL_AD_KEY","AD_CATEGORY_ID".toString());
        if(adNodeListRedis!=null){
            return adNodeListRedis;
        }
        // 查询数据库
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(Constant.common_categry_id);
        List<TbContent> tbContentList =
                tbContentMapper.selectByExample(tbContentExample);
        List<AdNode> adNodeList = new ArrayList<AdNode>();
        for (TbContent tbContent : tbContentList) {
            AdNode adNode = new AdNode();
            adNode.setSrc(tbContent.getPic());
            adNode.setSrcB(tbContent.getPic2());
            adNode.setHref(tbContent.getUrl());
            adNode.setHeight(Constant.common_heightB);
            adNode.setWidth(Constant.common_width);
            adNode.setHeightB(Constant.common_heightB);
            adNode.setWidthB(Constant.common_widthB);
            adNodeList.add(adNode);
        }
        //添加到缓存
        redisClient.hset("PORTAL_AD_KEY","AD_CATEGORY_ID".toString(),adNodeList);
        return adNodeList;
    }


    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询所有商品规格模板
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult selectItemParamAll(Integer page, Integer rows) {
        PageHelper.startPage (page,rows);
        TbItemParamExample example = new TbItemParamExample();
        example.setOrderByClause("updated DESC");
        List<TbItemParam> list =
                this.tbItemParamMapper.selectByExampleWithBLOBs(example);
        PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(page);
        pageResult.setResult(pageInfo.getList());
        pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
        return pageResult;
    }

    @Override
    public Integer insertItemParam(Long itemCatId, String paramData) {
        //1、判断该类别的商品是否有规格模板
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = tbItemParamExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> itemParamList =
                tbItemParamMapper.selectByExample(tbItemParamExample);
        if(itemParamList.size()>0){
            return 0;
        }

        //2、保存规格模板
        Date date = new Date();
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(itemCatId);
        tbItemParam.setParamData(paramData);
        tbItemParam.setUpdated(date);
        tbItemParam.setCreated(date);
        return tbItemParamMapper.insertSelective(tbItemParam);
    }

    @Override
    public Integer deleteItemParamById(Long id) {
        return tbItemParamMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询首页商品分类
     * @return
     */
    /**
     * 查询首页商品分类
     * @return
     */
    @Override
    public CatResult selectItemCategoryAll() {
        //查询缓存
        CatResult catResultRedis =
                (CatResult)redisClient.get("PROTAL_CATRESULT_KEY");
        if(catResultRedis!=null){
            return catResultRedis;
        }
        CatResult catResult = new CatResult();
        //查询商品分类
        catResult.setData(getCatList(0L));

        //添加到缓存
        redisClient.set("PROTAL_CATRESULT_KEY",catResult);

        return catResult;
    }

    /**
     * 私有方法，查询商品分类
     */
    private List<?> getCatList(Long parentId){
        //创建查询条件
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = this.tbItemCatMapper.selectByExample(example);
        List resultList = new ArrayList();
        int count = 0;
        for(TbItemCat tbItemCat:list){
            //判断是否是父节点
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));//查询子节点
                resultList.add(catNode);
                count++;
                //只取商品分类中的 18 条数据
                if (count == 18){
                    break;
                }
            }else{
                resultList.add(tbItemCat.getName());
            }
        }
        return resultList;
    }

}