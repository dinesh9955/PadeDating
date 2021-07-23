package com.padedatingapp.ui.call;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public class OpenTokConfig {
    /*
    Fill the following variables using your own Project info from the OpenTok dashboard
    https://dashboard.tokbox.com/projects

    Note that this application will ignore credentials in the `OpenTokConfig` file when `CHAT_SERVER_URL` contains a
    valid URL.
    */


//    public static final String API_KEY = "47249074";
//
//    public static final String SESSION_ID = "1_MX40NzI0OTA3NH5-MTYyNzAxNzcwNjcxNH5seGVMbzZleUdHb1VjMFJPK3JEc0puaTV-UH4";
//
//    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI0OTA3NCZzaWc9NmQ3ZmY0OWIzZWQxNjk3Zjc3MjI4YWYyODdjY2I3ZGI5ZDgxY2E3MTpzZXNzaW9uX2lkPTFfTVg0ME56STBPVEEzTkg1LU1UWXlOekF4Tnpjd05qY3hOSDVzZUdWTWJ6WmxlVWRIYjFWak1GSlBLM0pFYzBwdWFUVi1VSDQmY3JlYXRlX3RpbWU9MTYyNzAxNzcwNyZub25jZT0wLjU0MTIzOTA3MTE0MDg5ODMmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYyNzEwNDEwNyZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";


    public static final String API_KEY = "47249074";

    public static final String SESSION_ID = "1_MX40NzI0OTA3NH5-MTYyNzAxNzYwNjE5M35ueW9GWFhqZnFYWGlocVJmRXBKUzNmTnF-UH4";

    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI0OTA3NCZzaWc9ZWRmYjhiN2QzOTM5ZWFhODg4NmIxYTg0MTgzYjE4YjA4MzU5YTMxNTpzZXNzaW9uX2lkPTFfTVg0ME56STBPVEEzTkg1LU1UWXlOekF4TnpZd05qRTVNMzV1ZVc5R1dGaHFabkZZV0dsb2NWSm1SWEJLVXpObVRuRi1VSDQmY3JlYXRlX3RpbWU9MTYyNzAxNzYwNiZub25jZT0wLjIzOTIyODM2NzQ1NjIzODMyJnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE2MjcxMDQwMDYmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";





    public static boolean isValid() {
        if (TextUtils.isEmpty(OpenTokConfig.API_KEY)
                || TextUtils.isEmpty(OpenTokConfig.SESSION_ID)
                || TextUtils.isEmpty(OpenTokConfig.TOKEN)) {
            return false;
        }

        return true;
    }

    @NonNull
    public static String getDescription() {
        return "OpenTokConfig:" + "\n"
                + "API_KEY: " + OpenTokConfig.API_KEY + "\n"
                + "SESSION_ID: " + OpenTokConfig.SESSION_ID + "\n"
                + "TOKEN: " + OpenTokConfig.TOKEN + "\n";
    }
}
