package com.padedatingapp.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.padedatingapp.R
import com.padedatingapp.base.DataBindingActivity
import com.padedatingapp.databinding.ActivityPlayerBinding

class PlayerActivity : DataBindingActivity<ActivityPlayerBinding>() {

    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    override fun layoutId(): Int = R.layout.activity_player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initPlayerComponents() {
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
            .createMediaSource(intent!!.extras!!.getString("video_url").toString().toUri())


       // val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        simpleExoPlayer.addMediaSource(mediaSource)

        simpleExoPlayer.playWhenReady = true

        viewBinding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        viewBinding.playerView.player = simpleExoPlayer
        viewBinding.playerView.requestFocus()


        val defaultDataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "mediaPlayerSample")
        )


        simpleExoPlayer.prepare(mediaSource, false, false)
        simpleExoPlayer.playWhenReady = true

        viewBinding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        viewBinding.playerView.setPlayer(simpleExoPlayer)
        viewBinding.playerView.requestFocus()
        simpleExoPlayer.repeatMode = Player.REPEAT_MODE_OFF

        listeners()
    }




    private fun listeners() {
        simpleExoPlayer.addListener(object : Player.EventListener {
           override fun onTimelineChanged(
                timeline: Timeline,
                reason: Int
            ) {
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                       // progressBar.setVisibility(View.VISIBLE)
                        Log.d(
                            "PlayerActivity",
                            "onPlayerStateChanged - STATE_BUFFERING"
                        )
                       // showToast("onPlayerStateChanged - STATE_BUFFERING")
                    }
                    Player.STATE_READY -> {
                       // progressBar.setVisibility(View.INVISIBLE)
                        Log.d( "PlayerActivity", "onPlayerStateChanged - STATE_READY")
                       // showToast("onPlayerStateChanged - STATE_READY")
                    }
                    Player.STATE_IDLE -> {
                        Log.d( "PlayerActivity", "onPlayerStateChanged - STATE_IDLE")
                       // showToast("onPlayerStateChanged - STATE_IDLE")
                    }
                    Player.STATE_ENDED -> {
                        Log.d( "PlayerActivity", "onPlayerStateChanged - STATE_ENDED")
                       // showToast("onPlayerStateChanged - STATE_ENDED")
                    }
                }
            }

           override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {}
           override fun onIsPlayingChanged(isPlaying: Boolean) {}
            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onPlayerError(error: ExoPlaybackException) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
    }

    private fun releasePlayer() {
        simpleExoPlayer.release()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23)  initPlayerComponents()
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23)  initPlayerComponents()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onBackPressed() {
        finish()
    }

}