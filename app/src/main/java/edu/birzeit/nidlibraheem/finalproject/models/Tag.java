package edu.birzeit.nidlibraheem.finalproject.models;

public class Tag {

private long id;
    private String name;
    private User owner;

    public Tag() {
    }

    public Tag(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public Tag(long id, String name, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }



}
