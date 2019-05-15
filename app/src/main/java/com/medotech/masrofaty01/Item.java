package com.medotech.masrofaty01;

public class Item {
    private String id, name, notes, price, incomeCategoryId, outComeCategoryId, incomeCategoryName,
            outcomeCategoryName, createDate;

    public Item(String id, String name, String notes, String price,
                String incomeCategoryId, String outComeCategoryId, String incomeCategoryName, String outcomeCategoryName, String createDate) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.incomeCategoryId = incomeCategoryId;
        this.outComeCategoryId = outComeCategoryId;
        this.incomeCategoryName = incomeCategoryName;
        this.outcomeCategoryName = outcomeCategoryName;
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

    public String getIncomeCategoryId() {
        return incomeCategoryId;
    }

    public void setIncomeCategoryId(String incomeCategoryId) {
        this.incomeCategoryId = incomeCategoryId;
    }

    public String getOutComeCategoryId() {
        return outComeCategoryId;
    }

    public void setOutComeCategoryId(String outComeCategoryId) {
        this.outComeCategoryId = outComeCategoryId;
    }

    public String getIncomeCategoryName() {
        return incomeCategoryName;
    }

    public void setIncomeCategoryName(String incomeCategoryName) {
        this.incomeCategoryName = incomeCategoryName;
    }

    public String getOutcomeCategoryName() {
        return outcomeCategoryName;
    }

    public void setOutcomeCategoryName(String outcomeCategoryName) {
        this.outcomeCategoryName = outcomeCategoryName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
