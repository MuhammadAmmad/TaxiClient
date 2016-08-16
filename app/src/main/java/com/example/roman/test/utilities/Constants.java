package com.example.roman.test.utilities;

public class Constants {
    static final int TEN_SECS = 1000 * 10;

    public static int id;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final int DEFAULT = -1;

    public static final String LOGIN_INTENT = "login_intent";
    public static final String AIR_INTENT = "air_intent";
    public static final String MAIN_INTENT = "main_intent";
    public static final String SECTORS_INTENT = "sectors_intent";

    public static final String METHOD = "M";
    public static final String ERROR = "E";
    public static final String REQUEST = "RQ";
    public static final String RESPONSE = "RS";
    public static final String DG = "DG";

    public static final int METHOD_GET_PREF = 1;
    public static final int METHOD_GET_CURRENT_STATE = 5;
    public static final int METHOD_GET_INFO_ORDER_BY_ID = 18;
    public static final int METHOD_LOGIN = 102;
    public static final int METHOD_GET_SETTINGS = 103;
    public static final int METHOD_GET_SECTORS  = 104;
    public static final int METHOD_GET_BALANCE = 105;
    public static final int METHOD_SET_ALERT = 106;
    public static final int METHOD_GET_CURRENT_SECTOR = 107;
    public static final int METHOD_SET_TO_SECTOR = 108;
    public static final int METHOD_LOGOUT = 113;
    public static final int METHOD_SET_NEW_STATUS = 114;
    public static final int METHOD_GET_NEW_STATUS = 115;
    public static final int METHOD_NEW_ORDER = 116;
    public static final int METHOD_DELETE_ORDER = 117;
    public static final int METHOD_GET_ORDERS  = 118;
    public static final int METHOD_GET_PREV_ORDERS = 119;
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
    public static final int METHOD_GET_EXEC_ORDERS = 134;

    public static final int ERROR_NONE = 0;
    public static final int ERROR_LOGIN_INCORRECT = 1;
    public static final int ERROR_LOGIN_BLOCKED = 2;
    public static final int ERROR_LOGIN_OCCUPIED= 3;
    public static final int ERROR_LOGIN_RADIO = 4;
    public static final int ERROR_LOGIN_TAKEN = 5;
    public static final int ERROR_FULL_QUEUE = 6;
    public static final int ERROR_SECTOR_FAILED = 7;
    public static final int ERROR_ORDER_NOT_FOUND = 10;
    public static final int ERROR_ORDER_TAKEN = 11;
    public static final int ERROR_LOW_BALANCE = 15;
    public static final int ERROR_SHIFT_CLOSED = 100;
    public static final int ERROR_USER_BLOCKED = 106;

    public static final String THEME = "theme";
    public static final String NIGHT = "night";
    public static final String DAY = "day";

    public static final String SECTORS = "SC";

    public static final String LOGIN = "L";
    public static final String PASSWORD = "P";

    public static final String CURRENT_SECTOR = "PI";
    public static final String NEW_SECTOR = "OI";

    public static final String REASON_ARRAY = "RJRS";
    public static final String STATUS_ARRAY = "DSMS";
    public static final String STATUS_ID = "SI";

}