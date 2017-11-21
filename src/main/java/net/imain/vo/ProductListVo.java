package net.imain.vo;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;

/**
 * @author: uncle
 * @apdateTime: 2017-11-21 14:57
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ProductListVo {
    /** 商品id .*/
    private Integer id;

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

    /** 商品价格：单位元，保留两位小数 .*/
    private BigDecimal price;

    /** 商品状态：1.在售  2.下架  3.删除 .*/
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
