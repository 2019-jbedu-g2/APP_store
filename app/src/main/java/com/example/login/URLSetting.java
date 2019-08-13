package com.example.login;

public class URLSetting {
    private static String url = "http://192.168.0.20:8000/";
    private static String wsUrl = "ws://192.168.0.20:8000/";
    public URLSetting(){

    }
    public static String getURL(){         //HTTP통신 주소 반환
        return url;
    }
    public static String getWsURL(){       // WebSocket 통신을 위한 주소 반환.
        return wsUrl;
    }
}