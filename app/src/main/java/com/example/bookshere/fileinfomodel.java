package com.example.bookshere;

public class fileinfomodel {
    public String pdfName;
    public String Contact;
    public String Course;
    public String Course_Year;
    public String u_user;
    public String pdfURL;
    public String pdfId;
    public fileinfomodel() {

    }
    public fileinfomodel(String pdfName, String Contact,String Course,String Course_Year,String u_user,String pdfURL,String pdfId) {


        this.pdfName = pdfName;
        this.Contact=Contact;
        this.pdfURL= pdfURL;
        this.Course_Year= Course_Year;
        this.Course= Course;
        this.u_user= u_user;
        this.pdfId = pdfId ;

    }
    public String getPdfName() {
        return pdfName;
    }


    public String getPdfURL() {
        return pdfURL;
    }

    public String getU_user() {
        return u_user;
    }

    public String getPdfId() {
        return pdfId;
    }

}
