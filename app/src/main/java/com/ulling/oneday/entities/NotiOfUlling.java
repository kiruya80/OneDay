package com.ulling.oneday.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 알림
 *
 */
public class NotiOfUlling implements Serializable {


    private static final long serialVersionUID = -67097694787070113L;
    private String id = "";
    // 타이틀
    private String title = "";
    // 내용
    private String content = "";
    // 이미지 파일 리스트
    private ArrayList<ImageFile> imageFiles;
    // 이미지 url
//    private String imgUrl = "";
    // 클릭시 링크 앱링크 / url링크
    private String deepLink = "";
    // 날짜
    private long date;

    private boolean finish = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeepLink() {
        return deepLink;
    }

    public void setDeepLink(String deepLink) {
        this.deepLink = deepLink;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public ArrayList<ImageFile> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(ArrayList<ImageFile> imageFiles) {
        this.imageFiles = imageFiles;
    }

    @Override
    public String toString() {
        return "NotiOfUlling{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageFiles=" + imageFiles +
                ", deepLink='" + deepLink + '\'' +
                ", date=" + date +
                ", finish=" + finish +
                '}';
    }
}
