package com.usian.controller;


import com.usian.pojo.CartTermVo;
import com.usian.pojo.OrderVo;
import com.usian.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @RequestMapping("/goSettlement")
    public List<CartTermVo> goSettlement(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId){
        return orderService.goSettlement(ids,userId);
    }
    @RequestMapping("/insertOrder")
    public String insertOrder(@RequestBody OrderVo orderVo){
        return  orderService.insertOrder(orderVo);
    }
}
