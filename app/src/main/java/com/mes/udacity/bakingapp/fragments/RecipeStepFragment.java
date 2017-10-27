package com.mes.udacity.bakingapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.listeners.OnStepChangeListener;
import com.mes.udacity.bakingapp.models.RecipeStep;
import com.squareup.picasso.Picasso;


/**
 * Created by Mohamed on 10/23/2017.
 */

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private Button nextButton;
    private Button prevButton;
    private TextView shortDescriptionTextView;
    private TextView descriptionTextView;
    private ImageView stepThumbnailImageView;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private RecipeStep step;
    private int position;
    private int stepsCount;
    private long playerPosition;

    private OnStepChangeListener mListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepChangeListener) {
            mListener = (OnStepChangeListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_step_detail_fragment, container, false);
        Gson gson = new Gson();
        String stepStr;
        Bundle args = getArguments();
        stepStr = args.getString(getString(R.string.detail_intent_key));
        position = args.getInt(getString(R.string.position_intent_key));
        stepsCount = args.getInt(getString(R.string.count_intent_key));
        step = gson.fromJson(stepStr, RecipeStep.class);

        nextButton = view.findViewById(R.id.next_button);
        prevButton = view.findViewById(R.id.prev_button);
        if(nextButton != null && prevButton != null) {
            initializeNextButton();
            initializePreviousButton();
        }
        shortDescriptionTextView = view.findViewById(R.id.step_short_description);
        descriptionTextView = view.findViewById(R.id.step_description);
        if (shortDescriptionTextView != null) {
            shortDescriptionTextView.setText(step.getShortDescription());
        }
        if (descriptionTextView != null) {
            descriptionTextView.setText(step.getDescription());
        }
        stepThumbnailImageView = view.findViewById(R.id.step_image_view);
        if(stepThumbnailImageView != null && step.getThumbnailURL() != null
                && !step.getThumbnailURL().isEmpty()) {
            stepThumbnailImageView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(step.getThumbnailURL())
                    .into(stepThumbnailImageView);
        }
        mPlayerView = view.findViewById(R.id.playerView);
        String videoUrl = step.getVideoURL();
        if (mExoPlayer == null) {
            initializeMediaSession();
        }
        if (videoUrl != null && !videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl));

        }
        else {
            mPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.no_video));
        }
        return view;
    }

    private void initializeNextButton() {
        if (position == stepsCount) {
            nextButton.setEnabled(false);
            nextButton.setVisibility(View.INVISIBLE);
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changeStepTo(position + 1);
            }
        });
    }
    private void initializePreviousButton() {
        if (position == 1) {
            prevButton.setEnabled(false);
            prevButton.setVisibility(View.INVISIBLE);
        }
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.changeStepTo(position - 1);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }




    /**
     * ExoPlayer settings
     */
    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(playerPosition);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            playerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
