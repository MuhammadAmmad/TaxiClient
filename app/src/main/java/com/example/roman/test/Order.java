package com.example.roman.test;

import org.json.JSONException;
import org.json.JSONObject;

public class Order {
    public static final String ORDER_ID = "OI";
    public static final String STATUS_ID = "SI";
    public static final String DATE_CREATED = "DC";
    public static final String TIME_CREATED = "DCT";
    public static final String INFO_STATUS_ID = "II";
    public static final String PHONE = "P";
    public static final String DESCRIPTION = "D";
    public static final String PRICE = "PR";
    public static final String OPTION = "OP";
    public static final String FROM = "AF";
    public static final String TO = "AT";
    public static final String CAN_TAKE = "CTO";
    public static final String SECTOR_FROM = "SN";
    public static final String TARIFF_ID = "TN";
    public static final String DATE = "DS";
    public static final String TIME = "DST";
    public static final String LENGTH = "DSTM";
    public static final String IS_PREVIOUS = "IPO";
    public static final String CAN_REFUSE = "IPOA";

    public final double id;
    public final int statusId;
    public final String dateCreated;
    public final String timeCreated;
    public final int infoStatusId;
    public final String phone;
    public final String description;
    public final int price;
    public final String option;
    public final String from;
    public final String to;
    public final boolean canTake;
    public final String sectorFrom;
    public final String tariffId;
    public final String date;
    public final String time;
    public final double length;
    public final boolean isPrevious;
    public final boolean canRefuse;

    public Order(JSONObject order) throws JSONException {
        id = order.getDouble(ORDER_ID);
        statusId = order.getInt(STATUS_ID);
        dateCreated = order.getString(DATE_CREATED);
        timeCreated = order.getString(TIME_CREATED);
        infoStatusId = order.getInt(INFO_STATUS_ID);
        phone = order.getString(PHONE);
        description = order.getString(DESCRIPTION);
        price = order.getInt(PRICE);
        option = order.getString(OPTION);
        from = order.getString(FROM);
        to = order.getString(TO);
        canTake = order.getBoolean(CAN_TAKE);
        sectorFrom = order.getString(SECTOR_FROM);
        tariffId = order.getString(TARIFF_ID);
        date = order.getString(DATE);
        time = order.getString(TIME);
        length = order.getDouble(LENGTH);
        isPrevious = order.getBoolean(IS_PREVIOUS);
        canRefuse = order.getBoolean(CAN_REFUSE);
    }

//    public Order(String from, String to, int price, int id) {
//        this.from = from;
//        this.to = to;
//        this.price = price;
//        orderId = id;
//    }
}
