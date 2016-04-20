package com.intuit.schedulingapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cpingale on 4/20/16.
 */
public class Response {
    private Hits[] hits;

    public Hits[] getHits ()
    {
        return hits;
    }

    public void setHits (Hits[] hits)
    {
        this.hits = hits;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [hits = "+hits+"]";
    }
}
