package com.usian.controller;


import com.usian.fegin.ItemServiceFeign;
import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {


    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId) {

        TbItemParam tbItemParam =
                this.itemServiceFeign.selectItemParamByItemCatId(itemCatId);
        if(tbItemParam != null){
            return Result.ok(tbItemParam);
        }
        return Result.error("查无结果");
    }

    /**
     * 查询商品规格
     */
    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(defaultValue = "1") Integer
                                             page,@RequestParam(defaultValue = "3") Integer rows){
        PageResult pageResult =
                this.itemServiceFeign.selectItemParamAll(page,rows);
            return Result.ok(pageResult);
    }


    /**
     * 添加商品规格模板
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping("/insertItemParam")
    public Result insertItemParam(Long itemCatId,String paramData){
        Integer num = itemServiceFeign.insertItemParam(itemCatId,paramData);
        if(num==1){
            return Result.ok();
        }
        return Result.error("添加失败：该类目已有规格模板");
    }

    /*/backend/item/preUpdateItem*/
    @RequestMapping("/deleteItemParamById")
    public Result deleteItemById(Long id){
        itemServiceFeign.deleteItemParamById(id);

        return Result.ok("删除成功");

    }


}
