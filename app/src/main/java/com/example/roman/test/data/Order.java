package com.example.roman.test.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

@SimpleSQLTable(
        table = "orders",
        provider = "OrderProvider")
public class Order implements Parcelable {
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
    @SimpleSQLColumn("order_id")
    private String orderId;

    @SerializedName(STATUS_ID)
    @SimpleSQLColumn("status_id")
    private int statusId;

    @SerializedName(DATE_CREATED)
    @SimpleSQLColumn("date_created")
    private String dateCreated;

    @SerializedName(TIME_CREATED)
    @SimpleSQLColumn("time_created")
    private String timeCreated;

    @SerializedName(INFO_STATUS_ID)
    @SimpleSQLColumn("info_status_id")
    private int infoStatusId;

    @SerializedName(PHONE)
    @SimpleSQLColumn("phone")
    private String phone;

    @SerializedName(DESCRIPTION)
    @SimpleSQLColumn("description")
    private String description;

    @SerializedName(PRICE)
    @SimpleSQLColumn("price")
    private String price;

    @SerializedName(OPTION)
    @SimpleSQLColumn("option")
    private String option;

    @SimpleSQLColumn("from")
    @SerializedName(FROM)
    private String from;

    @SerializedName(TO)
    @SimpleSQLColumn("to")
    private String to;

    @SerializedName(CAN_TAKE)
    @SimpleSQLColumn("can_take")
    private boolean canTake;

    @SerializedName(SECTOR_FROM)
    @SimpleSQLColumn("sector_from")
    private String sectorFrom;

    @SerializedName(TARIFF_ID)
    @SimpleSQLColumn("tariff_id")
    private String tariffId;

    @SerializedName(DATE)
    @SimpleSQLColumn("date")
    private String date;

    @SerializedName(TIME)
    @SimpleSQLColumn("time")
    private String time;

    @SerializedName(LENGTH)
    @SimpleSQLColumn("length")
    private String length;

    @SerializedName(IS_PREVIOUS)
    @SimpleSQLColumn("is_previous")
    private boolean isPrevious;

    @SerializedName(CAN_REFUSE)
    @SimpleSQLColumn("can_refurse")
    private boolean canRefuse;

    public Order() { }

    public String getOrderId() {
        return orderId;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public int getInfoStatusId() {
        return infoStatusId;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getOption() {
        return option;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean getCanTake() {
        return canTake;
    }

    public String getSectorFrom() {
        return sectorFrom;
    }

    public String getTariffId() {
        return tariffId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLength() {
        return length;
    }

    public boolean getIsPrevious() {
        return isPrevious;
    }

    public boolean getCanRefuse() {
        return canRefuse;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setInfoStatusId(int infoStatusId) {
        this.infoStatusId = infoStatusId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setCanTake(boolean canTake) {
        this.canTake = canTake;
    }

    public void setSectorFrom(String sectorFrom) {
        this.sectorFrom = sectorFrom;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setIsPrevious(boolean previous) {
        isPrevious = previous;
    }

    public void setCanRefuse(boolean canRefuse) {
        this.canRefuse = canRefuse;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeInt(statusId);
        parcel.writeString(dateCreated);
        parcel.writeString(timeCreated);
        parcel.writeInt(infoStatusId);
        parcel.writeString(phone);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(option);
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeBooleanArray(new boolean[]{canTake});
        parcel.writeString(sectorFrom);
        parcel.writeString(tariffId);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(length);
        parcel.writeBooleanArray(new boolean[]{isPrevious});
        parcel.writeBooleanArray(new boolean[]{canRefuse});

    }
}
