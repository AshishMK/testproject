package test.com.mygapplication.DetailPages;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;

import test.com.mygapplication.R;
import test.com.mygapplication.Utills.Screen;

public class DetailActivity extends AppCompatActivity {
    CollapsingToolbarLayout toolLay;
    Toolbar toolbar;
    int viewBarHeight =0,actvityChoice=0;
    boolean isViewBarShow =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //type of activity selected
        actvityChoice=getIntent().getIntExtra("choice",0);
        setContentView(actvityChoice==0?R.layout.activity_detail:R.layout.activity_detail_slidein_toolbar);
      if(actvityChoice==1) {
          setupToolbarSlide();
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  animateToolbar(false);
              }
          }, 300);
      }
    }

    /**
     * Sup tool bar to show slidedown animation on collapsing
     */
    void setupToolbarSlide(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setExpandedTitleColor(Color.WHITE);
        ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setCollapsedTitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolLay = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ((AppBarLayout) findViewById(R.id.app_bar)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (toolLay.getHeight()+verticalOffset < 2 * ViewCompat.getMinimumHeight(toolLay)) {
                    //toolLay.setContentScrim(new ColorDrawable(Color.WHITE));
                    colorizeToolbar(toolbar, Color.DKGRAY, DetailActivity.this);
                    animateToolbar(true);
                   // title.setTextColor( Color.DKGRAY);
                } else {
                    // toolLay.setContentScrim(new ColorDrawable(Color.TRANSPARENT));
                    animateToolbar(false);
                    //title.setTextColor( Color.WHITE);
                    colorizeToolbar(toolbar, Color.WHITE,DetailActivity.this);
                    //toolbar.setTitleTextColor(Color.WHITE);
                }
            }
        });

    }

    /**
     * method to expand and collapse toolbar background with animation
     * @param show
     */
    void animateToolbar(final boolean show) {
        if(isViewBarShow ==show)return;
        isViewBarShow =show;

        findViewById(R.id.viewBar).post(new Runnable() {
            @Override
            public void run() {
                if(viewBarHeight ==0) {
                    viewBarHeight = findViewById(R.id.viewBar).getHeight() + Screen.dp(25);
                    findViewById(R.id.viewBar).getLayoutParams().height = viewBarHeight;
                }

                ViewCompat.animate(findViewById(R.id.viewBar))
                        .setStartDelay(0)
                        .setDuration(200)
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(View view) {

                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                findViewById(R.id.viewBar).setBackgroundColor(Color.WHITE);
                                ViewCompat.setTranslationY(findViewById(R.id.viewBar),!show?-viewBarHeight :0);
                            }

                            @Override
                            public void onAnimationCancel(View view) {
                                findViewById(R.id.viewBar).setBackgroundColor(Color.WHITE);
                                ViewCompat.setTranslationY(findViewById(R.id.viewBar),!show?-viewBarHeight :0);

                            }
                        })
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .translationY(show ? (0) : -viewBarHeight);


            }
        });

    }

    /**
     * Use this method to colorize toolbar icons to the desired target color
     *
     * @param toolbarView       toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     * @param activity          reference to activity needed to register observers
     */
    public static void colorizeToolbar(Toolbar toolbarView, int toolbarIconsColor, Activity activity) {
        final PorterDuffColorFilter colorFilter
                = new PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY);

        for (int i = 0; i < toolbarView.getChildCount(); i++) {
            final View v = toolbarView.getChildAt(i);

            //Step 1 : Changing the color of back button (or open drawer button).
            if (v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
            }

            if (v instanceof ActionMenuView) {
                for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) {

                    //Step 2: Changing the color of any ActionMenuViews - icons that
                    //are not back button, nor text, nor overflow menu icon.
                    final View innerView = ((ActionMenuView) v).getChildAt(j);

                    if (innerView instanceof ActionMenuItemView) {
                        int drawablesCount = ((ActionMenuItemView) innerView).getCompoundDrawables().length;
                        for (int k = 0; k < drawablesCount; k++) {
                            if (((ActionMenuItemView) innerView).getCompoundDrawables()[k] != null) {
                                final int finalK = k;

                                //Important to set the color filter in seperate thread,
                                //by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ActionMenuItemView) innerView).getCompoundDrawables()[finalK].setColorFilter(colorFilter);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            //Step 3: Changing the color of title and subtitle.
            toolbarView.setTitleTextColor(toolbarIconsColor);
            toolbarView.setSubtitleTextColor(toolbarIconsColor);

            //Step 4: Changing the color of the Overflow Menu icon.
            // setOverflowButtonColor(activity, colorFilter);
        }
    }
}
