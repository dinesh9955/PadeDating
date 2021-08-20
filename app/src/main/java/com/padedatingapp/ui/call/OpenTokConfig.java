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



    public static final String API_KEY = "47282844";
    public static final String SESSION_ID = "2_MX40NzI4Mjg0NH5-MTYyOTM3ODE3MjA5MX5KZS9hRjdLL1JRODBYdjg5RmJ4R0VtaTZ-fg";
    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI4Mjg0NCZzaWc9NmQzYTc1ZjViNjZiNGMzZGE4YjA4ZjFkZmUxYmFlYWE5ZTk3NDQxYTpzZXNzaW9uX2lkPTJfTVg0ME56STRNamcwTkg1LU1UWXlPVE0zT0RFM01qQTVNWDVLWlM5aFJqZExMMUpST0RCWWRqZzVSbUo0UjBWdGFUWi1mZyZjcmVhdGVfdGltZT0xNjI5Mzc4MTcyJm5vbmNlPTAuNTQwNDQ2NzIwMDY5MDQ4OCZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNjI5Mzc5OTcyJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";


//    public static final String API_KEY = "47282844";
//    public static final String SESSION_ID = "2_MX40NzI4Mjg0NH5-MTYyOTM3NzgwMDU5MH5SZURDMUMzdE9FekZWRzhDN2U2ZHhadXZ-fg";
//    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI4Mjg0NCZzaWc9MWMxNDFhYzQ3NTIxNDUxNDdkMmY5N2FlNThiOGZlOTIxYjdjOTkyMDpzZXNzaW9uX2lkPTJfTVg0ME56STRNamcwTkg1LU1UWXlPVE0zTnpnd01EVTVNSDVTWlVSRE1VTXpkRTlGZWtaV1J6aEROMlUyWkhoYWRYWi1mZyZjcmVhdGVfdGltZT0xNjI5Mzc3ODAxJm5vbmNlPTAuNDQ2OTY5OTQzNzQ1MDM4OTYmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYyOTM3OTYwMSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";





//    public static final String API_KEY = "47282844";
//    public static final String SESSION_ID = "2_MX40NzI4Mjg0NH5-MTYyNzU1NTMyNDM5Nn50WGZ2UnIwblk5bnVNZlE0bGpPM0NBRjF-fg";
//    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI4Mjg0NCZzaWc9YThkYWZlZWQ1MDg1ZTNjYjAxM2EwZWUwYjE2ZWQ3YzI5OTNmMTRiYjpzZXNzaW9uX2lkPTJfTVg0ME56STRNamcwTkg1LU1UWXlOelUxTlRNeU5ETTVObjUwV0daMlVuSXdibGs1Ym5WTlpsRTBiR3BQTTBOQlJqRi1mZyZjcmVhdGVfdGltZT0xNjI3NTU1MzI0Jm5vbmNlPTAuMjQ2NTY5ODI0Njc1ODI3NDQmcm9sZT1tb2RlcmF0b3ImZXhwaXJlX3RpbWU9MTYyNzU1NzEyNCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";



//    public static final String API_KEY = "47249074";
//
//    public static final String SESSION_ID = "1_MX40NzI0OTA3NH5-MTYyNzQ3NTYxMjMzMn5DMVRocHVlVDZBTzRQbWpaL0thK2ZsczV-UH4";
//
//    public static final String TOKEN = "T1==cGFydG5lcl9pZD00NzI0OTA3NCZzaWc9YmI5YjU1ZGZiOGU3MmJmMDMxYzdhMWU5MGQzZDg5ZThlODA0ZDFjMDpzZXNzaW9uX2lkPTFfTVg0ME56STBPVEEzTkg1LU1UWXlOelEzTlRZeE1qTXpNbjVETVZSb2NIVmxWRFpCVHpSUWJXcGFMMHRoSzJac2N6Vi1VSDQmY3JlYXRlX3RpbWU9MTYyNzQ3NTYxMiZub25jZT0wLjYwMTMzMDI2NzEwMzUxNDgmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYyNzU2MjAxMiZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";






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
