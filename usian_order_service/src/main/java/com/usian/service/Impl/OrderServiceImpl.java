package com.usian.service.Impl;

import com.usian.mapper.TbOrderItemMapper;
import com.usian.mapper.TbOrderMapper;
import com.usian.mapper.TbOrderShippingMapper;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import com.usian.service.OrderService;
import com.usian.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private   RedisClient redisClient;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Override
    public List<CartTermVo> goSettlement(Long[] ids, Long userId) {
        List<CartTermVo> cartTerms = redisClient.hgetAll("CART" + userId);
        ArrayList<CartTermVo> list = new ArrayList<>();
        for (Long id : ids) {
            for (CartTermVo cartTerm : cartTerms) {
                if(id.equals(cartTerm.getId())){
                    list.add(cartTerm);
                    break;
                }
            }
        }
        return list;
    }

    @Override
    @Transactional
    public String insertOrder(OrderVo orderVo) {
        TbOrder order = orderVo.getOrder();
        Date now = new Date();
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);
        order.setStatus(1);
        order.setCreateTime(now);
        tbOrderMapper.insertSelective(order);

        String orderItems = orderVo.getOrderItem();
        List<TbOrderItem> orderItemList = JsonUtils.jsonToList(orderItems, TbOrderItem.class);
        orderItemList.forEach(orderItem ->{
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setOrderId(orderId);
            tbOrderItemMapper.insertSelective(orderItem);
        });

        TbOrderShipping orderShipping = orderVo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(now);
        tbOrderShippingMapper.insertSelective(orderShipping);
        return orderId;
    }
}
