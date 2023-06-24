package com.example.canvala.util;

public class Constants {

    public static final String IP_ADDRESS = "192.168.191.215";
    public static final String MAIN_END_POINT = "http://" + IP_ADDRESS + "/canvala/";
    public static final String SECOND_END_POINT = "http://" + IP_ADDRESS + "/ortosec/";
    public static final String URL_PRODUCT = SECOND_END_POINT + "/assets/images/";
    public static final String SHARED_PREF_NAME = "data_user";
    public static final String SHARED_PREF_USER_ID = "user_id";
    public static final String URL_DOWNLOAD_LAPORAN = MAIN_END_POINT + "admin/downloadLaporan/";
    public static final String SHARED_PREF_ROLE = "role";
    public static final String SHARED_PREF_LOGGED = "logged_in";
}
