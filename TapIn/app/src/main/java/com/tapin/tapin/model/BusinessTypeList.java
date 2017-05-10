package com.tapin.tapin.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 03/10/2017.
 */

public class BusinessTypeList implements Cloneable, Serializable {

    public String name;
   public ArrayList<BusinessType> listbuBusinessTypes=new ArrayList<>();

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
