package com.sgx.jizan.model;

/**
 * Created by sgx on 2017/11/22.
 */

public class IndexModel {

    private Integer id;

    private String imgUrl;

    private String title;

    private String groupCount;

    private String lookCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public String getLookCount() {
        return lookCount;
    }

    public void setLookCount(String lookCount) {
        this.lookCount = lookCount;
    }
}
