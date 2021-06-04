package com.usian.pojo;

public class OrderVo {
    private String orderItem;
    private TbOrder order;
    private TbOrderShipping orderShipping;

    public OrderVo() {
    }

    public OrderVo(String orderItem, TbOrder order, TbOrderShipping orderShipping) {
        this.orderItem = orderItem;
        this.order = order;
        this.orderShipping = orderShipping;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
