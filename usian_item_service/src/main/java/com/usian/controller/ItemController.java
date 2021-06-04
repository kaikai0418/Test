package com.usian.controller;

import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemDescExample;
import com.usian.redis.RedisClient;
import com.usian.service.ItemService;
import com.usian.utils.AdNode;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/service/item")
public class ItemController {
    @Autowired
    private ItemService itemService;




    /**查询商品信息
     * 根据商品id
     * @param itemId
     * @return
     */
    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(Long itemId){
        return this.itemService.selectItemInfo(itemId);
    }

    @RequestMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(Integer page, Integer rows){
        return this.itemService.selectTbItemAllByPage(page,rows);
    }

    @RequestMapping("/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(Long itemId){
        return  itemService.selectItemDescByItemId(itemId);
    }

    /**
     * 商品的添加
     */
    @RequestMapping("/insertTbItem")
    public Integer insertTbItem(@RequestBody TbItem tbItem,String desc,
                                String itemParams){
        return this.itemService.insertTbItem(tbItem,desc,itemParams);
    }

    /**
     * 商品的修改
     */
    @RequestMapping("/updateTbItem")
    public void updateTbItem(@RequestBody TbItem tbItem,String desc,
                                String itemParams){
         this.itemService.updateTbItem(tbItem,desc,itemParams);
    }

    /**
     * 根据商品 ID 查询商品，商品分类，商品描述，商品规格参数
     * @param itemId
     * @return
     */
    @RequestMapping("/preUpdateItem")
    public Map<String,Object> preUpdateItem(Long itemId){
        return this.itemService.preUpdateItem(itemId);
    }

    @RequestMapping("/deleteItemById")
    public void deleteItemById(Long itemId){
        this.itemService.deleteItemById(itemId);
    }



}