package com.example.roman.test.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Record extends SugarRecord {
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

    @Unique
    @Expose
    private String recordId;

    @Expose
    @SerializedName(STATUS_ID)
    private int statusId;

    @Expose
    @SerializedName(DATE_CREATED)
    private String dateCreated;

    @Expose
    @SerializedName(TIME_CREATED)
    private String timeCreated;

    @Expose
    @SerializedName(INFO_STATUS_ID)
    private int infoStatusId;

    @Expose
    @SerializedName(PHONE)
    private String phone;

    @Expose
    @SerializedName(DESCRIPTION)
    private String description;

    @Expose
    @SerializedName(PRICE)
    private String price;

    @Expose
    @SerializedName(OPTION)
    private String option;

    @Expose
    @SerializedName(FROM)
    private String fromAddress;

    @Expose
    @SerializedName(TO)
    private String toAddress;

    @Expose
    @SerializedName(CAN_TAKE)
    private boolean canTake;

    @Expose
    @SerializedName(SECTOR_FROM)
    private String sectorFrom;

    @Expose
    @SerializedName(TARIFF_ID)
    private String tariffId;

    @Expose
    @SerializedName(DATE)
    private String date;

    @Expose
    @SerializedName(TIME)
    private String time;

    @Expose
    @SerializedName(LENGTH)
    private String length;

    @Expose
    @SerializedName(IS_PREVIOUS)
    private boolean isPrevious;

    @Expose
    @SerializedName(CAN_REFUSE)
    private boolean canRefuse;

    public Record() {}

    public String getRecordId() {
        return recordId;
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

    public String getFromAddress() {
        return fromAddress;
    }

    public String getToAddress() {
        return toAddress;
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

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
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
}
