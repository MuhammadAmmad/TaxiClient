package com.example.roman.test;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Roman on 04/08/2016.
 */

public class Utility {
    public static final int DEFAULT = -1;

    public static final String LOGIN_INTENT = "login_intent";
    public static final String AIR_INTENT = "air_intent";

    public static final String METHOD = "M";
    public static final String ERROR = "E";
    public static final String REQUEST = "RQ";
    public static final String RESPONSE = "RS";
    public static final String DG = "DG";

    public static final int METHOD_GET_PERF = 1;
    public static final int METHOD_GET_CURRENT_STATE = 5;
    public static final int METHOD_GET_INFOORDER_BY_ID = 18;
    public static final int METHOD_LOGIN = 102;
    public static final int METHOD_GET_SETTINGS = 103;
    public static final int METHOD_GET_SECTORS  = 104;
    public static final int METHOD_GET_BALANCE = 105;
    public static final int METHOD_SET_ALERT = 106;
    public static final int METHOD_GER_CURRENT_SECTOR = 107;
    public static final int METHOD_SET_TO_SECTOR = 108;
    public static final int METHOD_LOGOUT = 113;
    public static final int METHOD_SET_NEW_STATUS = 114;
    public static final int METHOD_GET_NEW_STATUS = 115;
    public static final int METHOD_NEW_ORDERS = 116;
    public static final int METHOD_DELETE_ORDERS = 117;
    public static final int METHOD_GET_ORDERS  = 118;
    public static final int METHOD_GET_PREVORDERS = 119;
    public static final int METHOD_SET_ORDER = 120;
    public static final int METHOD_SET_STATUS = 121;
    public static final int METHOD_SET_LATE_DATA = 122;
    public static final int METHOD_CLOSE_ORDER = 123;
    public static final int METHOD_GET_ORDER_BY_ID = 124;
    public static final int METHOD_SEND_MESSAGE = 125;
    public static final int METHOD_NEW_MESSAGE = 126;
    public static final int METHOD_GET_MESSAGE = 127;
    public static final int METHOD_DENY_ORDER = 128;
    public static final int METHOD_SET_ORDER_NEW = 130;
    public static final int METHOD_GET_ORDER_COUNT = 131;
    public static final int METHOD_GET_EXEVORDERS = 134;

    public static final int ERROR_NONE = 0;
    public static final int ERROR_LOGIN_FAILED = 1;
    public static final int ERROR_DRIVER_BLOCKED = 2;
    public static final int ERROR_DOUBLE_USER = 3;
    public static final int ERROR_LOGIN_FAILED_TAKEN = 5;
    public static final int ERROR_FULL_QUEUE = 6;
    public static final int ERROR_SECTOR_FAILED = 7;
    public static final int ERROR_ORDER_NOT_FOUND = 10;
    public static final int ERROR_ORDER_TAKEN = 11;
    public static final int ERROR_LOW_BALANCE = 15;
    public static final int ERROR_SHIFT_CLOSED = 100;
    public static final int ERROR_USER_BLOCKED = 106;

    public static JSONObject getRQObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("RQ", new Object());
        json.put("M", 0);

        return json;
    }

    public static int getMethod(JSONObject object) throws JSONException {
        return object.getInt(METHOD);
    }

    public static int getError(JSONObject object) throws JSONException {
        return object.getInt(ERROR);
    }
}