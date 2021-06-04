package com.usian.controller;

import com.usian.Feign.CartFeign;
import com.usian.fegin.ItemServiceFeign;
import com.usian.pojo.CartTermVo;
import com.usian.pojo.TbItem;
import com.usian.utils.CookieUtils;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 购物车 Controller
 */
@RestController
@RequestMapping("/frontend/cart")
public class CartController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @Autowired
    private CartFeign cartFeign;




    @RequestMapping("/deleteItemFromCart")
    public Result  deleteItemFromCart(@RequestParam Long itemId, @RequestParam String userId, HttpServletRequest
            request, HttpServletResponse response){
        try {
            if (StringUtils.isBlank(userId)) {
                //在用户未登录的状态下
                //.........

            } else {
                // 在用户已登录的状态
                //登录
                //1、获得redis中的购物车
                CartTermVo cart = getCartFromRedis(userId,itemId);
                //2、删除购物车中的商品
                 cartFeign.del(itemId,userId);
                //3、把购物车写到redis中
               /* addCartToRedis(userId,itemId,cart);*/

            }
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("删除失败");
    }





    @RequestMapping("/showCart")
    public Result showCart(@RequestParam("userId") Long userId,HttpServletRequest request){

        if(userId==null){//没有登入
          String cartJson =  CookieUtils.getCookieValue(request,"CART",true);
            Map<Long,CartTermVo> map = JsonUtils.jsonToMap(cartJson,Long.class, CartTermVo.class);
            Collection<CartTermVo> cartTerms = map.values();
            return Result.ok(cartTerms);
        }else{
            //
          List<CartTermVo> list=  cartFeign.getCart(userId);
            return Result.ok(list);
        }
    }



    @RequestMapping("/addItem")
    public Result addItem(@RequestParam("userId") Long userId,@RequestParam("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response){
         if (userId==null) {
             //没登入 在cookie 里面存储数据
             //1判断这个用户是不是第一次使用购物车
             String cartJson = CookieUtils.getCookieValue(request, "CART", true);
             if (cartJson == null || cartJson.equals("")) {
                 //1如果第一次使用购物车，创建一个购物车得cookie，
                 HashMap<Long, CartTermVo> cartMap = new HashMap<>();
                 //新增到购物车
                 addCartTerm(itemId, cartMap);
                 CookieUtils.setCookie(request, response, "CART", JsonUtils.objectToJson(cartMap), true);
             } else {//1.2如果不是第一次使用购物车，获取上一次购物车cookie值，添加即可
                 Map<Long, CartTermVo> cart = JsonUtils.jsonToMap(cartJson,Long.class, CartTermVo.class);
                 CartTermVo cartTermVo = cart.get(itemId);
                 if (cartTermVo == null) {
                     //没有加入过购物车，新增一个购物车
                     addCartTerm(itemId, cart);
                 } else {
                     //如果添加过 修改数量
                     cartTermVo.setNum(cartTermVo.getNum() + 1);
                 }
                 CookieUtils.setCookie(request, response, "CART", JsonUtils.objectToJson(cart), true);
             }
         }else{//登入的
                cartFeign.addItem(userId,itemId);
         }
            return Result.ok();
    }

    public void addCartTerm(Long itemId,Map cartMap){
        TbItem item = itemServiceFeign.selectItemInfo(itemId);
        CartTermVo cartTermVo = new CartTermVo();
        cartTermVo.setId(itemId);
        cartTermVo.setImage(item.getImage());
        cartTermVo.setNum(1);
        cartTermVo.setPrice(item.getPrice());
        cartTermVo.setTitle(item.getTitle());
        cartTermVo.setSellPoint(item.getSellPoint());
        cartMap.put(itemId,cartTermVo);
    }

    /**
     * 把购车商品列表写入redis
     * @param userId
     * @param cart
     */
    private Boolean addCartToRedis(String userId,Long itemId,CartTermVo cart) {
        return cartFeign.insertCart(userId,itemId, cart);
    }

    /**
     * 从redis中查询购物车
     * @param userId
     */
    private CartTermVo getCartFromRedis(String userId,Long itemId) {
        CartTermVo cart = cartFeign.selectCartByUserId(userId,itemId);
        if(cart!=null){
            return cart;
        }
        return new CartTermVo();
    }
}