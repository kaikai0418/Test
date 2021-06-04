package com.usian.controller;


import com.usian.fegin.ItemServiceFeign;
import com.usian.utils.AdNode;
import com.usian.utils.CatResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/frontend/itemCategory")
@RestController
public class ItemCategoryController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;


    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll(){
        CatResult catResult =itemServiceFeign.selectItemCategoryAll();
        return  Result.ok(catResult);
    }
//    selectFrontendContentByAD
    @RequestMapping("/selectFrontendContentByAD")
    public Result selectFrontendContentByAD() {
    List<AdNode> list = itemServiceFeign.selectFrontendContentByAD();
    if (list != null && list.size() > 0) {
        return Result.ok(list);
    }
    return Result.error("查无结果");
}

}
