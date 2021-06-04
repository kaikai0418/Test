package com.usian.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.fegin.ItemServiceFeign;
import org.springframework.web.bind.annotation.RequestMapping;
import com.usian.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/backend/item")
public class ItemController {
    @Autowired
    private ItemServiceFeign itemServiceFeign;

    /**
     * 查询商品基本信息
     * /
     */

    @HystrixCommand(fallbackMethod = "fallback",
            commandProperties = {
                    //错误百分比条件，达到熔断器最小请求数后错误率达到百分之多少后打开熔断器
                    @HystrixProperty(name =HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "5"),
                    //断容器最小请求数，达到这个值过后才开始计算是否打开熔断器
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "3"),
                    // 默认5秒; 熔断器打开后多少秒后 熔断状态变成半熔断状态(对该微服务进行一次请求尝试，不成功则状态改成熔断，成功则关闭熔断器
                    @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS, value = "5000")
            })
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId) {
        TbItem tbItem = itemServiceFeign.selectItemInfo(itemId);
        if (tbItem != null) {
            return Result.ok(tbItem);
        }
        return Result.error("查无结果");
    }


    // 返回托底数据的方法
    public Result fallback(Long itemId){
        return Result.ok("我是托底数据Fallback"+itemId);
    }
    /**
     * 查询商品并分页处理
     *
     * @return
     */
    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1")
                                                Integer page, @RequestParam(defaultValue = "2") Integer rows) {

        PageResult pageResult = itemServiceFeign.selectTbItemAllByPage(page, rows);
        if (pageResult.getResult() != null) {
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }


    @RequestMapping("/insertTbItem")
    public Result insertTbItem(TbItem tbItem,String desc,String itemParams){
        Integer insertTbItemNum = itemServiceFeign.insertTbItem(tbItem, desc,itemParams);
        if(insertTbItemNum==3){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    @RequestMapping("/updateTbItem")
    public Result updateTbItem(TbItem tbItem,String desc,String itemParams){
     itemServiceFeign.updateTbItem(tbItem, desc,itemParams);

        return Result.ok();
    }


    /*/backend/item/preUpdateItem*/
    @RequestMapping("/preUpdateItem")
    public Result preUpdateItem(Long itemId){
        Map<String,Object> map = itemServiceFeign.preUpdateItem(itemId);
        if(map.size()>0){
            return Result.ok(map);
        }
        return Result.error("查无结果");
    }

    /*/backend/item/preUpdateItem*/
    @RequestMapping("/deleteItemById")
    public Result deleteItemById(Long itemId){
       itemServiceFeign.deleteItemById(itemId);

      return Result.ok("删除成功");

    }

}