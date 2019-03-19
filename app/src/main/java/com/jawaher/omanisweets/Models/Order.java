package com.jawaher.omanisweets.Models;

public class Order {

    // attributes
    private String customer_id;
    private String customer_name;
    private String customer_phone;
    private String sweet_name;
    private String sweet_price;
    private String sweet_category;
    private String sweet_count;
    private String order_date;
    private String order_time;

    // constructors
    public Order() {

    }

    public Order(String customer_id, String customer_name, String customer_phone, String sweet_name, String sweet_price, String sweet_category, String sweet_count, String order_date, String order_time) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.sweet_name = sweet_name;
        this.sweet_price = sweet_price;
        this.sweet_category = sweet_category;
        this.sweet_count = sweet_count;
        this.order_date = order_date;
        this.order_time = order_time;
    }

    // getters and setters

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getSweet_name() {
        return sweet_name;
    }

    public void setSweet_name(String sweet_name) {
        this.sweet_name = sweet_name;
    }

    public String getSweet_price() {
        return sweet_price;
    }

    public void setSweet_price(String sweet_price) {
        this.sweet_price = sweet_price;
    }

    public String getSweet_category() {
        return sweet_category;
    }

    public void setSweet_category(String sweet_category) {
        this.sweet_category = sweet_category;
    }

    public String getSweet_count() {
        return sweet_count;
    }

    public void setSweet_count(String sweet_count) {
        this.sweet_count = sweet_count;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    @Override
    public String toString() {
        return getSweet_name() + " - " + getSweet_price() + " OMR - " + getOrder_date();
    }
}
