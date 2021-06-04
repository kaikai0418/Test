package com.usian.service;

import com.usian.pojo.CartTermVo;
import com.usian.pojo.OrderVo;

import java.util.List;

public interface OrderService {
    List<CartTermVo> goSettlement(Long[] ids, Long userId);

    String insertOrder(OrderVo orderVo);
}
