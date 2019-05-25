package com.medotech.masrofaty01;

public class Item {
    private String id;
    private String name;
    private String notes;
    private String price;
    private String categoryId;
    private String categoryName;
    private String createDate;

    public Item(String id, String name, String notes, String price,
                String categoryId, String categoryName, String createDate) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        if (notes.equals("null"))
            return "";
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
