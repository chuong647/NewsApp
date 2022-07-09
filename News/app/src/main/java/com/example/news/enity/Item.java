package com.example.news.enity;
// lop Item
public class Item {
    //khai bao
    private String title, link, date, linkImg;

    public Item() {
    }
    //truy cap vao Item
    public Item(String title, String link, String sumary) {
        this.title = title;
        this.link = link;
        this.date = sumary;
    }
    //truy cap vao Item
    public Item(String title, String link, String date, String linkImg) {
        this.title = title;
        this.link = link;
        this.date = date;
        this.linkImg = linkImg;
    }
    //tra ve trang chi tiet can hien thi
    public String getLinkImg() {
        return linkImg;
    }
    // tim kiem phuong thuc setLinkImg tren trang chi tiết cần hiển thị
    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }
    // tra ve trang thai cua trang
    public String getTitle() {
        return title;
    }
    //tim kiem phuong thuc trang thai tren trang chi tiet
    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
