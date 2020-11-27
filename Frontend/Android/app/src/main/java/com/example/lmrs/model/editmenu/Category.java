package com.example.lmrs.model.editmenu;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for MenuCategory
 */
public class Category {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("menu_items")
    @Expose
    private List<MenuItem> menuItems = null;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}
