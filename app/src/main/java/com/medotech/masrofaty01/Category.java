package com.medotech.masrofaty01;

public class Category {
    private String Id, name, money, budget;
    private int icon;

    public Category(String Id, String name, int icon, String money, String budget) {
        this.Id = Id;
        this.name = name;
        this.icon = icon;
        this.money = money;
        this.budget = budget;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
