package com.tapin.tapin.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Narendra on 6/7/17.
 */

public class OrderSummaryInfo implements Serializable, Cloneable {

    public String promotion_code;

    public double total;

    public String business_id;

    public String pd_time;

    public String cmd;

    public String points_dollar_amount;

    public double tax_amount;

    public String cc_last_4_digits;

    public double subtotal;

    public double tip_amount;

    public String pd_mode;

    public String note;

    public String consumer_id;

    public double promotion_discount_amount;

    public String pd_locations_id = "";

    public String pd_charge_amount = "";

    public int points_redeemed;

    public double delivery_charge_amount;

    public ArrayList<OrderedInfo> listOrdered;

    public String counterPickupTime;

    public String tableNumber;

    public String consumerDeliveryId = "";

    public String deliveryLocationID;
    public String deliveryLocation;
    public String locationDeliveryTime;

    public String parkingPickupTime;

    public String averageWaitTime;

}
