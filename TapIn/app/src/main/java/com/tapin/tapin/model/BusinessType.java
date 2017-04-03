package com.tapin.tapin.model;

import java.io.Serializable;

/**
 * Created by User on 03/10/2017.
 */

public class BusinessType implements Cloneable, Serializable {
    public String display_name;
    public String request_Service;
    public String icon;


    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
