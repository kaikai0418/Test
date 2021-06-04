package com.usian.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.mapper.*;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.service.ItemService;
import com.usian.utils.AdNode;
import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;


    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;



    /*
     * 查询商品信息
     * @param itemId
     * @return
     */
    @Override
    public TbItem selectItemInfo(Long itemId) {
        //查询缓存
        TbItem tbItem = (TbItem) redisClient.get("ITEM_INFO" + ":" + itemId + ":"+ "BASE");
        if(tbItem!=null){
            return tbItem;
        }

        tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        //把数据保存到缓存
        redisClient.set("ITEM_INFO" + ":" + itemId + ":"+ "BASE",tbItem);
        //设置缓存的有效期
        redisClient.expire("ITEM_INFO" + ":" + itemId + ":"+ "BASE",86400);

        return tbItem;
    }
    /**
     * 根据商品 ID 查询商品描述
     * @param itemId
     * @return
     */
    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        //查询缓存
        TbItemDesc tbItemDesc = (TbItemDesc) redisClient.get(
                "ITEM_INFO" + ":" + itemId + ":"+ "DESC");
        if(tbItemDesc!=null){
            return tbItemDesc;
        }

        TbItemDescExample example = new TbItemDescExample();
        TbItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemDesc> itemDescList =
                this.tbItemDescMapper.selectByExampleWithBLOBs(example);
        if(itemDescList!=null && itemDescList.size()>0){
            //把数据保存到缓存
            redisClient.set("ITEM_INFO" + ":" + itemId + ":"+ "DESC",itemDescList.get(0));
            //设置缓存的有效期
            redisClient.expire("ITEM_INFO" + ":" + itemId + ":"+ "DESC",86400);
            return itemDescList.get(0);
        }
        return null;
    }



    /*
---------------------------------------------------*/
/*    @Override
    public TbItem selectItemInfo(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }*/

    /**
     * 查询所有商品，并分页。
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        //查询状态是1 并且  按修改时间逆序排列
        TbItemExample tbItemExample = new TbItemExample();
        tbItemExample.setOrderByClause("updated DESC");
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        for (int i = 0; i < tbItemList.size(); i++) {
            TbItem tbItem =  tbItemList.get(i);
            tbItem.setPrice(tbItem.getPrice()/100);

        }
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(tbItemList);
        //返回PageResult
        PageResult pageResult = new PageResult();
        pageResult.setResult(tbItemPageInfo.getList());
        pageResult.setTotalPage(Long.valueOf(tbItemPageInfo.getPages()));
        pageResult.setPageIndex(tbItemPageInfo.getPageNum());
        return pageResult;
    }

    @Override
    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        //补齐 Tbitem 数据
        Long itemId = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        tbItem.setPrice(tbItem.getPrice()*100);
        Integer tbItemNum = tbItemMapper.insertSelective(tbItem);

        //补齐商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        Integer tbitemDescNum = tbItemDescMapper.insertSelective(tbItemDesc);

        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        Integer itemParamItmeNum =
         tbItemParamItemMapper.insertSelective(tbItemParamItem);


        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());

        ItemESVO itemESVO = new ItemESVO();
        itemESVO.setId(itemId);
        itemESVO.setItemSellPoint(tbItem.getSellPoint());
        itemESVO.setItemPrice(tbItem.getPrice()+"");
        itemESVO.setItemImage(tbItem.getImage());
        itemESVO.setItemTitle(tbItem.getTitle());

        itemESVO.setItemCategoryName(tbItemCat.getName());
        itemESVO.setItemDesc(tbItemDesc.getItemDesc());

     /*   //新增数据到ES
        amqpTemplate.convertAndSend("item_exchange","item.insert",itemESVO);*/

        return tbItemNum + tbitemDescNum + itemParamItmeNum;
    }

    @Override
    public void updateTbItem(TbItem tbItem, String desc, String itemParams) {
        //补齐 Tbitem 数据

        Date date = new Date();
        tbItem.setStatus((byte)1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        tbItem.setPrice(tbItem.getPrice()*100);
        Integer tbItemNum = tbItemMapper.updateByPrimaryKeySelective(tbItem);

        //补齐商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        Integer tbitemDescNum = tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);

        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setCreated(date);
        Integer itemParamItmeNum =
                tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);


    }

    @Override
    public void deleteItemById(Long itemId) {
        tbItemMapper.deleteByPrimaryKey(itemId);
    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        Map<String, Object> map = new HashMap<>();
        //根据商品 ID 查询商品
        TbItem item = this.tbItemMapper.selectByPrimaryKey(itemId);
        map.put("item", item);
        //根据商品 ID 查询商品描述
        TbItemDesc itemDesc = this.tbItemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc", itemDesc.getItemDesc());
        //根据商品 ID 查询商品类目
        TbItemCat itemCat = this.tbItemCatMapper.selectByPrimaryKey(item.getCid());
        map.put("itemCat", itemCat.getName());
        //根据商品 ID 查询商品规格信息
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list =
                this.tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            map.put("itemParamItem", list.get(0).getParamData());
        }
        return map;
    }
}
