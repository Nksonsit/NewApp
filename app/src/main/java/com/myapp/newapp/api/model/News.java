package com.myapp.newapp.api.model;

import java.io.Serializable;

/**
 * Created by ishan on 18-09-2017.
 */

public class News implements Serializable {
    private int Id;
    private String Title;
    private String Image;
    private String Description;
    private String Link;
    private String Url;
    private String TitleTag;
    private String MetaDes;
    private String AuthorId;
    private String Type;
    private String PublisherId;
    private String ShareTitle;
    private String Category;
    private String CreatedAt;
    private String UpdatedAt;
    private int IsPushNotification;

    public int getIsPushNotification() {
        return IsPushNotification;
    }

    public void setIsPushNotification(int isPushNotification) {
        IsPushNotification = isPushNotification;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTitleTag() {
        return TitleTag;
    }

    public void setTitleTag(String titleTag) {
        TitleTag = titleTag;
    }

    public String getMetaDes() {
        return MetaDes;
    }

    public void setMetaDes(String metaDes) {
        MetaDes = metaDes;
    }

    public String getAuthorId() {
        return AuthorId;
    }

    public void setAuthorId(String authorId) {
        AuthorId = authorId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPublisherId() {
        return PublisherId;
    }

    public void setPublisherId(String publisherId) {
        PublisherId = publisherId;
    }

    public String getShareTitle() {
        return ShareTitle;
    }

    public void setShareTitle(String shareTitle) {
        ShareTitle = shareTitle;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }
}
