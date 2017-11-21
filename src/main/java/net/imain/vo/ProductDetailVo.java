package net.imain.vo;

import java.math.BigDecimal;

/**
 * @author: uncle
 * @apdateTime: 2017-11-21 10:36
 */
public class ProductDetailVo {
    /** 商品id .*/
    private Integer id;

    /** 父类分类ID .*/
    private Integer parentCategoryId;

    /** 图片网址 .*/
    private String imageHost;

    /** 分类id .*/
    private Integer categoryId;

    /** 商品名称 .*/
    private String name;

    /** 商品负标题 .*/
    private String subtitle;

    /** 商品主图 .*/
    private String mainImage;

    /** 图片地址（JSON） .*/
    private String subImages;

    /** 商品详情 .*/
    private String detail;

    /** 商品价格：单位元，保留两位小数 .*/
    private BigDecimal price;

    /** 库存数量 .*/
    private Integer stock;

    /** 商品状态：1.在售  2.下架  3.删除 .*/
    private Integer status;

    /** 创建时间 .*/
    private String createTime;

    /** 更新时间 .*/
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
