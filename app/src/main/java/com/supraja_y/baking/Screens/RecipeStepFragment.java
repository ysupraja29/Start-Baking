package com.supraja_y.baking.Screens;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.supraja_y.baking.Models.Recipe;
import com.supraja_y.baking.Models.Step;
import com.supraja_y.baking.R;

import java.util.ArrayList;
import java.util.List;

import static com.supraja_y.baking.Screens.RecipeActivity.SEL_STEPS;
import static com.supraja_y.baking.Screens.RecipeDetailsActivity.SEL_POSITION;
import static com.supraja_y.baking.Screens.RecipeDetailsActivity.SEL_RECIPES;

/**
 * Created by suprajayerranagu on 22/07/17.
 */

public class RecipeStepFragment extends Fragment {
    private BandwidthMeter bandwidthMeter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int selectedIndex;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    ArrayList<Recipe> recipe;
    String recipeName;
    TextView textView;
    private String videoURL;
    private View rootView;
    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;

    public RecipeStepFragment() {

    }

    private ListItemClickListener itemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> allSteps, int Index, String recipeName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recipe = new ArrayList<>();
        shouldAutoPlay = true;
        clearResumePosition();
        itemClickListener = (RecipeDetailsActivity) getActivity();

        bandwidthMeter = new DefaultBandwidthMeter();


        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(SEL_STEPS);
            selectedIndex = savedInstanceState.getInt(SEL_POSITION);
            recipeName = savedInstanceState.getString("Title");


        } else {
            steps = getArguments().getParcelableArrayList(SEL_STEPS);
            if (steps != null) {
                steps = getArguments().getParcelableArrayList(SEL_STEPS);
                selectedIndex = getArguments().getInt(SEL_POSITION);
                recipeName = getArguments().getString("Title");
            } else {
                recipe = getArguments().getParcelableArrayList(SEL_RECIPES);
                //casting List to ArrayList
                steps = (ArrayList<Step>) recipe.get(0).getSteps();
                selectedIndex = 0;
            }

        }

        rootView = inflater.inflate(R.layout.recipe_step_fragment_bp, container, false);
        textView = (TextView) rootView.findViewById(R.id.recipe_step_detail_text);
        textView.setText(steps.get(selectedIndex).getDescription());

        Button mPrevStep = (Button) rootView.findViewById(R.id.previousStep);
        Button mNextstep = (Button) rootView.findViewById(R.id.nextStep);
        textView.setVisibility(View.VISIBLE);

        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        videoURL = steps.get(selectedIndex).getVideoURL();

        if (rootView.findViewWithTag("sw600dp-port-recipe_step_detail") != null) {
            recipeName = ((RecipeDetailsActivity) getActivity()).recipeName;
            ((RecipeDetailsActivity) getActivity()).getSupportActionBar().setTitle(recipeName);
        }

        String imageUrl = steps.get(selectedIndex).getThumbnailURL();
        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            ImageView thumbImage = (ImageView) rootView.findViewById(R.id.thumbImage);
            Picasso.with(getContext()).load(builtUri).into(thumbImage);
        }

        if (!videoURL.isEmpty()) {


            initializePlayer(Uri.parse(steps.get(selectedIndex).getVideoURL()));

            if (rootView.findViewWithTag("sw600dp-land-recipe_step_detail") != null) {
                getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                simpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            } else if (isInLandscapeMode(getContext())) {
                textView.setVisibility(View.GONE);
            }
        } else {
            player = null;
            simpleExoPlayerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.eye_off_outline));
            simpleExoPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }


        mPrevStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (steps.get(selectedIndex).getId() > 0) {
                    if (player != null) {
                        player.stop();
                    }
                    itemClickListener.onListItemClick(steps, steps.get(selectedIndex).getId() - 1, recipeName);
                } else {
                    Toast.makeText(getActivity(), "You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mNextstep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int lastIndex = steps.size() - 1;
                if (steps.get(selectedIndex).getId() < steps.get(lastIndex).getId()) {
                    if (player != null) {
                        player.stop();
                    }
                    itemClickListener.onListItemClick(steps, steps.get(selectedIndex).getId() + 1, recipeName);
                } else {
                    Toast.makeText(getContext(), "You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

                }
            }
        });


        return rootView;
    }

    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }
            player.prepare(mediaSource, !haveResumePosition, false);
            //  player.prepare(mediaSource);
            player.setPlayWhenReady(shouldAutoPlay);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SEL_STEPS, steps);
        currentState.putInt(SEL_POSITION, selectedIndex);
        currentState.putString("Title", recipeName);
    }

    public boolean isInLandscapeMode(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
       // if (player != null) {
            if (!videoURL.isEmpty()) {

                initializePlayer(Uri.parse(steps.get(selectedIndex).getVideoURL()));

            }
       // }


    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = player.isCurrentWindowSeekable() ? Math.max(0, player.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }
    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

}
