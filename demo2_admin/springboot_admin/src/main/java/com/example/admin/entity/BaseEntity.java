package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

/**
 * 文件名：BaseEntity.java
 * 描述：entity 层的公共类，包含公共字段 "id", "created", "updated", "statu"
 * 时间：2021-6-27
 * 作者：TechRice
 */
public class BaseEntity {
    /* 对象属性 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;                  // ID
    private LocalDateTime created;    // 创建时间
    private LocalDateTime updated;    // 更新时间
    private Integer statu;            // 状态

    /* 对象方法 */
    // getter 和 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    // 重写 toString() 方法

    @Override
    public String toString() {
        return "id=" + id +
               ", created=" + created +
               ", updated=" + updated +
               ", statu=" + statu;
    }
}
