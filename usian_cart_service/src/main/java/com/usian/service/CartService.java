package com.usian.service;

import com.usian.pojo.CartTermVo;
import com.usian.pojo.TbItem;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface CartService {
    void addItem(Long userId, Long itemId);

    List<CartTermVo> getCart(Long userId);

    CartTermVo selectCartByUserId(String userId,Long itemId);

    Boolean insertCart(String userId,Long itemId ,CartTermVo cart);

    void del(Long itemId,String userId);
}
