package com.usian.controller;

import com.usian.pojo.CartTermVo;
import com.usian.pojo.TbItem;
import com.usian.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cart")
public class CartController {


    @Autowired
    private CartService cartService;
    @RequestMapping("/addItem")
    public void addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId){
        cartService.addItem(userId,itemId);
    }

    @RequestMapping("/showCart")
    public List<CartTermVo> getCart(@RequestParam("userId") Long userId){
        return cartService.getCart(userId);
    }
    /**
     * 根据用户 ID 查询用户购物车
     */
    @RequestMapping("/selectCartByUserId")
    public CartTermVo selectCartByUserId(@RequestParam String userId,@RequestParam Long itemId){
        return this.cartService.selectCartByUserId(userId,itemId);
    }

    /**
     * 将购物车缓存到 redis 中
     */
    @RequestMapping("/insertCart")
    public Boolean insertCart(String userId,Long itemId, @RequestBody CartTermVo cart) {
        return this.cartService.insertCart(userId,itemId, cart);
    }
    @RequestMapping("/del")
    public void del(Long itemId,String userId) {
        cartService.del(itemId,userId);
    }

}
