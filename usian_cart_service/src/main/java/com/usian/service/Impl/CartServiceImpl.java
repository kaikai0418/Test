package com.usian.service.Impl;

import com.usian.mapper.TbItemMapper;
import com.usian.pojo.CartTermVo;
import com.usian.pojo.TbItem;
import com.usian.redis.RedisClient;
import com.usian.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbItemMapper tbItemMapper;

    //加入购物车
    @Override
    public void addItem(Long userId, Long itemId) {

        Boolean exists = redisClient.exists("CART" + userId);
        if(exists){
            //判断该商品是否在购物车
            CartTermVo cartTermVo =(CartTermVo) redisClient.hget("CART" + userId, itemId + "");
            if(cartTermVo==null){
                redisClient.hset("CART"+userId,itemId+"",getCartTerm(itemId));
            }else{
                cartTermVo.setNum(cartTermVo.getNum()+1);
                redisClient.hset("CART"+userId,itemId+"",cartTermVo);
            }

        }else{
            redisClient.hset("CART"+userId,itemId+"",getCartTerm(itemId));
        }
    }

    @Override
    public List<CartTermVo> getCart(Long userId) {
        List list = redisClient.hgetAll("CART"+userId);
        return list;
    }

    //根据商品id 获取购物车项
    public CartTermVo getCartTerm(Long itemId){
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        CartTermVo cartTermVo = new CartTermVo();
        cartTermVo.setId(itemId);
        cartTermVo.setImage(item.getImage());
        cartTermVo.setNum(1);
        cartTermVo.setPrice(item.getPrice());
        cartTermVo.setTitle(item.getTitle());
        cartTermVo.setSellPoint(item.getSellPoint());

        return  cartTermVo;
    }

    /**
     * 根据用户 ID 查询用户购物车
     */
    @Override
    public CartTermVo selectCartByUserId(String userId,Long itemId) {
        CartTermVo map =   (CartTermVo) redisClient.hget("CART" + userId, itemId + "");
        return map;
    }

    /**
     * 缓存购物车
     *
     * @param cart
     */
    @Override
    public Boolean insertCart(String userId,Long itemId,CartTermVo cart) {
        return redisClient.hset("CART"+userId, itemId+"", cart);
    }

    @Override
    public void del(Long itemId,String userId) {
        redisClient.hdel("CART"+userId,itemId+"");
    }
}
