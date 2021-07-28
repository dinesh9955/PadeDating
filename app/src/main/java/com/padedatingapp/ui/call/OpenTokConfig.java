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
//    public static final String SESSION_ID = "2_MX40NzI0OTA3NH5-MTYyNzQ3NTMzNDc0OH5ZTFBNQmhOOFRkVU5OTU5oSnozVWZyQVp-UH4";
//
//    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI0OTA3NCZzaWc9NTZiYzBhOTc4NDkyODdhMWE2MTI0ZmVmMTQ4NzcwOWY4OWNiYmQ4ZDpzZXNzaW9uX2lkPTJfTVg0ME56STBPVEEzTkg1LU1UWXlOelEzTlRNek5EYzBPSDVaVEZCTlFtaE9PRlJrVlU1T1RVNW9Tbm96VldaeVFWcC1VSDQmY3JlYXRlX3RpbWU9MTYyNzQ3NTMzNSZub25jZT0wLjEyMTMwNTMzNTU3NDk0OTM4JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE2Mjc1NjE3MzUmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";


    public static final String API_KEY = "47249074";

    public static final String SESSION_ID = "1_MX40NzI0OTA3NH5-MTYyNzQ3NTYxMjMzMn5DMVRocHVlVDZBTzRQbWpaL0thK2ZsczV-UH4";

    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI0OTA3NCZzaWc9YmI5YjU1ZGZiOGU3MmJmMDMxYzdhMWU5MGQzZDg5ZThlODA0ZDFjMDpzZXNzaW9uX2lkPTFfTVg0ME56STBPVEEzTkg1LU1UWXlOelEzTlRZeE1qTXpNbjVETVZSb2NIVmxWRFpCVHpSUWJXcGFMMHRoSzJac2N6Vi1VSDQmY3JlYXRlX3RpbWU9MTYyNzQ3NTYxMiZub25jZT0wLjYwMTMzMDI2NzEwMzUxNDgmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYyNzU2MjAxMiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";





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
