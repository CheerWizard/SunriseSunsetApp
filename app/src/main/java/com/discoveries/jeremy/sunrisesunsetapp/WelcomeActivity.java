package com.discoveries.jeremy.sunrisesunsetapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    ImageView liberBtn ;

    Button enjoyBtn;

    TextView rightsReserved , helpUs , vejen;

    Typeface maleModern;

    Animation increaseScale , rotate , alpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        identify();
        initFonts();
        loadAnimations();
        startAnimations();
        setListeners();
    }

    public void identify() {
        liberBtn = (ImageView) findViewById(R.id.liberBtn);
        enjoyBtn = (Button) findViewById(R.id.enjoy);
        rightsReserved = (TextView) findViewById(R.id.rightsReserved);
        vejen = (TextView) findViewById(R.id.vejen);
        helpUs = (TextView) findViewById(R.id.helpUs);
    }

    public void initFonts() {
        maleModern = Typeface.createFromAsset(getApplicationContext().getAssets() , "font/AlphaMaleModern.ttf");
        vejen.setTypeface(maleModern);
    }

    public void loadAnimations() {
        increaseScale = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.anim_increase_scale);
        rotate = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.anim_rotate);
        alpha = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.anim_alpha);
    }

    public void startAnimations() {
        enjoyBtn.startAnimation(increaseScale);
        vejen.startAnimation(increaseScale);

        helpUs.startAnimation(alpha);
        rightsReserved.startAnimation(alpha);
    }

    public void setListeners() {
        liberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                liberBtn.startAnimation(rotate);
            }
        });
        enjoyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this , LauncherActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welcome_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.exit:
                WelcomeActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
