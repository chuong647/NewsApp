package com.example.news.enity;
//lop news
public class News {
    //tao ham voi
    private int id;
    private String name;
    private String link;
    public News() {
    }
    //Truy cap vao lop News
    public News(String name, String link, int id) {
        this.name = name;
        this.link = link;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
