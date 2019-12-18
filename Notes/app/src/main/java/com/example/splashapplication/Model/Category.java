package com.example.splashapplication.Model;

public class Category {
    int id;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    int flag;
    String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Category(int id,String category,int flag) {
        this.id = id;
        this.category = category;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return category;
    }
}
