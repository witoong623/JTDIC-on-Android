package com.aliveplex.jtdic_on_android.Models;

/**
 * Created by Aliveplex on 21/4/2560.
 */

public class Tag {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public String getDescription() {
        return description;
    }

    private String description;

    public Tag(String tagName, String description) {
        this.tagName = tagName;
        this.description = description;
    }

    @Override
    public String toString() {
        return tagName.toString();
    }
}
