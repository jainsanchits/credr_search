package com.sjain.snapdealsearchbar.model;

/**
 * Created by sjain on 18/10/17.
 */

public class ProductDescription {

    String title;
    String imageLink;
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ProductDescription() {
    }

    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;

    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageSrc(String imageLink) {
        this.imageLink = imageLink;
    }
}
