package com.padedatingapp.ui.call;


import android.Manifest;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import com.padedatingapp.R;
import com.padedatingapp.base.BaseActivity;
import com.padedatingapp.model.CallData;
import com.padedatingapp.model.call.CallUser;
import com.padedatingapp.sockets.AppSocketListener;
import com.padedatingapp.sockets.SocketUrls;
import com.padedatingapp.ui.call.network.APIService;
import com.padedatingapp.ui.call.network.GetSessionResponse;
import com.padedatingapp.ui.chats.ConnectivityReceiver;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class VideoCallActivity2 extends BaseActivity implements EasyPermissions.PermissionCallbacks , ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = VideoCallActivity.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST_CODE = 124;

    private Retrofit retrofit;
    private APIService apiService;

    private Session session;
    private Publisher publisher;
    private Subscriber subscriber;

    private FrameLayout publisherViewContainer;
    private FrameLayout subscriberViewContainer;

    CallData callUser;

    TextView textViewCall;
    ImageView imageViewAudio, imageViewVideo, publisher_containerView;

    ImageView imageViewCallPic, imageViewCallCancel, imageViewCameraSwitch, imageViewChat;

    LinearLayout linearLayoutAudio, linearLayoutVideo;

    ImageView imageViewUser;
    TextView textViewUser, textViewUser2;

    boolean booleanAudio = true;
    boolean booleanVideo = true;
    private Emitter.Listener onConnect = null;
    private Emitter.Listener callDiscount = null;
    private Socket socket = null;

    @Override
    public int layoutId() {
        return R.layout.call_view_activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main2);

        publisherViewContainer = findViewById(R.id.publisher_container);
        subscriberViewContainer = findViewById(R.id.subscriber_container);

        publisher_containerView = findViewById(R.id.publisher_containerView);

        imageViewCameraSwitch = findViewById(R.id.ivSwitchCamera);
        imageViewChat = findViewById(R.id.ivChat);


        linearLayoutAudio = findViewById(R.id.layout_call);
        linearLayoutVideo = findViewById(R.id.layout_call_2);

        imageViewUser = findViewById(R.id.ivPorfilePic);
        textViewUser = findViewById(R.id.tvName);
        textViewUser2 = findViewById(R.id.tvName2);


        imageViewAudio = findViewById(R.id.ivMinOnOff);
        textViewCall = findViewById(R.id.tvEndCall);
        imageViewVideo = findViewById(R.id.ivVideoOffOn);

        imageViewCallPic = findViewById(R.id.ivCallPic);
        imageViewCallCancel = findViewById(R.id.ivCallCancel);


        Bundle bundle = getIntent().getExtras();

        callUser = (CallData) bundle.getSerializable("key");

//        AppSocketListener.getInstance().emit(SocketUrls.CallDisconnect);
//        if (!socket.connected()) {
//            socket.connect();
//            socket.emit(SocketUrls.CallDisconnect);
//        }

        AppSocketListener.getInstance().restartSocket();

        initializeSockets();
      //  requestPermissions();

        if(callUser != null){
            if(callUser.getCallFrom().equalsIgnoreCase("notification")){
                textViewCall.setVisibility(View.GONE);

                imageViewCallPic.setVisibility(View.VISIBLE);
                imageViewCallCancel.setVisibility(View.VISIBLE);

                imageViewAudio.setVisibility(View.GONE);
                imageViewVideo.setVisibility(View.GONE);
            }else{
                requestPermissions();
            }

            imageViewCallPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewCall.setVisibility(View.VISIBLE);

                    imageViewCallPic.setVisibility(View.GONE);
                    imageViewCallCancel.setVisibility(View.GONE);

                    imageViewAudio.setVisibility(View.VISIBLE);
                    imageViewVideo.setVisibility(View.VISIBLE);

                  //  publisher_containerView.setVisibility(View.GONE);
                    publisherViewContainer.setVisibility(View.VISIBLE);
                    requestPermissions();
                }
            });
//===============================================================================================
            imageViewCallCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   socket.emit(SocketUrls.CallDisconnect, callUser.getSenderID());
//                    AppSocketListener.getInstance().emit(SocketUrls.CallDisconnect, callUser.getSenderID());
//                    JSONObject json = new JSONObject();
//                    try {
//                        json.put("partner", callUser.getSenderID());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("partner", callUser.getReceiverID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AppSocketListener.getInstance().emit(SocketUrls.CallDisconnect, json);
                   // onBackPressed();
                }
            });
//===============================================================================================
            imageViewCameraSwitch.setVisibility(View.GONE);
            imageViewCameraSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publisher.swapCamera();
                }
            });


            imageViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publisher.swapCamera();
                }
            });


            if(callUser.getCallType().equalsIgnoreCase("audio")){
                linearLayoutAudio.setVisibility(View.VISIBLE);
                linearLayoutVideo.setVisibility(View.GONE);
                imageViewVideo.setImageResource(R.drawable.ic_video_off);
            } else if(callUser.getCallType().equalsIgnoreCase("video")){
                linearLayoutAudio.setVisibility(View.GONE);
                linearLayoutVideo.setVisibility(View.VISIBLE);
                imageViewVideo.setImageResource(R.drawable.ic_video_on);
                RequestOptions options = new RequestOptions();
                Glide.with(VideoCallActivity2.this).load(callUser.getUser2Image())
                        .apply(options).into(publisher_containerView);


                if(callUser.getCallFrom().equalsIgnoreCase("notification")){
                 //   publisher_containerView.setVisibility(View.VISIBLE);
                    publisherViewContainer.setVisibility(View.GONE);
                }else{
                 //   publisher_containerView.setVisibility(View.GONE);
                    publisherViewContainer.setVisibility(View.VISIBLE);
                }

            }

            textViewUser.setText(""+callUser.getUser2FirstName()+" "+callUser.getUser2LastName());
            textViewUser2.setText(""+callUser.getUser2FirstName()+" "+callUser.getUser2LastName());

            // options.placeholder(R.drawable.user_circle_1179465)
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            options.placeholder(R.drawable.user_circle_1179465);

            Glide.with(VideoCallActivity2.this).load(callUser.getUser2Image())
                    .apply(options).into(imageViewUser);

        }


        Log.e(TAG ,"callUser "+callUser.toString());



//===============================================================================================
        textViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                socket.emit(SocketUrls.CallDisconnect, callUser.getSenderID());
//                if (session != null) {
//                    session.disconnect();
//                }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("partner", callUser.getReceiverID());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                AppSocketListener.getInstance().emit(SocketUrls.CallDisconnect, json);
//                onBackPressed();
            }
        });
//===============================================================================================

        imageViewAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewAudio.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_mic_on).getConstantState()))
                {
                    imageViewAudio.setImageResource(R.drawable.group_111726);
//                    linearLayoutAudio.setVisibility(View.VISIBLE);
//                    linearLayoutVideo.setVisibility(View.GONE);
                    booleanAudio = false;
                }else{
                    imageViewAudio.setImageResource(R.drawable.ic_mic_on);
//                    linearLayoutAudio.setVisibility(View.GONE);
//                    linearLayoutVideo.setVisibility(View.VISIBLE);
                    booleanAudio = true;
                }

                publisher.setPublishAudio(booleanAudio);
             //   subscriber.setSubscribeToAudio(booleanAudio);
            }
        });

        imageViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageViewVideo.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_video_on).getConstantState()))
                {
                    imageViewVideo.setImageResource(R.drawable.ic_video_off);
//                    linearLayoutAudio.setVisibility(View.VISIBLE);
//                    linearLayoutVideo.setVisibility(View.GONE);
                    booleanVideo = false;
                }else{
                    imageViewVideo.setImageResource(R.drawable.ic_video_on);
//                    linearLayoutAudio.setVisibility(View.GONE);
//                    linearLayoutVideo.setVisibility(View.VISIBLE);
                    booleanVideo = true;
                }
               // session.setSessionListener(sessionListener);
                //requestPermissions();

                publisher.setPublishVideo(booleanVideo);
                //subscriber.setSubscribeToVideo(booleanVideo);
            }

        });




    }



    @Override
    protected void onResume() {
        super.onResume();

        if (session != null) {
            session.onResume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (session != null) {
            session.onPause();
        }
    }






    private Session.SessionListener sessionListener = new Session.SessionListener() {
        @Override
        public void onConnected(Session session) {
            Log.e(TAG, "onConnected: Connected to session: " + session.getSessionId());

            publisher = new Publisher.Builder(VideoCallActivity2.this).build();
            publisher.setPublisherListener(publisherListener);

            publisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);

           // publisher.setPublishAudio(booleanAudio);
//            publisher.setPublishVideo(booleanVideo);

//            publisher.publishAudio(false);
//            publisher.subscribeToAudio(booleanVideo);

            publisherViewContainer.addView(publisher.getView());

            if (publisher.getView() instanceof GLSurfaceView) {
                ((GLSurfaceView) publisher.getView()).setZOrderOnTop(true);
            }

//            publisher = new Publisher.Builder(VideoCallActivity2.this)
//                    .name("Bob")
//                    .audioTrack(true)
//                    .frameRate(Publisher.CameraCaptureFrameRate.FPS_7)
//                    .resolution(Publisher.CameraCaptureResolution.LOW)
//                    .videoTrack(false)
////                    .capturer(mCapturer)
////                    .renderer(mRenderer)
//                    .build();



            session.publish(publisher);

        }

        @Override
        public void onDisconnected(Session session) {
            Log.e(TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
        }

        @Override
        public void onStreamReceived(Session session, Stream stream) {
            Log.e(TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());

            if (subscriber == null) {
                subscriber = new Subscriber.Builder(VideoCallActivity2.this, stream).build();
                subscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
                subscriber.setSubscriberListener(subscriberListener);

                subscriber.setSubscribeToAudio(subscriber.getSubscribeToAudio());
                subscriber.setSubscribeToVideo(subscriber.getSubscribeToVideo());

//                if(subscriber.getSubscribeToVideo() == true){
//                    subscriber.setSubscribeToVideo(true);
//                }else{
//                    subscriber.setSubscribeToVideo(false);
//                }

//                if(subscriber.getSubscribeToAudio() == true){
//                    subscriber.setSubscribeToAudio(true);
//                }else{
//                    subscriber.setSubscribeToAudio(false);
//                }

                session.subscribe(subscriber);
                subscriberViewContainer.addView(subscriber.getView());
            }
        }

        @Override
        public void onStreamDropped(Session session, Stream stream) {
            Log.e(TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());
            onBackPressed();
        }

        @Override
        public void onError(Session session, OpentokError opentokError) {
            finishWithMessage("Session error: " + opentokError.getMessage());
        }
    };




    private PublisherKit.PublisherListener publisherListener = new PublisherKit.PublisherListener() {
        @Override
        public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
            Log.e(TAG, "onStreamCreated: Publisher Stream Created. Own stream " + stream.getStreamId());
            publisher.setPublishAudio(booleanAudio);
//            publisher.setPublishVideo(booleanVideo);
//            subscriber.getSession();
//            Log.e(TAG, "subscriber.getSession():  " + subscriber.getSession());
        }

        @Override
        public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
            Log.e(TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream " + stream.getStreamId());
        }

        @Override
        public void onError(PublisherKit publisherKit, OpentokError opentokError) {
            finishWithMessage("PublisherKit onError: " + opentokError.getMessage());
        }
    };


    SubscriberKit.SubscriberListener subscriberListener = new SubscriberKit.SubscriberListener() {
        @Override
        public void onConnected(SubscriberKit subscriberKit) {
            Log.e(TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
//            subscriber.setSubscribeToAudio(booleanAudio);
//            subscriber.setSubscribeToVideo(booleanVideo);
        }

        @Override
        public void onDisconnected(SubscriberKit subscriberKit) {
            Log.e(TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
        }

        @Override
        public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
            finishWithMessage("SubscriberKit onError: " + opentokError.getMessage());
        }
    };





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e(TAG, "onPermissionsGranted:" + requestCode + ": " + perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finishWithMessage("onPermissionsDenied: " + requestCode + ": " + perms);
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_CODE)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(this, perms)) {

            if (ServerConfig.hasChatServerUrl()) {
                // Custom server URL exists - retrieve session config
                if(!ServerConfig.isValid()) {
                    finishWithMessage("Invalid chat server url: " + ServerConfig.CHAT_SERVER_URL);
                    return;
                }

                initRetrofit();
                getSession();
            } else {
                // Use hardcoded session config
                if(!OpenTokConfig.isValid()) {
                    finishWithMessage("Invalid OpenTokConfig. " + OpenTokConfig.getDescription());
                    return;
                }

//                initializeSession(OpenTokConfig.API_KEY, OpenTokConfig.SESSION_ID, OpenTokConfig.TOKEN);
                if(callUser != null){
                    Log.e(TAG, "callUser.getData().getApikey() "+callUser.getApikey());
                    Log.e(TAG, "callUser.getData().getSessionId() "+callUser.getSessionId());
                    Log.e(TAG, "callUser.getData().getToken() "+callUser.getToken());

                    initializeSession(callUser.getApikey(), callUser.getSessionId(), callUser.getToken());
                }

            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_video_app), PERMISSIONS_REQUEST_CODE, perms);
        }
    }

    /* Make a request for session data */
    private void getSession() {
        Log.i(TAG, "getSession");

        Call<GetSessionResponse> call = apiService.getSession();

        call.enqueue(new Callback<GetSessionResponse>() {
            @Override
            public void onResponse(Call<GetSessionResponse> call, Response<GetSessionResponse> response) {
                GetSessionResponse body = response.body();
                Log.e(TAG, "bodyAA "+body.toString());

                initializeSession(body.apiKey, body.sessionId, body.token);
            }

            @Override
            public void onFailure(Call<GetSessionResponse> call, Throwable t) {
                throw new RuntimeException(t.getMessage());
            }
        });
    }

    private void initializeSession(String apiKey, String sessionId, String token) {
        Log.i(TAG, "apiKey: " + apiKey);
        Log.i(TAG, "sessionId: " + sessionId);
        Log.i(TAG, "token: " + token);

        /*
        The context used depends on the specific use case, but usually, it is desired for the session to
        live outside of the Activity e.g: live between activities. For a production applications,
        it's convenient to use Application context instead of Activity context.
         */
        session = new Session.Builder(this, apiKey, sessionId).build();
        session.setSessionListener(sessionListener);
        session.connect(token);
    }


    private void initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ServerConfig.CHAT_SERVER_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(APIService.class);
    }

    private void finishWithMessage(String message) {
        Log.e(TAG, message);
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        this.finish();
    }


    @Override
    public void onBackPressed() {
        onCallForDestroy();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        onCallForDestroy();
        super.onDestroy();
    }




//    protected void onCallForDestroy() {
//        if (subscriber != null) {
//            subscriberViewContainer.removeAllViews();
//            session.unsubscribe(subscriber);
//            subscriber.destroy();
//        }
//
//        if (publisher != null) {
//            publisherViewContainer.removeView(publisher.getView());
//            session.unpublish(publisher);
//            publisher.destroy();
//        }
//
//        if(session != null){
//            session.disconnect();
//        }
//
//    }


    private void onCallForDestroy() {
        if (session == null) {
            return;
        }

        if (subscriber != null) {
            subscriberViewContainer.removeView(subscriber.getView());
            session.unsubscribe(subscriber);
            subscriber = null;
        }

        if (publisher != null) {
            publisherViewContainer.removeView(publisher.getView());
            session.unpublish(publisher);
            publisher = null;
        }
        session.disconnect();

    }



    private void initializeSockets() {
        Log.e(TAG, "initializeSockets");



//        onConnect = new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//               // socket.emit(SocketUrls.CallDisconnect);
//                socket.emit(SocketUrls.CallDisconnect);
////                String data = (String) args[0];
////                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
//            }
//        };

        callDiscount = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
             //   Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                Log.e(TAG, "callDisconnectAAA "+data);
                if(data.toString().contains("disconnectd")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    });
                }
            }
        };


        AppSocketListener.getInstance().addOnHandler(SocketUrls.CallDisconnect, callDiscount);
//        AppSocketListener.getInstance().emit(SocketUrls.CallDisconnect);
       // AppSocketListener.getInstance().addOnHandler(SocketUrls.CallDisconnect, callDiscount);

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (!socket.connected()) {
                socket.connect();

                Log.e("Socket", " socket ${socket.connected()}");
            }
        } else {
            Log.e("Socket", " socket ${socket.notconnected()}");

        }
    }



}
