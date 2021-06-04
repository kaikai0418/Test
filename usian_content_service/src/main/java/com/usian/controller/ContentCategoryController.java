package com.usian.controller;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCategoryService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service/content")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据父节点 ID 查询子节点
     */
    /**
     * 根据父节点 ID 查询子节点
     */
    @RequestMapping("/selectContentCategoryByParentId")
    public List<TbContentCategory>
    selectContentCategoryByParentId(@RequestParam Long parentId) {
        return this.contentCategoryService.selectContentCategoryByParentId(parentId);
    }


    @RequestMapping("insertContentCategory")
    void insertContentCategory(Long parentId,String name){
        contentCategoryService.insertContentCategory(parentId,name);
    }



    /**
     * 删除内容分类
     */
    @RequestMapping("/deleteContentCategoryById")
    public Integer deleteContentCategoryById(Long categoryId){
        return this.contentCategoryService.deleteContentCategoryById(categoryId);
    }

    @RequestMapping("/updateContentCategory")
    public void updateContentCategory(Long id,String name){
        contentCategoryService.updateContentCategory(id,name);
    }

    @RequestMapping("/selectTbContentAllByCategoryId")
    public PageResult selectTbContentAllByCategoryId(@RequestParam Integer page,
                                                     @RequestParam Integer rows, @RequestParam Long categoryId) {
        return this.contentCategoryService.selectTbContentAllByCategoryId(
                page, rows, categoryId);
    }

        //添加内容管理
    @RequestMapping("insertTbContent")
    void insertTbContent(@RequestBody TbContent tbContent){
        contentCategoryService.insertTbContent(tbContent);
    }

    //删除内容管理

    @RequestMapping("/deleteContentByIds")
    public void deleteContentByIds(Long ids){
         this.contentCategoryService.deleteContentByIds(ids);
    }

}