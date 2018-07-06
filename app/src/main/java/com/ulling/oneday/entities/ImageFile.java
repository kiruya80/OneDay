package com.ulling.oneday.entities;

import java.io.Serializable;

/**
 * 이미지 파일 정보
 */
public class ImageFile implements Serializable {

    private static final long serialVersionUID = 4274456314468671831L;

    private String imgUrl = "";
    private String fileName = "";
    private int height;
    private int width;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


}
