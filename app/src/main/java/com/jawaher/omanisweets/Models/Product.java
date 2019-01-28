package com.jawaher.omanisweets.Models;

public class Product {

    // attributes
    private String name;
    private String price;
    private String image;
    private String category;
    private String isDeliveryAvailable;

    // constructors
    public Product() {
        // empty constructor
    }

    public Product(String name, String price, String image, String category,String isDeliveryAvailable) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
        this.isDeliveryAvailable = isDeliveryAvailable;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsDeliveryAvailable() {
        return isDeliveryAvailable;
    }

    public void setIsDeliveryAvailable(String isDeliveryAvailable) {
        this.isDeliveryAvailable = isDeliveryAvailable;
    }
}
