package com.example.roman.test.data;

import com.google.gson.annotations.SerializedName;

public class Order {
    private static final String ORDER_ID = "OI";
    private static final String STATUS_ID = "SI";
    private static final String DATE_CREATED = "DC";
    private static final String TIME_CREATED = "DCT";
    private static final String INFO_STATUS_ID = "II";
    private static final String PHONE = "P";
    private static final String DESCRIPTION = "D";
    private static final String PRICE = "PR";
    private static final String OPTION = "OP";
    private static final String FROM = "AF";
    private static final String TO = "AT";
    private static final String CAN_TAKE = "CTO";
    private static final String SECTOR_FROM = "SN";
    private static final String TARIFF_ID = "TN";
    private static final String DATE = "DS";
    private static final String TIME = "DST";
    private static final String LENGTH = "DSTM";
    private static final String IS_PREVIOUS = "IPO";
    private static final String CAN_REFUSE = "IPOA";

    @SerializedName(ORDER_ID)
    private String orderId;

    @SerializedName(STATUS_ID)
    private int statusId;

    @SerializedName(DATE_CREATED)
    private String dateCreated;

    @SerializedName(TIME_CREATED)
    private String timeCreated;

    @SerializedName(INFO_STATUS_ID)
    private int infoStatusId;

    @SerializedName(PHONE)
    private String phone;

    @SerializedName(DESCRIPTION)
    private String description;

    @SerializedName(PRICE)
    private int price;

    @SerializedName(OPTION)
    private String option;

    @SerializedName(FROM)
    private String from;

    @SerializedName(TO)
    private String to;

    @SerializedName(CAN_TAKE)
    private boolean canTake;

    @SerializedName(SECTOR_FROM)
    private String sectorFrom;

    @SerializedName(TARIFF_ID)
    private String tariffId;

    @SerializedName(DATE)
    private String date;

    @SerializedName(TIME)
    private String time;

    @SerializedName(LENGTH)
    private double length;

    @SerializedName(IS_PREVIOUS)
    private boolean isPrevious;

    @SerializedName(CAN_REFUSE)
    private boolean canRefuse;

    Order() { }

    public int getPrice() {
        return price;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getOrderId() {
        return orderId;
    }
}
