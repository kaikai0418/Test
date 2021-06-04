package com.usian.controller;

import com.usian.poi.OrderServiceFeign;
import com.usian.pojo.CartTermVo;
import com.usian.pojo.OrderVo;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/order")
public class OrderController {


    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @RequestMapping("/goSettlement")
    public Result goSettlement(@RequestParam("ids") Long[] ids,@RequestParam("userId") Long userId) {
      List<CartTermVo> list=  orderServiceFeign.goSettlement(ids,userId);
     return Result.ok(list);
    }

    @RequestMapping("/insertOrder")
    public Result insertOrder(String orderItem, TbOrderShipping orderShipping, TbOrder order){
        String orderId= orderServiceFeign.insertOrder(new OrderVo(orderItem, order, orderShipping));
        return Result.ok(orderId);
    }
}
