package com.usian.fegin;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "usian-content-service")
public interface ContentServiceFeign {

    @PostMapping("/service/content/selectContentCategoryByParentId")
    List<TbContentCategory> selectContentCategoryByParentId(@RequestParam("parentId")
                                                                    Long parentId);

    @PostMapping("/service/content/insertContentCategory")
    void insertContentCategory(@RequestParam("parentId") Long parentId,@RequestParam("name")  String name);

    @PostMapping("/service/content/deleteContentCategoryById")
    public Integer deleteContentCategoryById(@RequestParam("categoryId") Long categoryId);


    @PostMapping("/service/content/updateContentCategory")
    public void updateContentCategory(@RequestParam("id") Long id,@RequestParam("name")String name);

    @PostMapping("/service/content/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(@RequestParam("page") Integer page,
                                              @RequestParam("rows") Integer rows,
                                              @RequestParam("categoryId") Long categoryId);

    @PostMapping("/service/content/insertTbContent")
    public void insertTbContent(TbContent tbContent);

    @RequestMapping("/service/content/deleteContentByIds")
    public void deleteContentByIds(@RequestParam("ids") Long ids);
}


