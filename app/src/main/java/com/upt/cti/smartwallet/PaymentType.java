package com.upt.cti.smartwallet;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class PaymentType {
    public static int getColorFromPaymentType(String type) {
        type = type.toLowerCase();
        if (type.equals("entertainment"))
            return Color.rgb(200, 50, 50);
        else if (type.equals("food"))
            return Color.rgb(50, 150, 50);
        else if (type.equals("taxes"))
            return Color.rgb(20, 20, 150);
        else if (type.equals("travel"))
            return Color.rgb(230, 140, 0);
        else
            return Color.rgb(100, 100, 100);
    }

    public static List<String> getTypes() {
        List<String> list = new ArrayList<>();
        list.add("entertainment");
        list.add("food");
        list.add("taxes");
        list.add("travel");

        return list;
    }
}