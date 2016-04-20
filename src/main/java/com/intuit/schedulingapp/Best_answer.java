
package com.intuit.schedulingapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Best_answer {

    private String text;

    private String clean;

    private String[] segmented;

    private String[] segmented_ner;

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getClean ()
    {
        return clean;
    }

    public void setClean (String clean)
    {
        this.clean = clean;
    }

    public String[] getSegmented ()
    {
        return segmented;
    }

    public void setSegmented (String[] segmented)
    {
        this.segmented = segmented;
    }

    public String[] getSegmented_ner ()
    {
        return segmented_ner;
    }

    public void setSegmented_ner (String[] segmented_ner)
    {
        this.segmented_ner = segmented_ner;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [text = "+text+", clean = "+clean+", segmented = "+segmented+", segmented_ner = "+segmented_ner+"]";
    }
}
