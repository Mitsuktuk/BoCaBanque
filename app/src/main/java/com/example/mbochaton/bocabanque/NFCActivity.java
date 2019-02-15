package com.example.mbochaton.bocabanque;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NFCActivity extends AppCompatActivity implements CardView.OnClickListener {
    private CardView recevoirCard;
    private CardView envoyerCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Krub-Regular.ttf");
        TextView tv = (TextView) findViewById(R.id.tv_titre);
        tv.setTypeface(tf);

        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recevoirCard = (CardView)findViewById(R.id.recevoir_card);
        recevoirCard.setOnClickListener(this);
        envoyerCard = (CardView)findViewById(R.id.envoyer_card);
        envoyerCard.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.recevoir_card :
                i = new Intent(this, NFCRecevoir.class);
                break;
            case R.id.envoyer_card :
                i = new Intent(this, NFCEnvoyer.class);
                break;
            default :
                i = new Intent(this, NFCRecevoir.class);
                break;
        }

        startActivity(i);
    }
}
