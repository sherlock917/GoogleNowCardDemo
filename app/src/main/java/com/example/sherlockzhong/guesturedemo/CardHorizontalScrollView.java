package com.example.sherlockzhong.guesturedemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by SherlockZhong on 7/28/14.
 */
public class CardHorizontalScrollView extends HorizontalScrollView {

    private int scrollDistance = 0;
    private String cardID = "";

    public CardHorizontalScrollView (Context context) {
        super(context);
    }

    public CardHorizontalScrollView (Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent (MotionEvent ev) {
        cardMotionEvent(ev);
        return super.onTouchEvent(ev);
    }

    public void setScrollDistance (int scrollDistance) {
        this.scrollDistance = scrollDistance;
    }

    public void setCardID (String id) {
        this.cardID = id;
    }

    private void cardMotionEvent (MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN :
                break;
            case MotionEvent.ACTION_UP :
                detectScroll();
                break;
            case MotionEvent.ACTION_MOVE :
                break;
            default:
                break;
        }
    }

    private void scrollBack () {
        final CardHorizontalScrollView self = this;
        this.post(new Runnable() {
            @Override
            public void run() {
                self.smoothScrollTo(self.scrollDistance, 0);
            }
        });
    }

    private void detectScroll () {
        int scrollX = getScrollX();
        if (scrollX < this.scrollDistance * 2 / 3) {
            flyOut(0);
        } else if (scrollX > this.scrollDistance * 4 / 3) {
            flyOut(1);
        } else if (scrollX == this.scrollDistance) {
            showPopup();
        } else {
            scrollBack();
        }
    }

    private void flyOut(int direction) {
        final int _direction = direction;
        final CardHorizontalScrollView self = this;
        this.post(new Runnable() {
            @Override
            public void run() {
                if (_direction == 0) {
                    self.smoothScrollTo(0, 0);
                } else {
                    self.smoothScrollTo(self.scrollDistance * 2, 0);
                }
                self.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final LinearLayout cardContainer = (LinearLayout) self.findViewById(R.id.card_container);
                        self.removeView(cardContainer);
                        final LinearLayout mainContainer = (LinearLayout) self.getParent();
                        ScrollView mainScroller = (ScrollView) mainContainer.getParent();
                        RelativeLayout rootContainer = (RelativeLayout) mainScroller.getParent();
                        final Button undoButton = (Button) rootContainer.findViewById(R.id.undo_button);
                        undoButton.setVisibility(View.VISIBLE);
                        Animation fadeIn = AnimationUtils.loadAnimation(undoButton.getContext(), R.anim.fade_in);
                        undoButton.startAnimation(fadeIn);
                        final Runnable disappear = new Runnable() {
                            @Override
                            public void run() {
                                if (undoButton.getVisibility() == View.VISIBLE) {
                                    Animation fadeOut = AnimationUtils.loadAnimation(undoButton.getContext(), R.anim.fade_out);
                                    undoButton.startAnimation(fadeOut);
                                }
                                undoButton.setVisibility(View.INVISIBLE);
                                mainContainer.removeView(self);
                            }
                        };
                        undoButton.postDelayed(disappear, 3000);
                        undoButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                self.addView(cardContainer);
                                self.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        self.scrollTo(self.scrollDistance, 0);
                                    }
                                });
                                undoButton.setVisibility(View.INVISIBLE);
                                undoButton.removeCallbacks(disappear);
                            }
                        });
                    }
                }, 500);
            }
        });
    }

    private void showPopup () {
        View mainContainer = (View) this.getParent();
        View mainScroller = (View) mainContainer.getParent();
        View root = (View) mainScroller.getParent();
        View popup = (View) root.findViewById(R.id.popup_window);
        TextView text = (TextView) popup.findViewById(R.id.test_text);
        text.setText(this.cardID);
        popup.setVisibility(VISIBLE);
        TranslateAnimation animation = new
                TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(500);
        popup.startAnimation(animation);
    }

}
