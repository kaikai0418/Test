package com.usian.controller;


import com.usian.fegin.ItemServiceFeign;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/itemCategory")
public class ItemCategoryController {


    @Autowired
    private ItemServiceFeign itemServiceFeign;


    /*查询类目的方法*/

    @RequestMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(defaultValue = "0")Long id) {
       List<TbItemCat> itemCats= itemServiceFeign.selectItemCategoryByParentId(id);
        return Result.ok(itemCats);
    }
}
