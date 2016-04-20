
package com.intuit.schedulingapp;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Doc {

    private Title title;

    private Best_answer best_answer;

    public Title getTitle ()
    {
        return title;
    }

    public void setTitle (Title title)
    {
        this.title = title;
    }

    public Best_answer getBest_answer ()
    {
        return best_answer;
    }

    public void setBest_answer (Best_answer best_answer)
    {
        this.best_answer = best_answer;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [title = "+title+", best_answer = "+best_answer+"]";
    }
}
