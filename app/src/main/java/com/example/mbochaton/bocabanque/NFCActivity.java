package com.example.mbochaton.bocabanque;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NFCActivity.this);

        switch (v.getId()) {
            case R.id.recevoir_card :
                View  demanderView = getLayoutInflater().inflate(R.layout.nfc_demander_dialog, null);
                builder.setTitle("Demande de paiement");
                Spinner demanderSpinner = (Spinner) demanderView.findViewById(R.id.spinner_dialog);
                final EditText montant = (EditText) demanderView.findViewById(R.id.montant_dialog);
                ArrayAdapter<String> demanderAdapter = new ArrayAdapter<String>(NFCActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1);
                demanderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                demanderSpinner.setAdapter(demanderAdapter);
                builder.setView(demanderView);
                builder.setPositiveButton("Valider",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String montantString = montant.getText().toString();
                                double montantDouble = 0;
                                if (!montantString.matches("")) {
                                    montantDouble = Double.parseDouble(montantString);
                                }
                                Intent i = new Intent(NFCActivity.this, NFCRecevoir.class);
                                i.putExtra("montant", montantDouble);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.envoyer_card :
                View  payerView = getLayoutInflater().inflate(R.layout.nfc_payer_dialog, null);
                builder.setTitle("Demande de paiement");
                Spinner payerSpinner = (Spinner) payerView.findViewById(R.id.spinner_dialog);
                ArrayAdapter<String> payerAdapter = new ArrayAdapter<String>(NFCActivity.this, android.R.layout.simple_spinner_item, android.R.id.text1);
                payerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                payerSpinner.setAdapter(payerAdapter);
                builder.setView(payerView);
                builder.setPositiveButton("Valider",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(NFCActivity.this, NFCEnvoyer.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                break;
            default :
                break;
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
