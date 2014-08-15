package com.example.sherlockzhong.guesturedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainContainer = (LinearLayout) findViewById(R.id.main_container);
        final CardHorizontalScrollView redCard = generateCard(mainContainer, R.layout.red_card, 30, 20);
        final CardHorizontalScrollView greenCard = generateCard(mainContainer, R.layout.green_card, 30, 20);
        final CardHorizontalScrollView blueCard = generateCard(mainContainer, R.layout.blue_card, 30, 20);
        redCard.setCardID("red red red");
        greenCard.setCardID("green green green");
        blueCard.setCardID("blue blue blue");
        mainContainer.addView(redCard);
        mainContainer.addView(greenCard);
        mainContainer.addView(blueCard);
        redCard.setVisibility(View.INVISIBLE);
        greenCard.setVisibility(View.INVISIBLE);
        blueCard.setVisibility(View.INVISIBLE);

        mainContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                redCard.setVisibility(View.VISIBLE);
                greenCard.setVisibility(View.VISIBLE);
                blueCard.setVisibility(View.VISIBLE);

                Animation animation1 = generateFlyInAnimation(500, "top-left");
                View test1 = redCard;
                test1.startAnimation(animation1);

                Animation animation2 = generateFlyInAnimation(750, "right-bottom");
                View test2 = greenCard;
                test2.startAnimation(animation2);

                Animation animation3 = generateFlyInAnimation(1000, "top-right");
                View test3 = blueCard;
                test3.startAnimation(animation3);
            }
        }, 2000);

        final LinearLayout popup = (LinearLayout) findViewById(R.id.popup_window);
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation animation = new
                        TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 0.0f,
                        Animation.RELATIVE_TO_PARENT, 1.0f);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.setDuration(500);
                popup.startAnimation(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private CardHorizontalScrollView generateCard(LinearLayout container, int layoutId, int marginLeftRight, int marginTopBottom) {
        Display display = getWindowManager().getDefaultDisplay();
        final int windowWidth = display.getWidth();
        final CardHorizontalScrollView cardScroller = (CardHorizontalScrollView) getLayoutInflater().inflate(R.layout.card_container, container, false);
        LinearLayout cardContainer = (LinearLayout) cardScroller.findViewById(R.id.card_container);
        View card = getLayoutInflater().inflate(layoutId, cardContainer, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(windowWidth - marginLeftRight * 2, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        params.setMargins(windowWidth + marginLeftRight, marginTopBottom, windowWidth + marginLeftRight, marginTopBottom);
        card.setLayoutParams(params);
        cardContainer.addView(card);
        cardScroller.setScrollDistance(windowWidth);
        cardScroller.post(new Runnable() {
            @Override
            public void run() {
                cardScroller.scrollTo(windowWidth, 0);
            }
        });
        return cardScroller;
    }

    public Animation generateFlyInAnimation(int duration, String direction) {
        float x = 0.0f;
        float y = 0.0f;
        String[] directions = direction.split("-");
        if (directions.length > 1) {
            if (directions[1].equalsIgnoreCase("left")) {
                x = -1.0f;
            } else if (directions[1].equalsIgnoreCase("top")) {
                y = -1.0f;
            } else if (directions[1].equalsIgnoreCase("right")) {
                x = 1.0f;
            } else if (directions[1].equalsIgnoreCase("bottom")) {
                y = 1.0f;
            }
        }
        if (directions[0].equalsIgnoreCase("left")) {
            x = -1.0f;
        } else if (directions[0].equalsIgnoreCase("top")) {
            y = -1.0f;
        } else if (directions[0].equalsIgnoreCase("right")) {
            x = 1.0f;
        } else if (directions[0].equalsIgnoreCase("bottom")) {
            y = 1.0f;
        }
        TranslateAnimation animation = new
                TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, x,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, y,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(duration);
        return animation;
    }
}
