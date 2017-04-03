package com.tapin.tapin.utils;


import com.tapin.tapin.model.BusinessType;
import com.tapin.tapin.model.BusinessTypeList;

import java.util.ArrayList;

public class Constant {

    public static int TIMEOUT = 60 * 1000;
    public static int PAGE_LIMIT = 20;
    public static String SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static ArrayList<BusinessTypeList> listBusinessTypeLists = new ArrayList<>();

    static {

        BusinessTypeList businessTypeList1 = new BusinessTypeList();
        businessTypeList1.name = "one service only";
        BusinessType businessType1 = new BusinessType();
        businessType1.display_name = "";
        businessType1.icon = "";
        businessTypeList1.listbuBusinessTypes.add(businessType1);
        listBusinessTypeLists.add(businessTypeList1);

        BusinessTypeList businessTypeList2 = new BusinessTypeList();
        businessTypeList2.name = "collection";
        BusinessType businessType2 = new BusinessType();
        businessType2.display_name = "";
        businessType2.icon = "";
        businessTypeList2.listbuBusinessTypes.add(businessType2);
        listBusinessTypeLists.add(businessTypeList2);


        BusinessTypeList businessTypeList3 = new BusinessTypeList();
        businessTypeList3.name = "service";
        BusinessType businessType3 = new BusinessType();
        businessType3.display_name = "";
        businessType3.icon = "";
        businessTypeList3.listbuBusinessTypes.add(businessType3);
        listBusinessTypeLists.add(businessTypeList3);

        BusinessTypeList businessTypeList4 = new BusinessTypeList();
        businessTypeList4.name = "boutique";
        BusinessType businessType4 = new BusinessType();
        businessType4.display_name = "";
        businessType4.icon = "";
        businessTypeList4.listbuBusinessTypes.add(businessType4);
        listBusinessTypeLists.add(businessTypeList4);


        BusinessTypeList businessTypeList5 = new BusinessTypeList();
        businessTypeList5.name = "store";
        BusinessType businessType5 = new BusinessType();
        businessType5.display_name = "";
        businessType5.icon = "";
        businessTypeList5.listbuBusinessTypes.add(businessType5);
        listBusinessTypeLists.add(businessTypeList5);

        BusinessTypeList businessTypeList6 = new BusinessTypeList();
        businessTypeList6.name = "bistro";
        BusinessType businessType6 = new BusinessType();
        businessType6.display_name = "Order Food";
        businessType6.icon = getIcon(businessType6.display_name);
        businessTypeList6.listbuBusinessTypes.add(businessType6);
        BusinessType businessType66 = new BusinessType();
        businessType66.display_name = "Request Service";
        businessType66.icon = getIcon(businessType66.display_name);
        businessTypeList6.listbuBusinessTypes.add(businessType66);
        BusinessType businessType666 = new BusinessType();
        businessType666.display_name = "Events Calendar";
        businessType666.icon = getIcon(businessType6.display_name);
        businessTypeList6.listbuBusinessTypes.add(businessType666);
        listBusinessTypeLists.add(businessTypeList6);


        BusinessTypeList businessTypeList7 = new BusinessTypeList();
        businessTypeList7.name = "growler station";
        BusinessType businessType7 = new BusinessType();
        businessType7.display_name = "";
        businessType7.icon = "";
        businessTypeList7.listbuBusinessTypes.add(businessType7);
        listBusinessTypeLists.add(businessTypeList7);

        BusinessTypeList businessTypeList8 = new BusinessTypeList();
        businessTypeList8.name = "mobile food";
        BusinessType businessType8 = new BusinessType();
        businessType8.display_name = "Order Food";
        businessType8.icon = getIcon(businessType8.display_name);
        businessTypeList8.listbuBusinessTypes.add(businessType8);
        BusinessType businessType88 = new BusinessType();
        businessType88.display_name = "Text";
        businessType88.icon = getIcon(businessType88.display_name);
        businessTypeList8.listbuBusinessTypes.add(businessType88);
        BusinessType businessType888 = new BusinessType();
        businessType888.display_name = "Catering";
        businessType888.icon = getIcon(businessType888.display_name);
        businessTypeList8.listbuBusinessTypes.add(businessType888);
        listBusinessTypeLists.add(businessTypeList8);


    }

    private static String getIcon(String str) {

        if (str.equalsIgnoreCase("Order Food"))
            return "ic_order_food";
        if (str.equalsIgnoreCase("waitingToServe.png"))
            return "ic_waiting_to_serve";
        if (str.equalsIgnoreCase("Text"))
            return "ic_waiting_to_serve";
        if (str.equalsIgnoreCase("Catering"))
            return "ic_catering";
        if (str.equalsIgnoreCase("Request Service"))
            return "ic_request_service";
        if (str.equalsIgnoreCase("Events Calendar"))
            return "ic_events_calender";


        return str;
    }

    public static ArrayList<BusinessType> getBusinessList(String b_name)
    {
        for (int i = 0; i < listBusinessTypeLists.size(); i++) {

            if(listBusinessTypeLists.get(i).name.equalsIgnoreCase(b_name))
            {
                return listBusinessTypeLists.get(i).listbuBusinessTypes;
            }
        }
        return new ArrayList<>();
    }

}
