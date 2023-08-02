package com.example.qexamworld.Models;

public class CategoryModel {

    private String Id;
    private String name;
    private int noOfTests;

    public CategoryModel(String Id, String name, int noOfTests) {
        this.Id = Id;
        this.name = name;
        this.noOfTests = noOfTests;
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

    public int getNoOfTests() {
        return noOfTests;
    }

    public void setNoOfTests(int noOfTests) {
        this.noOfTests = noOfTests;
    }
}
