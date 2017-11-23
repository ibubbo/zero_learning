package net.imain.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 分类
 *
 * @author uncle
 */
@Data
public class Category {

    /** 分类ID .*/
    private Integer id;

    /** 父类ID：0 根节点 .*/
    private Integer parentId;

    /** 分类名称 .*/
    private String name;

    /** 分类状态：1 正常  2 废弃 .*/
    private Boolean status;

    /** 排序编号，数值相等则自然排序 .*/
    private Integer sortOrder;

    /** 创建时间 .*/
    private Date createTime;

    /** 更新时间 .*/
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id != null ? id.equals(category.id) : category.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}