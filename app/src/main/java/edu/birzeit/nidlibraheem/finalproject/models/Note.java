package edu.birzeit.nidlibraheem.finalproject.models;

import java.util.Date;

public class Note {

    private long id;
    private String title;
    private String content;
    private Date creationDate;
    private boolean isFavorite;
    private long owner_id;
    private String tags;

    public Note() {
    }

    public Note(String title, String content, Date creationDate, boolean isFavorite, long owner_id, String tags) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
        this.owner_id = owner_id;
        this.tags = tags;
    }

    public Note (long id, String title, String content, Date creationDate, boolean isFavorite, int owner_id, String tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
        this.owner_id = owner_id;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }


    public long getOwnerId() {
        return owner_id;
    }

    public String getTags() {
        return tags;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setOwnerId(long owner_id) {
        this.owner_id = owner_id;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }




}
