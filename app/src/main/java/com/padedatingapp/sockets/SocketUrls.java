package com.padedatingapp.sockets;


public class SocketUrls {

    public static final String socketConnection = "socket.connection";
    public static final String connectionFailure = "failedToConnect";
    public static final String CHAT_SERVER_URL = "http://15.207.74.128:5005/";
    public static final String NEW_MESSAGES="getMessage";
    public static final String SEND_MESSAGE="sendMessage";

    public static final String UPDATE_LOCATION="updateLocation";  //SP
    public static final String LAT_LONG_UPDATES="updateLocations"; //CU


    public static final String JOIN_ROOM="joinRoom";

//    static let joinRoom = "joinRoom"
//    static let getMessage = "getMessage"
//    static let sendMessage = "sendMessage"
//    static let sendOffer = "sendOffer"
//    static let getOffer = "getResponse_"
//    static let latLongUpdate = "updateLocation"
//    static let latLongUpdates = "updateLocations"


    /*static let latLongUpdate = "updateLocation" - SP
    static let latLongUpdates = "updateLocations" - CU

    let param : [String:Any] = ["receiver":dtaBookings?.sentBy?._id ?? "","bookingId":dtaBookings?._id ?? "","lat":currentLocation.latitude,"lng":currentLocation.longitude,"status" : dtaBookings?.status ?? ""]
*/
}
