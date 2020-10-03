package com.example.myhealthyagenda.article;

public class Article {
    private String textBrief;
    private String textHeadLine;
    private String htmlImg;
    private String link;
    public Article(String textBrief, String textHeadLine,String htmlImg,String link) {
        this.textBrief = textBrief;
        this.textHeadLine = textHeadLine;
        this.htmlImg = htmlImg;
        this.link = link;
    }

    public String getTextBrief() {
        return textBrief;
    }

    public void setTextBrief(String textBrief) {
        this.textBrief = textBrief;
    }

    public String getTextHeadLine() {
        return textHeadLine;
    }

    public void setTextHeadLine(String textHeadLine) {
        this.textHeadLine = textHeadLine;
    }

    public String getHtmlImg(){return this.htmlImg;}
    public String getLink(){return this.link;}

}
