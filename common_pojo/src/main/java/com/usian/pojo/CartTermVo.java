package com.usian.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CartTermVo implements Serializable {
    private Long id;
    private String title;
    private String image;
    private Integer num;
    private Long price;
    private String SellPoint;


    public String getSellPoint() {
        return SellPoint;
    }

    public void setSellPoint(String sellPoint) {
        SellPoint = sellPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
