package com.example.splashapplication.Model;

public class Task {

    public String title;
    public String description;
    public String date;
    public String category;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String image;
    public int id;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Task(String title, String description, String date,int id,String image) {
        this.id=id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image = image;
    }

    public Task(String title, String description, String date,int id) {
        this.id=id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

//    public Task(String title, String description, String date) {
//        this.title = title;
//        this.description = description;
//        this.date = date;
//    }

    public Task(int id,String title, String description, String date, String category,String image) {
        this.category=category;
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}