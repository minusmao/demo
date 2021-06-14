package com.example.fruit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author TechRice
 * @since 2021-06-12
 */
public class Fruit implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String name;

    private Integer sale;

    private String iconUrl;

    
    public Integer getId() {
        return id;
    }

      public void setId(Integer id) {
          this.id = id;
      }
    
    public String getName() {
        return name;
    }

      public void setName(String name) {
          this.name = name;
      }
    
    public Integer getSale() {
        return sale;
    }

      public void setSale(Integer sale) {
          this.sale = sale;
      }
    
    public String getIconUrl() {
        return iconUrl;
    }

      public void setIconUrl(String iconUrl) {
          this.iconUrl = iconUrl;
      }

    @Override
    public String toString() {
        return "Fruit{" +
              "id=" + id +
                  ", name=" + name +
                  ", sale=" + sale +
                  ", iconUrl=" + iconUrl +
              "}";
    }
}
