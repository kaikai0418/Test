package com.usian.service;

import com.usian.pojo.SearchItem;

import java.util.List;

public interface SearchItemService {

    Boolean importAll();

    List<SearchItem> selectByq(String q, Long page, Integer pageSize);
}
