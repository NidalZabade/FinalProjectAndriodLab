package edu.birzeit.nidlibraheem.finalproject.models;

import java.util.Date;

public class Note {

    private long id;
    private String title;
    private String content;
    private Date creationDate;
    private boolean isFavorite;
    private User owner;
    private Tag tag;

    public Note() {
    }

    public Note(String title, String content, Date creationDate, boolean isFavorite, User owner, Tag tag) {
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
        this.owner = owner;
        this.tag = tag;
    }

    public Note (long id, String title, String content, Date creationDate, boolean isFavorite, User owner, Tag tag) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.isFavorite = isFavorite;
        this.owner = owner;
        this.tag = tag;
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


    public User getOwner() {
        return owner;
    }

    public Tag getTag() {
        return tag;
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

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }




}
