
package com.intuit.schedulingapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Title {

    private String text;
    private String clean;
    private List<String> segmented = new ArrayList<String>();
    private List<String> classes = new ArrayList<String>();
    private List<String> segmentedNer = new ArrayList<String>();

    /**
     * 
     * @return
     *     The text
     */
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 
     * @return
     *     The clean
     */
    public String getClean() {
        return clean;
    }

    /**
     * 
     * @param clean
     *     The clean
     */
    public void setClean(String clean) {
        this.clean = clean;
    }

    /**
     * 
     * @return
     *     The segmented
     */
    public List<String> getSegmented() {
        return segmented;
    }

    /**
     * 
     * @param segmented
     *     The segmented
     */
    public void setSegmented(List<String> segmented) {
        this.segmented = segmented;
    }

    /**
     * 
     * @return
     *     The classes
     */
    public List<String> getClasses() {
        return classes;
    }

    /**
     * 
     * @param classes
     *     The classes
     */
    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    /**
     * 
     * @return
     *     The segmentedNer
     */
    public List<String> getSegmentedNer() {
        return segmentedNer;
    }

    /**
     * 
     * @param segmentedNer
     *     The segmented_ner
     */
    public void setSegmentedNer(List<String> segmentedNer) {
        this.segmentedNer = segmentedNer;
    }



}
