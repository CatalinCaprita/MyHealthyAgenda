package com.example.myhealthyagenda.menu;

public class MenuItem {
    private String title;
    int drawableRes;

    public MenuItem(String title, int drawableRes) {
        this.title = title;
        this.drawableRes = drawableRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }
}
