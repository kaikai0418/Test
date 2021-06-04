package com.usian.service;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.PageResult;

import java.util.List;

public interface ContentCategoryService {
    List<TbContentCategory> selectContentCategoryByParentId(Long parentId);

    void insertContentCategory(Long parentId, String name);

    Integer deleteContentCategoryById(Long categoryId);

    void updateContentCategory(Long id, String name);

    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);

    void insertTbContent(TbContent tbContent);

    void deleteContentByIds(Long ids);
}
