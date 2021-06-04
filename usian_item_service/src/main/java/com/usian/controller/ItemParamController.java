package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.AdNode;
import com.usian.utils.CatResult;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/itemParam")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;



    @GetMapping("selectFrontendContentByAD")
    public List<AdNode> selectFrontendContentByAD(){
      return   itemParamService.selectFrontendContentByAD();
    }

    @RequestMapping("/selectItemCategoryAll")
    public CatResult selectItemCategoryAll(){
       return itemParamService.selectItemCategoryAll();
    }

    /**
     * 根据商品分类 ID 查询规格参数模板
     */
    @RequestMapping("/selectItemParamByItemCatId")
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        return this.itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    /**
     * 查询所有商品规格模板
     */
    @RequestMapping("/selectItemParamAll")
    public PageResult selectItemParamAll(Integer page, Integer rows){
        return this.itemParamService.selectItemParamAll(page,rows);
    }

    @RequestMapping("/insertItemParam")
    public Integer insertItemParam(Long itemCatId, String paramData){
        return itemParamService.insertItemParam(itemCatId,paramData);
    }

    @RequestMapping("/deleteItemParamById")
    public Integer deleteItemParamById(Long id){
        return itemParamService.deleteItemParamById(id);
    }
}