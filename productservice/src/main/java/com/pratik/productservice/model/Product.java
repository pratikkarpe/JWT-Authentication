package com.pratik.productservice.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("is_deleted = 0")
public class Product extends Base {
    private String ean;
    private String sku;
    private long amount;
    private String title;
    private String description;
    @Column(name = "image_path")
    private String imagePath;

    public Product() {
        super();
    }

    public Product( String ean, String sku, long price, String title, String description, String imagePath) {
        super();
        this.ean = ean;
        this.sku = sku;
        this.amount = price;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    public Product(String id, long price, String title, String description, String imagePath) {
        super();
        this.amount = price;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long price) {
        this.amount = price;
    }
}
