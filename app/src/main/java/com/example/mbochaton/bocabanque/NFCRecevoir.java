package com.example.mbochaton.bocabanque;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.OperationBancaire;
import com.example.mbochaton.bocabanque.models.Utilisateur;
import com.example.mbochaton.bocabanque.services.RequestHelper;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NFCRecevoir extends AppCompatActivity {
    private TextView mTextView;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private double montant;
    private long userPId, maxId, idUser;
    private Utilisateur userP;
    private String compteCred, compteDeb;
    private CompteBancaire compteCredit, compteDebit;
    private OperationBancaire operationDebit, operationCredit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcrecevoir);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                montant = 0;
                compteCred = "";
                idUser = 0;
            } else {
                montant = extras.getDouble("montant");
                compteCred = extras.getString("compteCredit");
                idUser = extras.getLong("idUser");
            }
        } else {
            montant = (Double) savedInstanceState.getSerializable("montant");
            compteCred = (String) savedInstanceState.getSerializable("compteCredit");
            idUser = (Long) savedInstanceState.getSerializable("idUser");
        }


        //Toast.makeText(this, String.valueOf(montant) , Toast.LENGTH_LONG).show();

        //mTextView = (TextView)findViewById(R.id.txtMessagesReceived);

        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            Toast.makeText(this, "Approchez vous d'un appareil" , Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Pas de NFC" , Toast.LENGTH_SHORT).show();
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s += (new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding));
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }

        String[] parts = s.split(";");
        userPId = Long.parseLong(parts[0]);
        compteDeb = parts[1];

        loadUtilisateur();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private void loadUtilisateur() {
        RequestHelper.provideService().utilisateur(userPId).enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if(response.isSuccessful()) {
                    userP = response.body();

                    RequestHelper.provideService().listOperations().enqueue(new Callback<List<OperationBancaire>>() {
                        @Override
                        public void onResponse(Call<List<OperationBancaire>> call, Response<List<OperationBancaire>> response) {
                            if (response.isSuccessful()) {
                                List<OperationBancaire> operations = response.body();
                                maxId = 0;
                                for (int i = 0; i < operations.size(); i++) {
                                    if (operations.get(i).getId() > maxId) {
                                        maxId = operations.get(i).getId();
                                    }
                                }
                                addOperationDeb();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<OperationBancaire>> call, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {   }
        });
    }

    private void addOperationDeb() {
        RequestHelper.provideService().listComptes().enqueue(new Callback<List<CompteBancaire>>() {
            @Override
            public void onResponse(Call<List<CompteBancaire>> call, Response<List<CompteBancaire>> response) {
                if(response.isSuccessful()) {
                    List<CompteBancaire> comptes = response.body();
                    for (int i = 0; i < comptes.size(); i++) {
                        if(comptes.get(i).getIdUser() == userPId && comptes.get(i).getIntitule().equals(compteDeb)) {
                            compteDebit = comptes.get(i);
                        }
                    }
                    operationDebit = new OperationBancaire();
                    operationDebit.setDescription("Retrait NFC");
                    operationDebit.setMontant(-(montant));
                    operationDebit.setIdcompte(compteDebit.getId());
                    operationDebit.setId(maxId + 1000);
                    RequestHelper.provideService().createOperation(operationDebit).enqueue(new Callback<OperationBancaire>() {
                        @Override
                        public void onResponse(Call<OperationBancaire> call, Response<OperationBancaire> response) {
                            if(response.isSuccessful()) {
                                double ancienSolde = compteDebit.getSolde();
                                compteDebit.setSolde(ancienSolde - montant);

                                RequestHelper.provideService().modifCompte(compteDebit.getId(), compteDebit).enqueue(new Callback<CompteBancaire>() {
                                    @Override
                                    public void onResponse(Call<CompteBancaire> call, Response<CompteBancaire> response) {
                                        if(response.isSuccessful()) {
                                            addOperationCred();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<CompteBancaire> call, Throwable t) {  }
                                });
                            } else {
                                Toast.makeText(NFCRecevoir.this, "Opération bancaire non créé", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<OperationBancaire> call, Throwable t) {
                            Toast.makeText(NFCRecevoir.this, "Opération bancaire non créé", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(NFCRecevoir.this, "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(NFCRecevoir.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addOperationCred() {
        RequestHelper.provideService().listComptes().enqueue(new Callback<List<CompteBancaire>>() {
            @Override
            public void onResponse(Call<List<CompteBancaire>> call, Response<List<CompteBancaire>> response) {
                if(response.isSuccessful()) {
                    List<CompteBancaire> comptes = response.body();
                    for (int i = 0; i < comptes.size(); i++) {
                        if(comptes.get(i).getIdUser() == idUser && comptes.get(i).getIntitule().equals(compteCred)) {
                            compteCredit = comptes.get(i);
                        }
                    }
                    operationCredit = new OperationBancaire();
                    operationCredit.setDescription("Dépôt NFC");
                    operationCredit.setIdcompte(compteCredit.getId());
                    operationCredit.setId(maxId + 1001);
                    operationCredit.setMontant(montant);
                    RequestHelper.provideService().createOperation(operationCredit).enqueue(new Callback<OperationBancaire>() {
                        @Override
                        public void onResponse(Call<OperationBancaire> call, Response<OperationBancaire> response) {
                            if(response.isSuccessful()) {
                                double ancienSolde = compteCredit.getSolde();
                                compteCredit.setSolde(ancienSolde + montant);

                                RequestHelper.provideService().modifCompte(compteCredit.getId(), compteCredit).enqueue(new Callback<CompteBancaire>() {
                                    @Override
                                    public void onResponse(Call<CompteBancaire> call, Response<CompteBancaire> response) {
                                        if(response.isSuccessful()) {
                                            Toast.makeText(NFCRecevoir.this, "Transfert validé", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<CompteBancaire> call, Throwable t) {  }
                                });
                            } else {
                                Toast.makeText(NFCRecevoir.this, "Opération bancaire non créé", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<OperationBancaire> call, Throwable t) {
                            Toast.makeText(NFCRecevoir.this, "Opération bancaire non créé", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(NFCRecevoir.this, "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(NFCRecevoir.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }
}