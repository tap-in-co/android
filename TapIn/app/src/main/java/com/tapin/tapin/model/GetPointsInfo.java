package com.tapin.tapin.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 03/10/2017.
 */

public class GetPointsInfo implements Cloneable, Serializable {

    @SerializedName("total_earned_points")
    public int total_earned_points;

    @SerializedName("total_redeemed_points")
    public int total_redeemed_points;

    @SerializedName("total_available_points")
    public int total_available_points;


    @SerializedName("points")
    public List<PointInfo> listPointInfos;

    @SerializedName("current_points_level")
    public CurrentProfileLevel current_points_level;

    @SerializedName("next_points_level")
    public CurrentProfileLevel next_points_level;

    class CurrentProfileLevel implements Serializable {
        @SerializedName("points")
        public String points;

        @SerializedName("dollar_value")
        public String dollar_value;

        @SerializedName("points_level_name")
        public String points_level_name;

        @SerializedName("message")
        public String message;

    }

}
