package com.example.bookshere;

public class ImageUploadInfo {
    public String imageName;

    public String imageURL;
    public String buk_year;
    public String buk_price;
    public String c_name;
    public String author;
    public String contact;
    public String c_year;
    public String u_user;
    public String keys ;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String url,String buk_year,String buk_price,String c_name,String c_year,String u_user,String keys,String author,String contact) {

        System.out.println(url+"this is image url");
        this.imageName = name;
        this.imageURL= url;
        this.buk_year= buk_year;
        this.buk_price= buk_price;
        this.c_name= c_name;
        this.c_year= c_year;
        this.u_user= u_user;
        this.keys = keys;
        this.author=author;
        this.contact=contact;


    }

    public String getImageName() {
        return imageName;
    }


    public String getImageURL() {
        return imageURL;
    }
    public String getC_name() { return c_name;}
}

