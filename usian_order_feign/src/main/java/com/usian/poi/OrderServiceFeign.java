package com.usian.poi;


import com.usian.pojo.CartTermVo;
import com.usian.pojo.OrderVo;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-order-service")
public interface OrderServiceFeign {



    @RequestMapping("/order/goSettlement")
    public List<CartTermVo> goSettlement(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId);



    @RequestMapping("/order/insertOrder")
    public String insertOrder(@RequestBody OrderVo orderVo);
}
