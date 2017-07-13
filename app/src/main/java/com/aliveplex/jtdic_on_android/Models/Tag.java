package com.aliveplex.jtdic_on_android.Models;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

/**
 * Created by Aliveplex on 21/4/2560.
 */

public class Tag implements Comparable {
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

    private DateTime recentUse;

    public DateTime getRecentUse() {
        return recentUse;
    }

    public Tag(String tagName, String description) {
        this.tagName = tagName;
        this.description = description;
    }

    @Override
    public String toString() {
        return tagName.toString();
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return recentUse.compareTo(((Tag)o).getRecentUse());
    }

    public void setRecentUse(DateTime recentUse) {
        this.recentUse = recentUse;
    }
}
