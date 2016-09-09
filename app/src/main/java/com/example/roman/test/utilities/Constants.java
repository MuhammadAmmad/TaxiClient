package com.example.roman.test.utilities;

public class Constants {
    public static final String NEW_FORMAT = "hh:mm";
    public static final String OLD_FORMAT = "hh:mm:ss";

    static final int TEN_SECS = 1000 * 10;

    public static int id;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final int DEFAULT = -1;

    public static final String ORDER_STATUS_NEW = "1";
    public static final String ORDER_STATUS_TAKE = "2";
    public static final String ORDER_STATUS_NEED_CONF = "3";
    public static final String ORDER_STATUS_GONE = "4";
    public static final String ORDER_STATUS_WAITING = "5";
    public static final String ORDER_STATUS_INFORMED = "6";
    public static final String ORDER_STATUS_DRIVING = "7";
    public static final String ORDER_STATUS_DONE = "8";
    public static final String ORDER_STATUS_CANCELED = "9";
    public static final String ORDER_STATUS_REJECTED = "10";

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
    public static final int METHOD_SET_ORDER_STATUS = 121;
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
    public static final int ERROR_TARIFF_NOT_FOUND = 8;
    public static final int ERROR_CANT_CHANGE_STATUS = 9;
    public static final int ERROR_ORDER_NOT_FOUND = 10;
    public static final int ERROR_ORDER_TAKEN = 11;
    public static final int ERROR_NOT_CONFIRMED = 12;
    public static final int ERROR_DRIVER_REMOVED = 13;
    public static final int ERROR_ORDER_CLOSED = 14;
    public static final int ERROR_LOW_BALANCE = 15;
    public static final int ERROR_NO_TIME = 16;
    public static final int ERROR_OTHER_ORDER = 17;
    public static final int ERROR_CARD_NOT_FOUND = 18;
    public static final int ERROR_CARD_BLACK_LIST = 19;
    public static final int ERROR_ORDER_CANT_CREATE = 20;
    public static final int ERROR_MULTIPLE_ORDERS = 21;
    public static final int ERROR_NOT_ON_MY_WAY = 22;
    public static final int ERROR_FIRST_IN_QUEUE = 23;
    public static final int ERROR_OPERATOR_DECLINED = 24;
    public static final int ERROR_MAX_REST = 25;
    public static final int ERROR_DRIVER_NOT_FOUND = 26;

    public static final int ERROR_SHIFT_CLOSED = 100;
    public static final int ERROR_DRIVER_BLOCKED = 106;

    public static final String THEME = "theme";
    public static final String NIGHT = "Night";
    public static final String DAY = "Day";

    public static final String SECTORS = "SC";
    public static final String NEW_ORDER_MASK = "NOF";
    public static final String SECTOR_HASH = "SCH";

    public static final String LOGIN = "L";
    public static final String PASSWORD = "P";

    public static final String CURRENT_SECTOR = "PI";
    public static final String NEW_SECTOR = "OI";

    public static final String REASON_ARRAY = "RJRS";
    public static final String STATUS_ARRAY = "DSMS";
    public static final String STATUS_ID = "SI";
    public static final String ORDER_ID = "OI";
    public static final String WAITING_TIME = "TT";

    public static final String QUEUE_POSITION = "CQI";
    public static final String SECTOR_ID = "CSI";
    public static final String DRIVERS_ARRAY = "SCT";

    public static final int SHOW_PHONE_NUMBER = 1;
    public static final int SHOW_DATE_START = 1 << 1;
    public static final int SHOW_TIME_START = 1 << 2;
    public static final int SHOW_DATE_CREATED = 1 << 3;
    public static final int SHOW_TIME_CREATED = 1 << 4;
    public static final int SHOW_ADDRESS_START = 1 << 6;
    public static final int SHOW_ADDRESS_END = 1 << 7;
    public static final int SHOW_PRICE = 1 << 8;
    public static final int SHOW_TARIFF = 1 << 9;
    public static final int SHOW_OPTIONS = 1 << 10;
    public static final int SHOW_CAR_TYPE = 1 << 11;
    public static final int SHOW_DESCRIPTION = 1 << 12;
    public static final int SHOW_ROUTE_LENGTH = 1 << 13;

    public static final String TITLE = "TITLE";
    public static final String MESSAGE = "MESSAGE";
}