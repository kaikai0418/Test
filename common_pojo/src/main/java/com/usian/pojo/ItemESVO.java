package com.usian.pojo;


import java.io.Serializable;

/**
 * @Title: ItemESVO
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/19 14:32
 */


public class ItemESVO implements Serializable {


    private Long id;  // {itemid:1,itemTile:"xxx",itemSellPint:"xxx"}
    private String itemTitle;
    private String itemSellPoint;
    private String itemPrice;
    private String itemImage;
    private String itemCategoryName;
    private String itemDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemSellPoint() {
        return itemSellPoint;
    }

    public void setItemSellPoint(String itemSellPoint) {
        this.itemSellPoint = itemSellPoint;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
}
