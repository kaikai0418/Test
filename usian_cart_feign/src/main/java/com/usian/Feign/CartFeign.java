package com.usian.Feign;


import com.usian.pojo.CartTermVo;
import com.usian.pojo.TbItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "usian-cart-service")
public interface CartFeign {

    @RequestMapping("/cart/showCart")
    public List<CartTermVo> getCart(@RequestParam("userId") Long userId);

    @RequestMapping("/cart/addItem")
    public void addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId);

    @RequestMapping("/cart/selectCartByUserId")
    CartTermVo selectCartByUserId(@RequestParam String userId,@RequestParam Long itemId);

    @RequestMapping("/cart/insertCart")
    Boolean insertCart(@RequestParam String userId,@RequestParam Long itemId,CartTermVo cart);

    @RequestMapping("/cart/del")
    void del(@RequestParam Long itemId,@RequestParam String userId);
}

