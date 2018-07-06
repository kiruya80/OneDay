package com.ulling.lib.core.viewutil.animation;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import java.util.Arrays;

public class ReboundRecyclerViewAnimator {
    /**
     * Initial delay before to show items - in ms
     */
    private static final int INIT_DELAY = 1000;
    /**
     * Initial entrance tension parameter.
     * See https://facebook.github.io/rebound/
     * 초기 리스트 애니메이션
     * 로우 올라오는 속도?
     * 낮을수록 천천히 올라옴
     * 250;
     */
    private static final int INIT_TENSION = 100;
    /**
     * Initial entrance friction parameter.
     * 18;
     */
    private static final int INIT_FRICTION = 20;
    /**
     * Scroll entrance animation tension parameter.
     * 300;
     */
    private static final int SCROLL_TENSION = 300;
    /**
     * Scroll entrance animation friction parameter.
     * 30;
     */
    private static final int SCROLL_FRICTION = 30;
    private int mHeight;
    private RecyclerView mRecyclerView;
    private SpringSystem mSpringSystem;
    private boolean mFirstViewInit = true;
    private int mLastPosition = -1;
    private int visablePosition = 0;
    private int mStartDelay;
    private int ADD_ITEM_DELAY = 70;
    private boolean endAnimation = false;
    public Boolean[] aniFlag;

    public ReboundRecyclerViewAnimator(RecyclerView recyclerView, int startDelay, int visablePosition) {
        mRecyclerView = recyclerView;
        mSpringSystem = SpringSystem.create();
        mHeight = mRecyclerView.getResources().getDisplayMetrics().heightPixels;
        if (startDelay < 0) {
            mStartDelay = INIT_DELAY;
        } else {
            mStartDelay = startDelay;
        }
        this.visablePosition = visablePosition;
        aniFlag = new Boolean[visablePosition];
        Arrays.fill(aniFlag, false);
    }

    /**
     * 초기에 보이는 화면에 사용
     *
     * @param item
     */
    public void onCreateViewHolder(View item, int position) {
        if (visablePosition == 0 || visablePosition <= position) {
            Arrays.fill(aniFlag, true);
            return;
        }
        if (aniFlag != null && aniFlag.length <= position) {
            Arrays.fill(aniFlag, true);
            return;
        }
        if (mRecyclerView != null && mFirstViewInit && !endAnimation && !aniFlag[position]) {
            aniFlag[position] = true;
            slideInBottom(item, mStartDelay, INIT_TENSION, INIT_FRICTION);
            mStartDelay += ADD_ITEM_DELAY;
        }
    }

    /**
     * 스크롤 하는 경우에만 사용
     *
     * @param item
     * @param position
     */
    public void onBindViewHolder(View item, int position) {
        if (visablePosition != 0 && visablePosition < position) {
            endAnimation = true;
            return;
        }
        if (!mFirstViewInit && position > mLastPosition) {
            slideInBottom(item, 0, SCROLL_TENSION, SCROLL_FRICTION);
            mLastPosition = position;
        }
    }

    private boolean slideInBottom(final View item,
                                  final int delay,
                                  final int tension,
                                  final int friction) {
        // Move item far outside the RecyclerView
        item.setTranslationY(mHeight);
        Runnable startAnimation = new Runnable() {
            @Override
            public void run() {
                final SpringConfig config = new SpringConfig(tension, friction);
                final Spring spring = mSpringSystem.createSpring();
                spring.setSpringConfig(config);
                spring.addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        /**
                         * Decrease translationY until 0.
                         */
                        float val = (float) (mHeight - spring.getCurrentValue());
                        item.setTranslationY(val);
                    }

                    @Override
                    public void onSpringEndStateChange(Spring spring) {
//                        super.onSpringEndStateChange(spring);
                        mFirstViewInit = false;
                    }

                    @Override
                    public void onSpringAtRest(Spring spring) {
                        super.onSpringAtRest(spring);
                    }

                    @Override
                    public void onSpringActivate(Spring spring) {
                        super.onSpringActivate(spring);
                    }
                });
                // Set the spring in motion; moving from 0 to height
                spring.setEndValue(mHeight);
            }
        };
        if (item != null) {
            return item.postDelayed(startAnimation, delay);
        } else {
            return false;
        }
    }
}
