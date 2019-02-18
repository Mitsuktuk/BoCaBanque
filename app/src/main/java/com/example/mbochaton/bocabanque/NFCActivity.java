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
import android.widget.Toast;

import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.services.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NFCActivity extends AppCompatActivity implements CardView.OnClickListener {
    private CardView recevoirCard;
    private CardView envoyerCard;
    private List<String> mCompteBancaireList = new ArrayList<String>();
    private long idUser;
    private Spinner demanderSpinner;
    private Spinner payerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idUser = 0;
            } else {
                idUser = extras.getLong("idUser");
            }
        } else {
            idUser = (Long)savedInstanceState.getSerializable("idUser");
        }

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
                final View  demanderView = getLayoutInflater().inflate(R.layout.nfc_demander_dialog, null);
                builder.setTitle("Demande de paiement");
                demanderSpinner = (Spinner) demanderView.findViewById(R.id.spinner_dialog);
                final EditText montant = (EditText) demanderView.findViewById(R.id.montant_dialog);
                loadComptes(0);
                builder.setView(demanderView);
                builder.setPositiveButton("Valider",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String compteCredit = demanderSpinner.getSelectedItem().toString();
                                String montantString = montant.getText().toString();
                                double montantDouble = 0;
                                if (!montantString.matches("")) {
                                    montantDouble = Double.parseDouble(montantString);
                                }
                                Intent i = new Intent(NFCActivity.this, NFCRecevoir.class);
                                i.putExtra("montant", montantDouble);
                                i.putExtra("compteCredit", compteCredit);
                                i.putExtra("idUser", idUser);
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
                builder.setTitle("Payer");
                payerSpinner = (Spinner) payerView.findViewById(R.id.spinner_dialog);
                loadComptes(1);
                builder.setView(payerView);
                builder.setPositiveButton("Valider",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String compteDebit = payerSpinner.getSelectedItem().toString();
                                Intent i = new Intent(NFCActivity.this, NFCEnvoyer.class);
                                i.putExtra("compteDebit", compteDebit);
                                i.putExtra("idUser", idUser);
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

    private void loadComptes(final int cas) {
        RequestHelper.provideService().listComptes().enqueue(new Callback<List<CompteBancaire>>() {
            @Override
            public void onResponse(Call<List<CompteBancaire>> call, Response<List<CompteBancaire>> response) {
                if(response.isSuccessful()) {
                    List<CompteBancaire> comptes = response.body();

                    for (int i = 0; i < comptes.size(); i++) {
                        if (comptes.get(i).getIdUser() == idUser) {
                            mCompteBancaireList.add(comptes.get(i).getIntitule());
                        }
                    }

                    switch(cas) {
                        case 0:
                            ArrayAdapter<String> demanderAdapter = new ArrayAdapter<String>(NFCActivity.this, android.R.layout.simple_spinner_item, mCompteBancaireList);
                            demanderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            demanderSpinner.setAdapter(demanderAdapter);
                            break;
                        case 1:
                            ArrayAdapter<String> payerAdapter = new ArrayAdapter<String>(NFCActivity.this, android.R.layout.simple_spinner_item, mCompteBancaireList);
                            payerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            payerSpinner.setAdapter(payerAdapter);
                            break;
                    }

                } else {
                    Toast.makeText(NFCActivity.this, "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(NFCActivity.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }

}
